package com.adiandrodev.filmadda.presentation.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.adiandrodev.filmadda.BuildConfig
import com.adiandrodev.filmadda.MyApplication
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_ID_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.SEASON_NO_KEY
import com.adiandrodev.filmadda.R
import com.adiandrodev.filmadda.data.model.*
import com.adiandrodev.filmadda.databinding.ActivityTvShowsBinding
import com.adiandrodev.filmadda.databinding.RatingDialogBinding
import com.adiandrodev.filmadda.di.Injector
import com.adiandrodev.filmadda.domain.model.TvDetails
import com.adiandrodev.filmadda.presentation.adapter.*
import com.adiandrodev.filmadda.presentation.viewmodel.TvShowViewModel
import com.adiandrodev.filmadda.presentation.viewmodel.TvShowViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.reflect.full.memberProperties

class TvShowsActivity : BaseActivity() {

    private lateinit var binding: ActivityTvShowsBinding

    @Inject
    lateinit var viewModelFactory: TvShowViewModelFactory
    private lateinit var viewModel: TvShowViewModel

    private var tvShowId: Int? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var countryKey: String
    private lateinit var countryName: String
    private var accountId = -1
    private lateinit var sessionId: String

    private lateinit var tvDetails: TvDetails

    private lateinit var videoAdapter: VideosRecyclerAdapter
    private lateinit var imagesAdapter: ImagesRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tv_shows)
        (application as Injector).createTvShowSubComponent().inject(this)
        viewModel = viewModelFactory.create(TvShowViewModel::class.java)
        tvShowId = intent.getIntExtra(MEDIA_ID_KEY, -1)

        loadSharedPrefData()

        initAdapter()

        setClickListeners()

        if (super.isNetworkConnected()) {
            getTvShowDetailsFromTmdb()
        } else {
            super.showErrorDialog()
        }
    }

    private fun loadSharedPrefData() {
        sharedPreferences = getSharedPreferences(MyApplication.APPLICATION_PREFERENCES, MODE_PRIVATE)
        countryKey = sharedPreferences.getString(MyApplication.COUNTRY_KEY, "US")?:"US"
        countryName = sharedPreferences.getString(MyApplication.COUNTRY_NAME, "United States of America")?:"United States of America"
        accountId = sharedPreferences.getInt(MyApplication.ACCOUNT_ID_KEY, -1)
        sessionId = sharedPreferences.getString(MyApplication.SESSION_ID_KEY, "")?:""
    }

    private fun initAdapter() {
        videoAdapter = getVideoAdapter()
        imagesAdapter = getImageAdapter()
    }

    private fun getVideoAdapter(): VideosRecyclerAdapter {
        return VideosRecyclerAdapter(
            null, { key, view ->
                loadAndSetImageUsingGlide(
                    "$key/maxresdefault.jpg",
                    null,
                    view,
                    BuildConfig.YOUTUBE_VIDEO_THUMBNAIL_BASE_URL
                )
            }, {
                val videoUrl = "https://www.youtube.com/watch?v=$it"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    intent.setPackage("com.android.chrome")
                    startActivity(intent)
                }
            }, {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun getImageAdapter(): ImagesRecyclerAdapter {
        return ImagesRecyclerAdapter(this, null, true, { imagePath, view ->
            loadAndSetImageUsingGlide(
                imagePath,
                R.drawable.custom_poster,
                view
            )
        }, { imagePath ->
            val intent = Intent(this, ImageActivity::class.java)
            intent.putExtra(MyApplication.IMAGE_PATH_KEY, imagePath)
            startActivity(intent)
        }
        )
    }

    private fun setClickListeners() {
        binding.backBtn.setOnClickListener { finish() }
        binding.videosAndImagesRg.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rb_videos -> setUpVideos()
                R.id.rb_backdrops -> setUpBackdrops()
                R.id.rb_posters -> setUpPosters()
            }
        }
        binding.homepageBtn.setOnClickListener {
            tvDetails.tvShow?.homepage?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
            }
        }
        binding.imdbBtn.setOnClickListener {
            tvDetails.tvShow?.external_ids?.imdb_id?.let { id ->
                val imdbUrl = BuildConfig.IMDB_BASE_URL + id
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(imdbUrl))
                startActivity(intent)
            }
        }
        binding.facebookBtn.setOnClickListener {
            tvDetails.tvShow?.external_ids?.facebook_id?.let { id ->
                val facebookPageUrl = BuildConfig.FACEBOOK_BASE_URL + id
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookPageUrl))
                startActivity(intent)
            }
        }
        binding.instagramBtn.setOnClickListener {
            tvDetails.tvShow?.external_ids?.instagram_id?.let { id ->
                val instagramPageUrl = BuildConfig.INSTAGRAM_BASE_URL + id
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramPageUrl))
                startActivity(intent)
            }
        }
        binding.twitterBtn.setOnClickListener {
            tvDetails.tvShow?.external_ids?.twitter_id?.let { id ->
                val twitterPageUrl = BuildConfig.TWITTER_BASE_URL + id
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(twitterPageUrl))
                startActivity(intent)
            }
        }
        binding.wikipediaBtn.setOnClickListener {
            tvDetails.tvShow?.external_ids?.wikidata_id?.let { id ->
                val wikipediaPageUrl = BuildConfig.WIKIDATA_BASE_URL + id
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(wikipediaPageUrl))
                startActivity(intent)
            }
        }
        binding.favBtn.setOnClickListener {
            if (super.isNetworkConnected()) {
                tvDetails.tvShow?.id?.let { id ->
                    blockActions()
                    if (tvDetails.tvShow?.account_states?.favorite == true) {
                        viewModel.addOrRemoveFromFavList(accountId, sessionId, false, id).observe(this) { success ->
                            if (success?.success == true) {
                                tvDetails.tvShow?.account_states?.favorite = false
                                setUpAccountStates()
                            } else {
                                Snackbar.make(binding.root, "Something went wrong, please try again.", Snackbar.LENGTH_SHORT)
                                    .setBackgroundTint(resources.getColor(R.color.background_color_rev, null))
                                    .setTextColor(resources.getColor(R.color.background_color, null))
                                    .show()
                            }
                        }
                    } else if (tvDetails.tvShow?.account_states?.favorite == false) {
                        viewModel.addOrRemoveFromFavList(accountId, sessionId, true, id).observe(this) { success ->
                            if (success?.success == true) {
                                tvDetails.tvShow?.account_states?.favorite = true
                                setUpAccountStates()
                            } else {
                                Snackbar.make(binding.root, "Something went wrong, please try again.", Snackbar.LENGTH_SHORT)
                                    .setBackgroundTint(resources.getColor(R.color.background_color_rev, null))
                                    .setTextColor(resources.getColor(R.color.background_color, null))
                                    .show()
                            }
                        }
                    }
                    unBlockActions()
                }
            } else {
                super.showErrorDialog()
            }
        }
        binding.watchlistBtn.setOnClickListener {
            if (super.isNetworkConnected()) {
                tvDetails.tvShow?.id?.let { id ->
                    blockActions()
                    if (tvDetails.tvShow?.account_states?.watchlist == true) {
                        viewModel.addOrRemoveFromWatchList(accountId, sessionId, false, id).observe(this) { success ->
                            if (success?.success == true) {
                                tvDetails.tvShow?.account_states?.watchlist = false
                                setUpAccountStates()
                            } else {
                                Snackbar.make(binding.root, "Something went wrong, please try again.", Snackbar.LENGTH_SHORT)
                                    .setBackgroundTint(resources.getColor(R.color.background_color_rev, null))
                                    .setTextColor(resources.getColor(R.color.background_color, null))
                                    .show()
                            }
                        }
                    } else if (tvDetails.tvShow?.account_states?.watchlist == false) {
                        viewModel.addOrRemoveFromWatchList(accountId, sessionId, true, id).observe(this) { success ->
                            if (success?.success == true) {
                                tvDetails.tvShow?.account_states?.watchlist = true
                                setUpAccountStates()
                            } else {
                                Snackbar.make(binding.root, "Something went wrong, please try again.", Snackbar.LENGTH_SHORT)
                                    .setBackgroundTint(resources.getColor(R.color.background_color_rev, null))
                                    .setTextColor(resources.getColor(R.color.background_color, null))
                                    .show()
                            }
                        }
                    }
                    unBlockActions()
                }
            } else {
                super.showErrorDialog()
            }
        }
        binding.rateBtn.setOnClickListener {
            if (super.isNetworkConnected()) {
                showRatingDialog()
            } else {
                super.showErrorDialog()
            }
        }
    }

    private fun setUpPosters() {
        binding.videosAndImagesRv.adapter = imagesAdapter
        tvDetails.images?.let {
            (binding.videosAndImagesRv.adapter as ImagesRecyclerAdapter).dataSetChanged(it, true)
        }
    }

    private fun setUpBackdrops() {
        binding.videosAndImagesRv.adapter = imagesAdapter
        tvDetails.images?.let {
            (binding.videosAndImagesRv.adapter as ImagesRecyclerAdapter).dataSetChanged(it, false)
        }
    }

    private fun setUpVideos() {
        binding.videosAndImagesRv.adapter = videoAdapter
        tvDetails.videos?.let {
            (binding.videosAndImagesRv.adapter as VideosRecyclerAdapter).dataSetChanged(it)
        }
    }

    private fun blockActions() {
        binding.favBtn.background = ResourcesCompat.getDrawable(resources, R.drawable.icon_bg_grey, null)
        binding.favBtn.isClickable = false
        binding.favBtn.focusable = View.NOT_FOCUSABLE
        binding.watchlistBtn.background = ResourcesCompat.getDrawable(resources, R.drawable.icon_bg_grey, null)
        binding.watchlistBtn.isClickable = false
        binding.watchlistBtn.focusable = View.NOT_FOCUSABLE
        binding.rateBtn.background = ResourcesCompat.getDrawable(resources, R.drawable.icon_bg_grey, null)
        binding.rateBtn.isClickable = false
        binding.rateBtn.focusable = View.NOT_FOCUSABLE
    }

    private fun unBlockActions() {
        binding.favBtn.background = ResourcesCompat.getDrawable(resources, R.drawable.icon_bg, null)
        binding.favBtn.isClickable = true
        binding.favBtn.focusable = View.FOCUSABLE
        binding.watchlistBtn.background = ResourcesCompat.getDrawable(resources, R.drawable.icon_bg, null)
        binding.watchlistBtn.isClickable = true
        binding.watchlistBtn.focusable = View.FOCUSABLE
        binding.rateBtn.background = ResourcesCompat.getDrawable(resources, R.drawable.icon_bg, null)
        binding.rateBtn.isClickable = true
        binding.rateBtn.focusable = View.FOCUSABLE
    }

    private fun setUpAccountStates() {
        if (accountId != -1 && sessionId.isNotEmpty()) {
            if (tvDetails.tvShow?.account_states?.favorite == true) {
                binding.favBtn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.baseline_favorite_24_red, null))
            } else if (tvDetails.tvShow?.account_states?.favorite == false) {
                binding.favBtn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.baseline_favorite_24_white, null))
            }
            if (tvDetails.tvShow?.account_states?.watchlist == true) {
                binding.watchlistBtn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.baseline_bookmark_24_green, null))
            } else if (tvDetails.tvShow?.account_states?.watchlist == false) {
                binding.watchlistBtn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.baseline_bookmark_24_white, null))
            }
            if (tvDetails.tvShow?.account_states?.rated != null) {
                binding.rateBtn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.baseline_star_rate_24_yellow, null))
            } else {
                binding.rateBtn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.baseline_star_rate_24_white, null))
            }
        } else {
            binding.cl1.visibility = View.GONE
        }
    }

    private fun showRatingDialog() {
        val dialog = Dialog(this)
        val dialogBinding = RatingDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        tvDetails.tvShow?.account_states?.rated?.value?.let {
            dialogBinding.ratingBar.rating = it.toFloat()/2
        }
        dialogBinding.clearBtn.setOnClickListener {
            dialogBinding.ratingBar.rating = 0f
        }
        dialogBinding.doneBtn.setOnClickListener {
            val rating = dialogBinding.ratingBar.rating
            if (rating == 0f) {
                tvDetails.tvShow?.id?.let { id ->
                    viewModel.deleteRating(id, sessionId).observe(this) { success ->
                        if (success?.success == true) {
                            tvDetails.tvShow?.account_states?.rated = null
                            setUpAccountStates()
                        } else {
                            Snackbar.make(binding.root, "Something went wrong, please try again.", Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(resources.getColor(R.color.background_color_rev, null))
                                .setTextColor(resources.getColor(R.color.background_color, null))
                                .show()
                        }
                    }
                }
            } else {
                tvDetails.tvShow?.id?.let { id ->
                    viewModel.rateTvShow(id, sessionId, (rating * 2).toDouble()).observe(this) { success ->
                        if (success?.success == true) {
                            tvDetails.tvShow?.account_states?.rated = Rated((rating * 2).toInt())
                            setUpAccountStates()
                        } else {
                            Snackbar.make(binding.root, "Something went wrong, please try again.", Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(resources.getColor(R.color.background_color_rev, null))
                                .setTextColor(resources.getColor(R.color.background_color, null))
                                .show()
                        }
                    }
                }
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun getTvShowDetailsFromTmdb() {
        tvShowId?.let { id ->
            viewModel.getTvShowDetails(id, accountId, sessionId).observe(this) { res ->
                res?.let {
                    tvDetails = it
                    setMainPoster()
                    setBackgroundPoster()
                    setCastRecycler()
                    handleVideoPosterBackdropVisibility()
                    binding.videosAndImagesRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    setUpVideos()
                    setUpSeasons()
                    setUpRecommendationList()
                    setUpWatchProviders()
                    setUpAccountStates()
                    setUpLandingPages()
                    setUpGenres()
                    setUpUiTexts()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.mainUi.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setMainPoster() {
        val index = if (!tvDetails.images?.posters.isNullOrEmpty()) {
            0
        } else {
            -1
        }
        val path = if (index == -1) {
            tvDetails.tvShow?.poster_path
        } else {
            tvDetails.images?.posters?.get(0)?.file_path
        }

        loadAndSetImageUsingGlide(
            path?:"",
            R.drawable.custom_poster,
            binding.tvPosterIv
        )
    }

    private fun setBackgroundPoster() {
        val index = if ((tvDetails.images?.posters?.size ?: -1) >= 2) {
            1
        } else {
            -1
        }
        val path = if (index == -1) {
            tvDetails.tvShow?.backdrop_path?:""
        } else {
            tvDetails.images?.posters?.get(index)?.file_path?:""
        }
        loadAndSetImageUsingGlide(
            path,
            R.drawable.custom_backdrop,
            binding.backgroundPosterIv
        )
    }

    private fun setCastRecycler() {
        if (tvDetails.tvShow?.credits?.cast.isNullOrEmpty()) {
            binding.castLayout.visibility = View.GONE
        } else {
            binding.castRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            tvDetails.tvShow?.credits?.cast?.let {
                binding.castRv.adapter = CastRecyclerAdapter(it, { personId ->
                    val intent = Intent(this, PersonActivity::class.java)
                    intent.putExtra(MyApplication.PERSON_ID_KEY, personId)
                    startActivity(intent)
                }, { imagePath, view ->
                    loadAndSetImageUsingGlide(
                        imagePath,
                        R.drawable.custom_poster,
                        view
                    )
                })
            }
        }
    }

    private fun handleVideoPosterBackdropVisibility() {
        if (tvDetails.videos?.results.isNullOrEmpty()
            && tvDetails.images?.posters.isNullOrEmpty()
            && tvDetails.images?.backdrops.isNullOrEmpty()
        ) {
            binding.videosAndImagesLl.visibility = View.GONE
        } else {
            if (tvDetails.videos?.results.isNullOrEmpty()) {
                binding.rbVideos.visibility = View.GONE
            }
            if (tvDetails.images?.posters.isNullOrEmpty()) {
                binding.rbPosters.visibility = View.GONE
            }
            if (tvDetails.images?.backdrops.isNullOrEmpty()) {
                binding.rbBackdrops.visibility = View.GONE
            }
        }
    }

    private fun setUpSeasons() {
        if (tvDetails.tvShow?.seasons.isNullOrEmpty()) {
            binding.seasonsLayout.visibility = View.GONE
        } else {
            binding.seasonsRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.seasonsRv.adapter = SeasonsRecyclerAdapter(tvDetails.tvShow?.seasons, { posterPath, view ->
                loadAndSetImageUsingGlide(posterPath, R.drawable.custom_poster, view)
            }, {
                val intent = Intent(this, SeasonActivity::class.java)
                intent.putExtra(MEDIA_ID_KEY, tvShowId)
                intent.putExtra(SEASON_NO_KEY, it)
                startActivity(intent)
            })
        }
    }

    private fun formatDate(inputDate: String?): String {
        return if (inputDate.isNullOrEmpty()) {
            ""
        } else {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val outputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy")
            LocalDate.parse(inputDate, inputFormatter).format(outputFormatter)
        }
    }

    private fun setUpRecommendationList() {
        if (tvDetails.tvShow?.recommendations?.results.isNullOrEmpty()) {
            binding.recommendationLl.visibility = View.GONE
        } else {
            binding.recommendationRv.adapter = RecommendationRecyclerAdapter(tvDetails.tvShow?.recommendations,
                {imagePath, view ->
                    loadAndSetImageUsingGlide(imagePath, R.drawable.custom_poster, view)
                }, {mediaId, mediaType ->
                    val intent = when (mediaType) {
                        MyApplication.MEDIA_TYPE_MOVIE -> {
                            Intent(this, MovieActivity::class.java)
                        }
                        else -> Intent(this, TvShowsActivity::class.java)
                    }
                    intent.putExtra(MEDIA_ID_KEY, mediaId)
                    startActivity(intent)
                }
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpWatchProviders() {
        binding.countryTv.text = "($countryName)"
        val property = ProvidingCountries::class.memberProperties.find {
            it.name == countryKey
        }
        if (tvDetails.watchProviders?.results == null) {
            binding.cl3.visibility = View.GONE
        }
        tvDetails.watchProviders?.results?.let {
            val res = property?.get(it)
            if (res != null) {
                getProviderCountryAndValidate(res as Providers)
            } else {
                if (tvDetails.watchProviders?.results?.US == null) {
                    binding.cl3.visibility = View.GONE
                } else {
                    binding.cl3.visibility = View.VISIBLE
                }
                tvDetails.watchProviders?.results?.US?.let { US ->
                    binding.countryTv.text = "(United States of America)"
                    getProviderCountryAndValidate(US)
                }
            }
        }
    }

    private fun getProviderCountryAndValidate(provider: Providers) {
        if ((provider.buy.isNullOrEmpty() && provider.rent.isNullOrEmpty() && provider.flatrate.isNullOrEmpty())) {
            binding.cl3.visibility = View.GONE
        } else {
            setUpWpList(provider.buy, binding.buyWpRv, binding.buyWpCl)
            setUpWpList(provider.rent, binding.rentWpRv, binding.rentWpCl)
            setUpWpList(provider.flatrate, binding.streamWpRv, binding.streamWpCl)
        }
    }

    private fun setUpWpList(providerList: ArrayList<Provider?>?, wpRv: RecyclerView, wpCl: ConstraintLayout) {
        if (providerList.isNullOrEmpty()) {
            wpCl.visibility = View.GONE
        }
        wpRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        wpRv.adapter = WatchProviderRecyclerAdapter(providerList) { logoPath, view ->
            loadAndSetImageUsingGlide(logoPath, null, view)
        }
    }

    private fun setUpLandingPages() {
        if (tvDetails.tvShow?.homepage.isNullOrEmpty()) {
            binding.homepageBtn.visibility = View.GONE
        }
        if (tvDetails.tvShow?.external_ids?.imdb_id.isNullOrEmpty()) {
            binding.imdbBtn.visibility = View.GONE
        }
        if (tvDetails.tvShow?.external_ids?.facebook_id.isNullOrEmpty()) {
            binding.facebookBtn.visibility = View.GONE
        }
        if (tvDetails.tvShow?.external_ids?.instagram_id.isNullOrEmpty()) {
            binding.instagramBtn.visibility = View.GONE
        }
        if (tvDetails.tvShow?.external_ids?.twitter_id.isNullOrEmpty()) {
            binding.twitterBtn.visibility = View.GONE
        }
        if (tvDetails.tvShow?.external_ids?.wikidata_id.isNullOrEmpty()) {
            binding.wikipediaBtn.visibility = View.GONE
        }
    }

    private fun setUpGenres() {
        if (tvDetails.tvShow?.genres.isNullOrEmpty()) {
            binding.genresRv.visibility = View.GONE
            binding.genresLayout.visibility = View.GONE
        } else {
            tvDetails.tvShow?.genres?.let {
                val spanCount = if (it.size <= 2) {
                    1
                } else if (it.size <= 6) {
                    2
                } else {
                    3
                }
                binding.genresRv.layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL)
                binding.genresRv.adapter = GenresRecyclerAdapter(it)
            }
        }
    }

    private fun setUpUiTexts() {
        if (tvDetails.tvShow?.overview.isNullOrEmpty()) {
            binding.ll3.visibility = View.GONE
        } else {
            binding.overviewTv.text = tvDetails.tvShow?.overview
        }
        tvDetails.tvShow?.original_name?.let {
            binding.originalNameTv.text = it
        }
        tvDetails.tvShow?.status?.let {
            binding.statusTv.text = it
        }
        tvDetails.tvShow?.original_language?.let { originalLangKey ->
            tvDetails.tvShow?.spoken_languages?.let { spokenLangList ->
                for (lang in spokenLangList) {
                    if (lang?.iso_639_1 == originalLangKey) {
                        binding.originalLanguageTv.text = lang.english_name
                        break
                    }
                }
            }
        }
        tvDetails.tvShow?.type?.let {
            binding.typeTv.text = it
        }
        tvDetails.tvShow?.number_of_seasons?.let {
            binding.noOfSeasonsTv.text = it.toString()
        }
        tvDetails.tvShow?.number_of_episodes?.let {
            binding.noOfEpisodesTv.text = it.toString()
        }
        tvDetails.tvShow?.name?.let {
            binding.tvNameTv.text = it
        }
        tvDetails.tvShow?.first_air_date?.let {
            binding.firstAirDateTv.text = formatDate(it)
        }
        tvDetails.tvShow?.vote_average?.let {
            val rating = roundToOneDecimalPlace(it)
            binding.ratingTextView.text = rating.toString()
            binding.ratingView.setProgress(rating)
        }
        tvDetails.tvShow?.vote_count?.let {
            binding.voteCountTv.text = it.toString()
        }
        tvDetails.tvShow?.tagline?.let {
            binding.taglineTv.text = it
        }
        tvDetails.tvShow?.production_countries?.let {
            if (it.isNotEmpty()) {
                var res = ""
                for (country in it) {
                    res += country?.name + ", "
                }
                binding.productionCountriesTv.text = res.substring(0, res.length - 2)
            }
        }
        tvDetails.tvShow?.production_companies?.let {
            if (it.isNotEmpty()) {
                var res = ""
                for (company in it) {
                    res += company?.name + ", "
                }
                binding.productionCompaniesTv.text = res.substring(0, res.length - 2)
            }
        }
        tvDetails.tvShow?.spoken_languages?.let {
            if (it.isNotEmpty()) {
                var res = ""
                for (language in it) {
                    res += language?.name + ", "
                }
                binding.spokenLanguagesTv.text = res.substring(0, res.length - 2)
            }
        }
    }

    private fun roundToOneDecimalPlace(number: Double): Double {
        return String.format("%.1f", number).toDouble()
    }

    private fun loadAndSetImageUsingGlide(imagePath: String, customDrawable: Int?, imageView: ImageView, imageBaseUrl: String = BuildConfig.IMAGE_URL) {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                if (imagePath.isNotEmpty()) {
                    if (imagePath[0] == '/') {
                        imagePath.removeRange(0, 0)
                    }

                    if (customDrawable == null) {
                        Glide.with(this@TvShowsActivity)
                            .load(imageBaseUrl + imagePath)
                            .centerCrop()
                            .into(imageView)
                    } else {
                        Glide.with(this@TvShowsActivity)
                            .load(imageBaseUrl + imagePath)
                            .centerCrop()
                            .placeholder(customDrawable)
                            .into(imageView)
                    }
                }
            } catch (e: Exception) {
                Log.e("TEST_TAG", "loadAndSetImageUsingGlide: ${e.message}")
            }
        }
    }
}