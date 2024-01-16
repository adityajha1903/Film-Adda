package com.adiandrodev.filmadda.presentation.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.adiandrodev.filmadda.BuildConfig
import com.adiandrodev.filmadda.MyApplication
import com.adiandrodev.filmadda.MyApplication.Companion.ACCOUNT_ID_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.AVATAR_PATH_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.COUNTRY_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.COUNTRY_NAME
import com.adiandrodev.filmadda.MyApplication.Companion.GRAVATAR_HASH_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_TYPE_MOVIE
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_TYPE_TV
import com.adiandrodev.filmadda.MyApplication.Companion.SESSION_ID_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.USERNAME_KEY
import com.adiandrodev.filmadda.R
import com.adiandrodev.filmadda.data.model.*
import com.adiandrodev.filmadda.databinding.ActivityProfileBinding
import com.adiandrodev.filmadda.databinding.MediaListsBinding
import com.adiandrodev.filmadda.di.Injector
import com.adiandrodev.filmadda.domain.model.CombinedAccountStates
import com.adiandrodev.filmadda.presentation.adapter.MediaRecyclerAdapter
import com.adiandrodev.filmadda.presentation.viewmodel.ProfileViewModel
import com.adiandrodev.filmadda.presentation.viewmodel.ProfileViewModelFactory
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.random.Random

class ProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityProfileBinding

    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory
    private lateinit var viewModel: ProfileViewModel

    private lateinit var sharedPreference: SharedPreferences

    private var sessionId: String = ""
    private var accountDetails = AccountDetails(Avatar(Gravatar(null), Tmdb(null)), null, null, null, null)

    private lateinit var accStates: CombinedAccountStates

    private val emptyMediaList = MediaList(null, null, null, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        (application as Injector).createProfileSubComponent().inject(this)
        viewModel = viewModelFactory.create(ProfileViewModel::class.java)

        loadSharedPrefData()

        setUiDetails()

        initAdapters()

        checkLogInStatus()

        setClickListeners()
    }

    private fun initAdapters() {
        lifecycleScope.launch(Dispatchers.Default) {
            val mediaFavRvAdapter = getEmptyAdapter(getString(R.string.favourite), binding.favouriteMedia)
            binding.favouriteMedia.mediaPeopleListRv.adapter = mediaFavRvAdapter
            binding.favouriteMedia.mediaPeopleListRv.layoutManager = LinearLayoutManager(this@ProfileActivity, LinearLayoutManager.HORIZONTAL, false)
            val mediaWatchListRvAdapter = getEmptyAdapter(getString(R.string.watchlist),binding.watchlistMedia)
            binding.watchlistMedia.mediaPeopleListRv.adapter = mediaWatchListRvAdapter
            binding.watchlistMedia.mediaPeopleListRv.layoutManager = LinearLayoutManager(this@ProfileActivity, LinearLayoutManager.HORIZONTAL, false)
            val mediaRatedRvAdapter = getEmptyAdapter(getString(R.string.rated), binding.ratedMedia)
            binding.ratedMedia.mediaPeopleListRv.adapter = mediaRatedRvAdapter
            binding.ratedMedia.mediaPeopleListRv.layoutManager = LinearLayoutManager(this@ProfileActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun getEmptyAdapter(listName: String, listBinding: MediaListsBinding): MediaRecyclerAdapter {
        return MediaRecyclerAdapter(
            emptyMediaList,
            listName,
            MEDIA_TYPE_MOVIE,
            true,
            { mediaId, mType, position, listType ->
                if (super.isNetworkConnected()) {
                    when (mType) {
                        MEDIA_TYPE_MOVIE -> {
                            when (listType) {
                                getString(R.string.favourite) -> {
                                    viewModel.removeMovieFromFav(accountDetails.id ?: -1, sessionId, mediaId).observe(this) {
                                        it?.success?.let { done ->
                                            if (done) {
                                                accStates.favouriteMovies?.results?.removeAt(position)
                                                setUpListData(getString(R.string.favourite), MEDIA_TYPE_MOVIE, accStates.favouriteMovies?: emptyMediaList, listBinding)
                                            }
                                        }
                                    }
                                }
                                getString(R.string.watchlist) -> {
                                    viewModel.removeMovieFromWatchlist(accountDetails.id ?: -1, sessionId, mediaId).observe(this) {
                                        it?.success?.let { done ->
                                            if (done) {
                                                accStates.watchlistMovies?.results?.removeAt(position)
                                                setUpListData(getString(R.string.watchlist), MEDIA_TYPE_MOVIE, accStates.watchlistMovies?: emptyMediaList, listBinding)
                                            }
                                        }
                                    }
                                }
                                else -> {
                                    viewModel.removeRatingFromMovie(mediaId, sessionId).observe(this) {
                                        it?.success?.let { done ->
                                            if (done) {
                                                accStates.ratedMovies?.results?.removeAt(position)
                                                setUpListData(getString(R.string.rated), MEDIA_TYPE_MOVIE, accStates.ratedMovies?: emptyMediaList, listBinding)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else -> {
                            when (listType) {
                                getString(R.string.favourite) -> {
                                    viewModel.removeTvShowFromFav(accountDetails.id ?: -1, sessionId, mediaId).observe(this) {
                                        it?.success?.let { done ->
                                            if (done) {
                                                accStates.favouriteTvShows?.results?.removeAt(position)
                                                setUpListData(getString(R.string.favourite), MEDIA_TYPE_TV, accStates.favouriteTvShows?: emptyMediaList, listBinding)
                                            }
                                        }
                                    }
                                }
                                getString(R.string.watchlist) -> {
                                    viewModel.removeTvShowFromWatchlist(accountDetails.id ?: -1, sessionId, mediaId).observe(this) {
                                        it?.success?.let { done ->
                                            if (done) {
                                                accStates.watchlistTvShows?.results?.removeAt(position)
                                                setUpListData(getString(R.string.watchlist), MEDIA_TYPE_TV, accStates.watchlistTvShows?: emptyMediaList, listBinding)
                                            }
                                        }
                                    }
                                }
                                else -> {
                                    viewModel.removeRatingFromTvShow(mediaId, sessionId).observe(this) {
                                        it?.success?.let { done ->
                                            if (done) {
                                                accStates.ratedTvShows?.results?.removeAt(position)
                                                setUpListData(getString(R.string.rated), MEDIA_TYPE_TV, accStates.ratedTvShows?: emptyMediaList, listBinding)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    super.showErrorDialog()
                }
            },{ mediaId, mType ->
                val intent = when (mType) {
                    MEDIA_TYPE_MOVIE -> {
                        Intent(this, MovieActivity::class.java)
                    }
                    else -> {
                        Intent(this, TvShowsActivity::class.java)
                    }
                }
                intent.putExtra(MyApplication.MEDIA_ID_KEY, mediaId)
                startActivity(intent)
            },{ imagePath, view ->
                loadAndSetImageUsingGlide(
                    imagePath,
                    R.drawable.custom_poster,
                    view
                )
            })
    }

    private fun setUiDetails() {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.includedAppBar.activityTitle.text = getString(R.string.profile)
            binding.usernameTv.text = accountDetails.username ?: ""
            binding.favouriteMedia.listNameTv.text = getString(R.string.favourite)
            binding.favouriteMedia.todayRb.text = MEDIA_TYPE_MOVIE
            binding.favouriteMedia.thisWeakRb.text = MEDIA_TYPE_TV
            binding.watchlistMedia.listNameTv.text = getString(R.string.watchlist)
            binding.watchlistMedia.todayRb.text = MEDIA_TYPE_MOVIE
            binding.watchlistMedia.thisWeakRb.text = MEDIA_TYPE_TV
            binding.ratedMedia.listNameTv.text = getString(R.string.rated)
            binding.ratedMedia.todayRb.text = MEDIA_TYPE_MOVIE
            binding.ratedMedia.thisWeakRb.text = MEDIA_TYPE_TV
            binding.favouriteMedia.warningTv.visibility = View.GONE
            binding.watchlistMedia.warningTv.visibility = View.GONE
            binding.ratedMedia.warningTv.visibility = View.GONE
            setUserImage()
        }
    }

    private fun setUserImage() {
        val avatarPath = accountDetails.avatar?.tmdb?.avatar_path
        val gravatarHash = accountDetails.avatar?.gravatar?.hash
        if (!avatarPath.isNullOrEmpty()) {
            loadAndSetImageUsingGlide(
                avatarPath,
                R.drawable.custom_profile,
                binding.profileIv,
            )
        } else if (!gravatarHash.isNullOrEmpty()) {
            loadAndSetImageUsingGlide(
                gravatarHash,
                R.drawable.custom_profile,
                binding.profileIv,
                BuildConfig.GRAVATAR_BASE_URL
            )
        }
    }

    private fun loadSharedPrefData() {
        sharedPreference = getSharedPreferences(MyApplication.APPLICATION_PREFERENCES, Context.MODE_PRIVATE)
        accountDetails.id = sharedPreference.getInt(ACCOUNT_ID_KEY, -1)
        sessionId = sharedPreference.getString(SESSION_ID_KEY, "") ?: ""
        accountDetails.username = sharedPreference.getString(USERNAME_KEY, "") ?: ""
        accountDetails.iso_3166_1 = sharedPreference.getString(COUNTRY_KEY, "US") ?: "US"
        accountDetails.avatar?.gravatar?.hash = sharedPreference.getString(GRAVATAR_HASH_KEY, "") ?: ""
        accountDetails.avatar?.tmdb?.avatar_path = sharedPreference.getString(AVATAR_PATH_KEY, "") ?: ""
    }

    private fun setClickListeners() {
        binding.includedAppBar.backBtn.setOnClickListener { finish() }
        binding.logInOrSignUpButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            if (super.isNetworkConnected()) {
                getRequestToken()
            } else {
                super.showErrorDialog()
            }
        }
        binding.favouriteMedia.radioGrp.setOnCheckedChangeListener { _, i ->
            radioButtonSwitchHandler(i, getString(R.string.favourite), binding.favouriteMedia, accStates.favouriteMovies?: emptyMediaList, accStates.favouriteTvShows ?: emptyMediaList)
        }
        binding.watchlistMedia.radioGrp.setOnCheckedChangeListener { _, i ->
            radioButtonSwitchHandler(i, getString(R.string.watchlist), binding.watchlistMedia, accStates.watchlistMovies?: emptyMediaList, accStates.watchlistTvShows ?: emptyMediaList)
        }
        binding.ratedMedia.radioGrp.setOnCheckedChangeListener { _, i ->
            radioButtonSwitchHandler(i, getString(R.string.rated), binding.ratedMedia, accStates.ratedMovies?: emptyMediaList, accStates.ratedTvShows ?: emptyMediaList)
        }
        binding.logOutButton.setOnClickListener {
            if (super.isNetworkConnected()) {
                showAlertDialog()
            } else {
                super.showErrorDialog()
            }
        }
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Log out!")
        alertDialogBuilder.setMessage("Are you sure you want to log out?")
        alertDialogBuilder.setPositiveButton("Yes") { dialog, _ ->
            logOut()
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.progress_10, null))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.progress_1, null))
        }
        alertDialog.show()
    }

    private fun logOut() {
        viewModel.deleteSession(sessionId).observe(this) {
            Log.i("TEST_TAG", "logOut: ${it?.success}")
            if (it?.success == true) {
                binding.logInSv.visibility = View.VISIBLE
                binding.shimmerLayout.visibility = View.GONE
                binding.profileStatesData.visibility = View.GONE
                sharedPreference.edit()
                    .putString(SESSION_ID_KEY, "")
                    .putInt(ACCOUNT_ID_KEY, -1)
                    .putString(USERNAME_KEY, "")
                    .putString(GRAVATAR_HASH_KEY, "")
                    .putString(AVATAR_PATH_KEY, "")
                    .apply()
                sessionId = ""
                accountDetails = AccountDetails(Avatar(Gravatar(null), Tmdb(null)), null, null, null, null)
            }
        }
    }

    private fun radioButtonSwitchHandler(id: Int, listName: String, listBinding: MediaListsBinding, moviesList: MediaList, tvShowsList: MediaList) {
        when (id) {
            R.id.today_rb -> setUpListData(listName, MEDIA_TYPE_MOVIE, moviesList, listBinding)
            else -> setUpListData(listName, MEDIA_TYPE_TV, tvShowsList, listBinding)
        }
    }

    private fun getRequestToken() {
        viewModel.getRequestToken().observe(this) {
            if (it?.request_token.isNullOrEmpty()) {
                Toast.makeText(this, "Failed to generate request token, Check your Internet connection.", Toast.LENGTH_SHORT).show()
            }
            it?.request_token?.let { requestToken ->
                binding.progressBar.visibility = View.GONE
                getReqTokenApproved(requestToken)
            }
        }
    }

    private fun getReqTokenApproved(requestToken: String) {
        val authUrl = BuildConfig.AUTH_URL + requestToken + "?redirect_to=com.adiandrodev.filmadda://approved"
        try {
            val intent = CustomTabsIntent.Builder().build()
            intent.launchUrl(this, Uri.parse(authUrl))
        } catch (e: Exception) {
            Toast.makeText(this, "Please Check your Internet connection.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        val uri = intent.data
        if (uri != null && uri.scheme == "com.adiandrodev.filmadda") {
            val approvedToken = uri.getQueryParameter("request_token")
            Log.i("TEST_TAG", "approvedToken = ${approvedToken.toString()}")
            binding.logInSv.visibility = View.GONE
            binding.shimmerLayout.visibility = View.VISIBLE
            approvedToken?.let {
                if (super.isNetworkConnected()) {
                    createSession(it)
                } else {
                    super.showErrorDialog()
                }
            }
        }
    }

    private fun createSession(approvedToken: String) {
        viewModel.createSession(approvedToken).observe(this) {
            if (it?.success == true) {
                it.session_id?.let { id ->
                    Log.i("TEST_TAG", "sessionId = $id")
                    sessionId = id
                    if (super.isNetworkConnected()) {
                        loadUserDataFromDatabase(sessionId)
                    } else {
                        super.showErrorDialog()
                    }
                }
            } else {
                Log.e("TEST_TAG", it?.status_message.toString())
            }
        }
    }

    private fun loadUserDataFromDatabase(id: String) {
        viewModel.getAccountDetails(id).observe(this) { accDetails ->
            accDetails?.let {
                saveUserDataInPref(it)
            }
        }
    }

    private fun saveUserDataInPref(accDetails: AccountDetails) {
        accountDetails.id = accDetails.id ?: -1
        accountDetails.iso_3166_1 = accDetails.iso_3166_1 ?: "US"
        accountDetails.username = accDetails.username ?: ""
        accountDetails.avatar?.gravatar?.hash = accDetails.avatar?.gravatar?.hash ?: ""
        accountDetails.avatar?.tmdb?.avatar_path = accDetails.avatar?.tmdb?.avatar_path ?: ""
        sharedPreference.edit()
            .putString(SESSION_ID_KEY, sessionId)
            .putInt(ACCOUNT_ID_KEY, accountDetails.id ?: -1)
            .putString(COUNTRY_KEY, accountDetails.iso_3166_1)
            .putString(USERNAME_KEY, accountDetails.username)
            .putString(GRAVATAR_HASH_KEY, accountDetails.avatar?.gravatar?.hash)
            .putString(AVATAR_PATH_KEY, accountDetails.avatar?.tmdb?.avatar_path)
            .apply()
        if (super.isNetworkConnected()) {
            saveCountryName()
        } else {
            super.showErrorDialog()
        }
        setUiDetails()
        if (super.isNetworkConnected()) {
            loadAccountStatesData()
        } else {
            super.showErrorDialog()
        }
    }

    private fun saveCountryName() {
        lifecycleScope.launch {
            viewModel.getAllAvailableCountries().observe(this@ProfileActivity) {
                for (country in it) {
                    if (country.iso_3166_1 == accountDetails.iso_3166_1) {
                        sharedPreference.edit().putString(COUNTRY_NAME, country.english_name).apply()
                        break
                    }
                }
            }
        }
    }

    private fun checkLogInStatus() {
        if (sessionId.isNotEmpty() && accountDetails.id != -1) {
            binding.logInSv.visibility = View.GONE
            binding.shimmerLayout.visibility = View.VISIBLE
            if (super.isNetworkConnected()) {
                loadAccountStatesData()
            } else {
                super.showErrorDialog()
            }
        }
    }

    private fun loadAccountStatesData() {
        viewModel.getAllAccountStates(accountDetails.id ?: -1, sessionId).observe(this) {
            it?.let { data ->
                accStates = data
                displayAccountStatesDataOnUi()
            }
        }
    }

    private fun displayAccountStatesDataOnUi() {
        setUpListData(getString(R.string.favourite), MEDIA_TYPE_MOVIE, accStates.favouriteMovies ?: emptyMediaList, binding.favouriteMedia)
        setUpListData(getString(R.string.watchlist), MEDIA_TYPE_MOVIE, accStates.watchlistMovies ?: emptyMediaList, binding.watchlistMedia)
        setUpListData(getString(R.string.rated), MEDIA_TYPE_MOVIE, accStates.ratedMovies ?: emptyMediaList, binding.ratedMedia)
        binding.shimmerLayout.visibility = View.GONE
        binding.profileStatesData.visibility = View.VISIBLE
    }

    private fun setUpListData(
        listName: String,
        activeMedia: String,
        mediaList: MediaList,
        listBinding: MediaListsBinding
    ) {
        setEmptyWarningString(listName, activeMedia, listBinding)
        listBinding.mediaPeopleListRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        if (mediaList.results.isNullOrEmpty()) {
            listBinding.mediaPeopleListRv.visibility = View.GONE
            listBinding.emptyListWarningTv.visibility = View.VISIBLE
            listBinding.randomBackdropIv.visibility = View.GONE
        } else {
            listBinding.mediaPeopleListRv.visibility = View.VISIBLE
            listBinding.emptyListWarningTv.visibility = View.GONE
            listBinding.randomBackdropIv.visibility = View.VISIBLE
            (listBinding.mediaPeopleListRv.adapter as MediaRecyclerAdapter).changeDataList(mediaList, activeMedia)
            setMediaBackdrop(listBinding, mediaList)
        }
    }

    private fun setEmptyWarningString(listName: String, activeMedia: String, listBinding: MediaListsBinding) {
        lifecycleScope.launch(Dispatchers.Main) {
            listBinding.emptyListWarningTv.text = when (activeMedia) {
                MEDIA_TYPE_MOVIE -> {
                    when (listName) {
                        getString(R.string.favourite) -> getString(R.string.warning_no_fav_movies)
                        getString(R.string.watchlist) -> getString(R.string.warning_no_watchlist_movies)
                        else -> getString(R.string.warning_no_rated_movies)
                    }
                }
                else -> {
                    when (listName) {
                        getString(R.string.favourite) -> getString(R.string.warning_no_fav_tvShows)
                        getString(R.string.watchlist) -> getString(R.string.warning_no_watchlist_tvShows)
                        else -> getString(R.string.warning_no_rated_tvShows)
                    }
                }
            }
        }
    }

    private fun setMediaBackdrop(listBinding: MediaListsBinding, mediaList: MediaList) {
        mediaList.results?.size?.let {
            val index = getRandomNumber(it - 1)
            val imagePath = mediaList.results[index]?.backdrop_path
            loadAndSetImageUsingGlide(
                imagePath?:"",
                R.drawable.custom_backdrop,
                listBinding.randomBackdropIv
            )
        }
    }

    private fun getRandomNumber(n: Int): Int {
        require(n >= 0) { "n must be a non-negative integer" }
        return Random.nextInt(n + 1)
    }

    private fun loadAndSetImageUsingGlide(imagePath: String, customDrawable: Int, imageView: ImageView, imageBaseUrl: String = BuildConfig.IMAGE_URL) {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                if (imagePath.isNotEmpty()) {
                    if (imagePath[0] == '/') {
                        imagePath.removeRange(0, 0)
                    }
                    Glide.with(this@ProfileActivity)
                        .load(imageBaseUrl + imagePath)
                        .centerCrop()
                        .placeholder(customDrawable)
                        .into(imageView)
                }
            } catch (e: java.lang.Exception) {
                Log.e("TEST_TAG", "loadAndSetImageUsingGlide: ${e.message}")
            }
        }
    }
}