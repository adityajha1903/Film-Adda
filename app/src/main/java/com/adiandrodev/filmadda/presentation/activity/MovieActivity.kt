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
import com.adiandrodev.filmadda.MyApplication.Companion.ACCOUNT_ID_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.APPLICATION_PREFERENCES
import com.adiandrodev.filmadda.MyApplication.Companion.COLLECTION_ID_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.COUNTRY_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.COUNTRY_NAME
import com.adiandrodev.filmadda.MyApplication.Companion.IMAGE_PATH_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_ID_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_TYPE_MOVIE
import com.adiandrodev.filmadda.MyApplication.Companion.PERSON_ID_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.SESSION_ID_KEY
import com.adiandrodev.filmadda.R
import com.adiandrodev.filmadda.data.model.Provider
import com.adiandrodev.filmadda.data.model.Providers
import com.adiandrodev.filmadda.data.model.ProvidingCountries
import com.adiandrodev.filmadda.data.model.Rated
import com.adiandrodev.filmadda.databinding.ActivityMovieBinding
import com.adiandrodev.filmadda.databinding.RatingDialogBinding
import com.adiandrodev.filmadda.di.Injector
import com.adiandrodev.filmadda.domain.model.MovieDetails
import com.adiandrodev.filmadda.presentation.adapter.*
import com.adiandrodev.filmadda.presentation.viewmodel.MovieViewModel
import com.adiandrodev.filmadda.presentation.viewmodel.MovieViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.reflect.full.memberProperties

class MovieActivity : BaseActivity() {

    private lateinit var binding: ActivityMovieBinding

    @Inject
    lateinit var viewModelFactory: MovieViewModelFactory
    private lateinit var viewModel: MovieViewModel

    private var movieId: Int? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var countryKey: String
    private lateinit var countryName: String
    private var accountId = -1
    private lateinit var sessionId: String

    private lateinit var movieDetails: MovieDetails

