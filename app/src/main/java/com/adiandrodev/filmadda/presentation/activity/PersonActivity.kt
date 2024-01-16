package com.adiandrodev.filmadda.presentation.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.adiandrodev.filmadda.BuildConfig
import com.adiandrodev.filmadda.MyApplication
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_TYPE_MOVIE
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_TYPE_TV
import com.adiandrodev.filmadda.MyApplication.Companion.PERSON_ID_KEY
import com.adiandrodev.filmadda.R
import com.adiandrodev.filmadda.data.model.CastOrCrewIn
import com.adiandrodev.filmadda.data.model.People
import com.adiandrodev.filmadda.databinding.ActivityPersonBinding
import com.adiandrodev.filmadda.databinding.CreditsLayoutBinding
import com.adiandrodev.filmadda.di.Injector
import com.adiandrodev.filmadda.presentation.adapter.CreditsRecyclerAdapter
import com.adiandrodev.filmadda.presentation.adapter.ProfileRecyclerAdapter
import com.adiandrodev.filmadda.presentation.viewmodel.PersonViewModel
import com.adiandrodev.filmadda.presentation.viewmodel.PersonViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class PersonActivity : BaseActivity() {

    private lateinit var binding: ActivityPersonBinding

    @Inject
    lateinit var viewModelFactory: PersonViewModelFactory
    private lateinit var viewModel: PersonViewModel

    private var personId = -1
    private lateinit var person: People

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_person)
        (application as Injector).createPersonSubComponent().inject(this)
        viewModel = viewModelFactory.create(PersonViewModel::class.java)
        personId = intent.getIntExtra(PERSON_ID_KEY, -1)

        initAdapters()

        setClickListeners()

        if (super.isNetworkConnected()) {
            loadData()
        } else {
            super.showErrorDialog()
        }

    }

    private fun initAdapters() {
        binding.includedMovieCredit.mediaRv.adapter = getAdapter(MEDIA_TYPE_MOVIE)
        binding.includedMovieCredit.listNameTv.text = LIST_MOVIE
        binding.includedMovieCredit.mediaRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.includedTvCredit.mediaRv.adapter = getAdapter(MEDIA_TYPE_TV)
        binding.includedTvCredit.listNameTv.text = LIST_TV
        binding.includedTvCredit.mediaRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun getAdapter(mType: String): CreditsRecyclerAdapter {
        return CreditsRecyclerAdapter(
            ArrayList(),
            true,
            mType,
            clickListener = { mediaId, mediaType ->
            val intent = when (mediaType) {
                MEDIA_TYPE_MOVIE -> {
                    Intent(this, MovieActivity::class.java)
                }
                else -> Intent(this, TvShowsActivity::class.java)
            }
            intent.putExtra(MyApplication.MEDIA_ID_KEY, mediaId)
            startActivity(intent)
            },
            setImage = { imagePath, view ->
                loadAndSetImageUsingGlide(imagePath, view)
            }
        )
    }

    private fun loadData() {
        if (personId != -1) {
            viewModel.getPersonDetails(personId).observe(this) {
                it?.let { res ->
                    person = res
                    setPersonProfile()
                    setPersonProfileList()
                    setMediaCreditList(person.movie_credits?.cast, true, binding.includedMovieCredit, NO_MOVIE_CAST_CREDITS_WARNING)
                    setMediaCreditList(person.tv_credits?.cast, true, binding.includedTvCredit, NO_TV_CAST_CREDITS_WARNING)
                    setUpLandingPages()
                    setUpUiTexts()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.mainUi.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setUpUiTexts() {
        person.known_for_department?.let {
            if (it.isNotEmpty()) {
                binding.knownForTv.text = it
            }
        }
        person.gender?.let {
            when (it) {
                1 -> binding.genderTv.text = FEMALE
                2 -> binding.genderTv.text = MALE
            }
        }
        person.place_of_birth?.let {
            if (it.isNotEmpty()) {
                binding.placeOfBirthTv.text = it
            }
        }
        person.birthday?.let {
            if (it.isNotEmpty()) {
                binding.birthdayTv.text = formatDate(it)
            }
        }
        person.biography?.let {
            if (it.isNotEmpty()) {
                binding.biographyTv.text = it
            }
        }
    }

    private fun setUpLandingPages() {
        if (person.homepage.isNullOrEmpty()) {
            binding.homepageBtn.visibility = View.GONE
        }
        if (person.external_ids?.imdb_id.isNullOrEmpty()) {
            binding.imdbBtn.visibility = View.GONE
        }
        if (person.external_ids?.facebook_id.isNullOrEmpty()) {
            binding.facebookBtn.visibility = View.GONE
        }
        if (person.external_ids?.instagram_id.isNullOrEmpty()) {
            binding.instagramBtn.visibility = View.GONE
        }
        if (person.external_ids?.twitter_id.isNullOrEmpty()) {
            binding.twitterBtn.visibility = View.GONE
        }
        if (person.external_ids?.wikidata_id.isNullOrEmpty()) {
            binding.wikipediaBtn.visibility = View.GONE
        }
    }

    private fun setMediaCreditList(
        castOrCrewList: ArrayList<CastOrCrewIn?>?,
        isCast: Boolean,
        listBinding: CreditsLayoutBinding,
        emptyListMessage: String
    ) {
        if (castOrCrewList.isNullOrEmpty()) {
            listBinding.movieWarningTv.text = emptyListMessage
            listBinding.movieWarningTv.visibility = View.VISIBLE
            listBinding.mediaRv.visibility = View.GONE
        } else {
            listBinding.movieWarningTv.visibility = View.GONE
            listBinding.mediaRv.visibility = View.VISIBLE
            (listBinding.mediaRv.adapter as CreditsRecyclerAdapter).changeDataSet(castOrCrewList, isCast)
        }
    }

    private fun setPersonProfileList() {
        binding.profilesRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.profilesRv.adapter = ProfileRecyclerAdapter(this, person.images, { imagePath, view ->
            loadAndSetImageUsingGlide(imagePath, view)
        }, { imagePath ->
            val intent = Intent(this, ImageActivity::class.java)
            intent.putExtra(MyApplication.IMAGE_PATH_KEY, imagePath)
            startActivity(intent)
        })
    }

    private fun setPersonProfile() {
        person.profile_path?.let { profilePath ->
            loadAndSetImageUsingGlide(profilePath, binding.personProfileIv)
        }
    }

    private fun setClickListeners() {
        binding.includedAppBar.backBtn.setOnClickListener { finish() }

        binding.homepageBtn.setOnClickListener {
            person.homepage?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
            }
        }
        binding.imdbBtn.setOnClickListener {
            person.external_ids?.imdb_id?.let { id ->
                val imdbUrl = BuildConfig.IMDB_BASE_URL + id
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(imdbUrl))
                startActivity(intent)
            }
        }
        binding.facebookBtn.setOnClickListener {
            person.external_ids?.facebook_id?.let { id ->
                val facebookPageUrl = BuildConfig.FACEBOOK_BASE_URL + id
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookPageUrl))
                startActivity(intent)
            }
        }
        binding.instagramBtn.setOnClickListener {
            person.external_ids?.instagram_id?.let { id ->
                val instagramPageUrl = BuildConfig.INSTAGRAM_BASE_URL + id
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramPageUrl))
                startActivity(intent)
            }
        }
        binding.twitterBtn.setOnClickListener {
            person.external_ids?.twitter_id?.let { id ->
                val twitterPageUrl = BuildConfig.TWITTER_BASE_URL + id
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(twitterPageUrl))
                startActivity(intent)
            }
        }
        binding.wikipediaBtn.setOnClickListener {
            person.external_ids?.wikidata_id?.let { id ->
                val wikipediaPageUrl = BuildConfig.WIKIDATA_BASE_URL + id
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(wikipediaPageUrl))
                startActivity(intent)
            }
        }
        binding.includedMovieCredit.radioGrp.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.cast_rb -> setMediaCreditList(person.movie_credits?.cast, true, binding.includedMovieCredit, NO_MOVIE_CAST_CREDITS_WARNING)
                R.id.crew_rb -> setMediaCreditList(person.movie_credits?.crew, false, binding.includedMovieCredit, NO_MOVIE_CREW_CREDITS_WARNING)
            }
        }
        binding.includedTvCredit.radioGrp.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.cast_rb -> setMediaCreditList(person.tv_credits?.cast, true, binding.includedTvCredit, NO_TV_CAST_CREDITS_WARNING)
                R.id.crew_rb -> setMediaCreditList(person.tv_credits?.crew, false, binding.includedTvCredit, NO_TV_CREW_CREDITS_WARNING)
            }
        }
    }

    private fun loadAndSetImageUsingGlide(imagePath: String, imageView: ImageView, imageBaseUrl: String = BuildConfig.IMAGE_URL) {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                if (imagePath.isNotEmpty()) {
                    if (imagePath[0] == '/') {
                        imagePath.removeRange(0, 0)
                    }

                    Glide.with(this@PersonActivity)
                        .load(imageBaseUrl + imagePath)
                        .centerCrop()
                        .placeholder(R.drawable.custom_poster)
                        .into(imageView)
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

    companion object {
        private const val LIST_MOVIE = "Movies credits"
        private const val LIST_TV = "Tv credits"
        private const val MALE = "Male"
        private const val FEMALE = "Female"
        private const val NO_MOVIE_CAST_CREDITS_WARNING = "This person have never been a part of any movie as its cast"
        private const val NO_MOVIE_CREW_CREDITS_WARNING = "This person have never been a part of any movie as its crew"
        private const val NO_TV_CAST_CREDITS_WARNING = "This person have never been a part of any tv show as its cast"
        private const val NO_TV_CREW_CREDITS_WARNING = "This person have never been a part of any tv show as its crew"
    }
}