    private lateinit var videoAdapter: VideosRecyclerAdapter
    private lateinit var imagesAdapter: ImagesRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie)
        (application as Injector).createMovieSubComponent().inject(this)
        viewModel = viewModelFactory.create(MovieViewModel::class.java)
        movieId = intent.getIntExtra(MEDIA_ID_KEY, -1)

        loadSharedPrefData()

        initAdapter()

        setClickListeners()

        if (super.isNetworkConnected()) {
            getMovieDetailsFromTmdb()
        } else {
            super.showErrorDialog()
        }
    }

    private fun initAdapter() {
        videoAdapter = getVideoAdapter()
        imagesAdapter = getImageAdapter()
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
            intent.putExtra(IMAGE_PATH_KEY, imagePath)
            startActivity(intent)
        }
        )
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

    private fun getMovieDetailsFromTmdb() {
        movieId?.let { id ->
            viewModel.getMovieDetails(id, accountId, sessionId).observe(this) { res ->
                res?.let {
                    movieDetails = it
                    setMainPoster()
                    setBackgroundPoster()
                    setCastRecycler()
                    handleVideoPosterBackdropVisibility()
                    binding.videosAndImagesRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    setUpVideos()
                    if (movieDetails.videos?.results.isNullOrEmpty()) {
                        setUpPosters()
                        binding.rbPosters.isChecked = true
                    } else if (movieDetails.images?.posters.isNullOrEmpty()) {
                        setUpBackdrops()
                        binding.rbBackdrops.isChecked = true
                    }
                    setUpRecommendationList()
                    setUpCollection()
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

    @SuppressLint("SetTextI18n")
    private fun setUpCollection() {
        if (movieDetails.movie?.belongs_to_collection == null) {
            binding.collectionLayout.visibility = View.GONE
        } else {
            movieDetails.movie?.belongs_to_collection?.poster_path?.let {
                loadAndSetImageUsingGlide(it, R.drawable.custom_poster, binding.collectionPosterIv)
                binding.collectionTv.text = "Part of ${movieDetails.movie?.belongs_to_collection?.name}."
            }
        }
    }

    private fun setUpGenres() {
        if (movieDetails.movie?.genres.isNullOrEmpty()) {
            binding.genresRv.visibility = View.GONE
            binding.genresLayout.visibility = View.GONE
        } else {
            movieDetails.movie?.genres?.let {
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

    @SuppressLint("SetTextI18n")
    private fun setUpUiTexts() {
        if (movieDetails.movie?.overview.isNullOrEmpty()) {
            binding.ll3.visibility = View.GONE
        } else {
            binding.overviewTv.text = movieDetails.movie?.overview
        }
        movieDetails.movie?.original_title?.let {
            binding.originalTitleTv.text = it
        }
        movieDetails.movie?.status?.let {
            binding.statusTv.text = it
        }
        movieDetails.movie?.original_language?.let { originalLangKey ->
            movieDetails.movie?.spoken_languages?.let { spokenLangList ->
                for (lang in spokenLangList) {
                    if (lang?.iso_639_1 == originalLangKey) {
                        binding.originalLanguageTv.text = lang.english_name
                        break
                    }
                }
            }
        }
        movieDetails.movie?.title?.let {
            binding.movieTitleTv.text = it
        }
        movieDetails.movie?.release_date?.let {
            if (it.isNotEmpty()) {
                binding.releaseDateTv.text = formatDate(it)
            }
        }
        movieDetails.movie?.runtime?.let {
            if (it != 0) {
                val h = (it / 60).toString()
                val m = (it % 60).toString()
                binding.runtimeTv.text = "${h}h ${m}m"
            }
        }
        movieDetails.movie?.vote_average?.let {
            val rating = roundToOneDecimalPlace(it)
            binding.ratingTextView.text = rating.toString()
            binding.ratingView.setProgress(rating)
        }
        movieDetails.movie?.vote_count?.let {
            binding.voteCountTv.text = it.toString()
        }
        movieDetails.movie?.tagline?.let {
            binding.taglineTv.text = it
        }
        movieDetails.movie?.budget?.let {
            if (it != 0L) {
                binding.budgetTv.text = "$${formatAmount(it)}"
            }
        }
        movieDetails.movie?.revenue?.let {
            if (it != 0L) {
                binding.revenueTv.text = "$${formatAmount(it)}"
            }
        }
        movieDetails.movie?.production_countries?.let {
            if (it.isNotEmpty()) {
                var res = ""
                for (country in it) {
                    res += country?.name + ", "
                }
                binding.productionCountriesTv.text = res.substring(0, res.length - 2)
            }
        }
        movieDetails.movie?.production_companies?.let {
            if (it.isNotEmpty()) {
                var res = ""
                for (company in it) {
                    res += company?.name + ", "
                }
                binding.productionCompaniesTv.text = res.substring(0, res.length - 2)
            }
        }
        movieDetails.movie?.spoken_languages?.let {
            if (it.isNotEmpty()) {
                var res = ""
                for (language in it) {
                    res += language?.name + ", "
                }
                binding.spokenLanguagesTv.text = res.substring(0, res.length - 2)
            }
        }
    }

    private fun formatAmount(amount: Long, locale: Locale = Locale.US): String {
        val numberFormat = NumberFormat.getNumberInstance(locale)
        return numberFormat.format(amount)
    }

    private fun roundToOneDecimalPlace(number: Double): Double {
        return String.format("%.1f", number).toDouble()
    }

    private fun setUpLandingPages() {
        if (movieDetails.movie?.homepage.isNullOrEmpty()) {
            binding.homepageBtn.visibility = View.GONE
        }
        if (movieDetails.movie?.external_ids?.imdb_id.isNullOrEmpty()) {
            binding.imdbBtn.visibility = View.GONE
        }
        if (movieDetails.movie?.external_ids?.facebook_id.isNullOrEmpty()) {
            binding.facebookBtn.visibility = View.GONE
        }
        if (movieDetails.movie?.external_ids?.instagram_id.isNullOrEmpty()) {
            binding.instagramBtn.visibility = View.GONE
        }
        if (movieDetails.movie?.external_ids?.twitter_id.isNullOrEmpty()) {
            binding.twitterBtn.visibility = View.GONE
        }
        if (movieDetails.movie?.external_ids?.wikidata_id.isNullOrEmpty()) {
            binding.wikipediaBtn.visibility = View.GONE
        }
    }

    private fun setUpAccountStates() {
        if (accountId != -1 && sessionId.isNotEmpty()) {
            if (movieDetails.movie?.account_states?.favorite == true) {
                binding.favBtn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.baseline_favorite_24_red, null))
            } else if (movieDetails.movie?.account_states?.favorite == false) {
                binding.favBtn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.baseline_favorite_24_white, null))
            }
            if (movieDetails.movie?.account_states?.watchlist == true) {
                binding.watchlistBtn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.baseline_bookmark_24_green, null))
            } else if (movieDetails.movie?.account_states?.watchlist == false) {
                binding.watchlistBtn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.baseline_bookmark_24_white, null))
            }
            if (movieDetails.movie?.account_states?.rated != null) {
                binding.rateBtn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.baseline_star_rate_24_yellow, null))
            } else {
                binding.rateBtn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.baseline_star_rate_24_white, null))
            }
        } else {
            binding.cl1.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpWatchProviders() {
        binding.countryTv.text = "($countryName)"
        val property = ProvidingCountries::class.memberProperties.find {
            it.name == countryKey
        }
        if (movieDetails.watchProviders?.results == null) {
            binding.cl3.visibility = View.GONE
        }
        movieDetails.watchProviders?.results?.let {
            val res = property?.get(it)
            if (res != null) {
                getProviderCountryAndValidate(res as Providers)
            } else {
                if (movieDetails.watchProviders?.results?.US == null) {
                    binding.cl3.visibility = View.GONE
                } else {
                    binding.cl3.visibility = View.VISIBLE
                }
                movieDetails.watchProviders?.results?.US?.let { US ->
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

    private fun setUpRecommendationList() {
        if (movieDetails.movie?.recommendations?.results.isNullOrEmpty()) {
            binding.recommendationLl.visibility = View.GONE
        } else {
            binding.recommendationRv.adapter = RecommendationRecyclerAdapter(movieDetails.movie?.recommendations,
                {imagePath, view ->
                    loadAndSetImageUsingGlide(imagePath, R.drawable.custom_poster, view)
                }, {mediaId, mediaType ->
                    val intent = when (mediaType) {
                        MEDIA_TYPE_MOVIE -> {
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

    private fun handleVideoPosterBackdropVisibility() {
        if (movieDetails.videos?.results.isNullOrEmpty()
            && movieDetails.images?.posters.isNullOrEmpty()
            && movieDetails.images?.backdrops.isNullOrEmpty()
        ) {
            binding.videosAndImagesLl.visibility = View.GONE
        } else {
            if (movieDetails.videos?.results.isNullOrEmpty()) {
                binding.rbVideos.visibility = View.GONE
            }
            if (movieDetails.images?.posters.isNullOrEmpty()) {
                binding.rbPosters.visibility = View.GONE
            }
            if (movieDetails.images?.backdrops.isNullOrEmpty()) {
                binding.rbBackdrops.visibility = View.GONE
            }
        }
    }

    private fun setUpPosters() {
        binding.videosAndImagesRv.adapter = imagesAdapter
        movieDetails.images?.let {
            (binding.videosAndImagesRv.adapter as ImagesRecyclerAdapter).dataSetChanged(it, true)
        }
    }

    private fun setUpBackdrops() {
        binding.videosAndImagesRv.adapter = imagesAdapter
        movieDetails.images?.let {
            (binding.videosAndImagesRv.adapter as ImagesRecyclerAdapter).dataSetChanged(it, false)
        }
    }

    private fun setUpVideos() {
        binding.videosAndImagesRv.adapter = videoAdapter
        movieDetails.videos?.let {
            (binding.videosAndImagesRv.adapter as VideosRecyclerAdapter).dataSetChanged(it)
        }
    }

    private fun setCastRecycler() {
        if (movieDetails.movie?.credits?.cast.isNullOrEmpty()) {
                binding.castLayout.visibility = View.GONE
        } else {
            binding.castRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            movieDetails.movie?.credits?.cast?.let {
                binding.castRv.adapter = CastRecyclerAdapter(it, { personId ->
                    val intent = Intent(this, PersonActivity::class.java)
                    intent.putExtra(PERSON_ID_KEY, personId)
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

    private fun setMainPoster() {
        val index = if (!movieDetails.images?.posters.isNullOrEmpty()) {
            0
        } else {
            -1
        }
        val path = if (index == -1) {
            movieDetails.movie?.poster_path
        } else {
            movieDetails.images?.posters?.get(0)?.file_path
        }

        loadAndSetImageUsingGlide(
            path?:"",
            R.drawable.custom_poster,
            binding.moviePosterIv
        )
    }

    private fun setBackgroundPoster() {
        val index = if ((movieDetails.images?.posters?.size ?: -1) >= 2) {
            1
        } else {
            -1
        }
        val path = if (index == -1) {
            movieDetails.movie?.backdrop_path?:""
        } else {
            movieDetails.images?.posters?.get(index)?.file_path?:""
        }
        loadAndSetImageUsingGlide(
            path,
            R.drawable.custom_backdrop,
            binding.backgroundPosterIv
        )
    }

    private fun loadSharedPrefData() {
        sharedPreferences = getSharedPreferences(APPLICATION_PREFERENCES, MODE_PRIVATE)
        countryKey = sharedPreferences.getString(COUNTRY_KEY, "US")?:"US"
        countryName = sharedPreferences.getString(COUNTRY_NAME, "United States of America")?:"United States of America"
        accountId = sharedPreferences.getInt(ACCOUNT_ID_KEY, -1)
        sessionId = sharedPreferences.getString(SESSION_ID_KEY, "")?:""
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
            movieDetails.movie?.homepage?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
            }
        }
        binding.imdbBtn.setOnClickListener {
            movieDetails.movie?.external_ids?.imdb_id?.let { id ->
                val imdbUrl = BuildConfig.IMDB_BASE_URL + id
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(imdbUrl))
                startActivity(intent)
            }
        }
        binding.facebookBtn.setOnClickListener {
            movieDetails.movie?.external_ids?.facebook_id?.let { id ->
                val facebookPageUrl = BuildConfig.FACEBOOK_BASE_URL + id
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookPageUrl))
                startActivity(intent)
            }
        }
        binding.instagramBtn.setOnClickListener {
            movieDetails.movie?.external_ids?.instagram_id?.let { id ->
                val instagramPageUrl = BuildConfig.INSTAGRAM_BASE_URL + id
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramPageUrl))
                startActivity(intent)
            }
        }
        binding.twitterBtn.setOnClickListener {
            movieDetails.movie?.external_ids?.twitter_id?.let { id ->
                val twitterPageUrl = BuildConfig.TWITTER_BASE_URL + id
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(twitterPageUrl))
                startActivity(intent)
            }
        }
        binding.wikipediaBtn.setOnClickListener {
            movieDetails.movie?.external_ids?.wikidata_id?.let { id ->
                val wikipediaPageUrl = BuildConfig.WIKIDATA_BASE_URL + id
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(wikipediaPageUrl))
                startActivity(intent)
            }
        }
        binding.collectionBtn.setOnClickListener {
            movieDetails.movie?.belongs_to_collection?.id?.let {
                val intent = Intent(this, CollectionActivity::class.java)
                intent.putExtra(COLLECTION_ID_KEY, it)
                startActivity(intent)
            }
        }
        binding.favBtn.setOnClickListener {
            if (super.isNetworkConnected()) {
                movieDetails.movie?.id?.let { id ->
                    blockActions()
                    if (movieDetails.movie?.account_states?.favorite == true) {
                        viewModel.addOrRemoveFromFavList(accountId, sessionId, false, id).observe(this) { success ->
                            if (success?.success == true) {
                                movieDetails.movie?.account_states?.favorite = false
                                setUpAccountStates()
                            } else {
                                Snackbar.make(binding.root, "Something went wrong, please try again.", Snackbar.LENGTH_SHORT)
                                    .setBackgroundTint(resources.getColor(R.color.background_color_rev, null))
                                    .setTextColor(resources.getColor(R.color.background_color, null))
                                    .show()
                            }
                        }
                    } else if (movieDetails.movie?.account_states?.favorite == false) {
                        viewModel.addOrRemoveFromFavList(accountId, sessionId, true, id).observe(this) { success ->
                            if (success?.success == true) {
                                movieDetails.movie?.account_states?.favorite = true
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
                movieDetails.movie?.id?.let { id ->
                    blockActions()
                    if (movieDetails.movie?.account_states?.watchlist == true) {
                        viewModel.addOrRemoveFromWatchList(accountId, sessionId, false, id).observe(this) { success ->
                            if (success?.success == true) {
                                movieDetails.movie?.account_states?.watchlist = false
                                setUpAccountStates()
                            } else {
                                Snackbar.make(binding.root, "Something went wrong, please try again.", Snackbar.LENGTH_SHORT)
                                    .setBackgroundTint(resources.getColor(R.color.background_color_rev, null))
                                    .setTextColor(resources.getColor(R.color.background_color, null))
                                    .show()
                            }
                        }
                    } else if (movieDetails.movie?.account_states?.watchlist == false) {
                        viewModel.addOrRemoveFromWatchList(accountId, sessionId, true, id).observe(this) { success ->
                            if (success?.success == true) {
                                movieDetails.movie?.account_states?.watchlist = true
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

    private fun showRatingDialog() {
        val dialog = Dialog(this)
        val dialogBinding = RatingDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        movieDetails.movie?.account_states?.rated?.value?.let {
            dialogBinding.ratingBar.rating = it.toFloat()/2
        }
        dialogBinding.clearBtn.setOnClickListener {
            dialogBinding.ratingBar.rating = 0f
        }
        dialogBinding.doneBtn.setOnClickListener {
            val rating = dialogBinding.ratingBar.rating
            if (rating == 0f) {
                movieDetails.movie?.id?.let { id ->
                    viewModel.deleteRating(id, sessionId).observe(this) { success ->
                        if (success?.success == true) {
                            movieDetails.movie?.account_states?.rated = null
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
                movieDetails.movie?.id?.let { id ->
                    viewModel.rateMovie(id, sessionId, (rating * 2).toDouble()).observe(this) { success ->
                        if (success?.success == true) {
                            movieDetails.movie?.account_states?.rated = Rated((rating * 2).toInt())
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

    private fun loadAndSetImageUsingGlide(imagePath: String, customDrawable: Int?, imageView: ImageView, imageBaseUrl: String = BuildConfig.IMAGE_URL) {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                if (imagePath.isNotEmpty()) {
                    if (imagePath[0] == '/') {
                        imagePath.removeRange(0, 0)
                    }

                    if (customDrawable == null) {
                        Glide.with(this@MovieActivity)
                            .load(imageBaseUrl + imagePath)
                            .centerCrop()
                            .into(imageView)
                    } else {
                        Glide.with(this@MovieActivity)
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

    private fun formatDate(inputDate: String?): String {
        return if (inputDate.isNullOrEmpty()) {
            ""
        } else {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val outputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy")
            LocalDate.parse(inputDate, inputFormatter).format(outputFormatter)
        }
    }
}
