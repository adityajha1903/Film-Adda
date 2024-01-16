package com.adiandrodev.filmadda.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.adiandrodev.filmadda.BuildConfig
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_ID_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_TYPE_MOVIE
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_TYPE_TV
import com.adiandrodev.filmadda.MyApplication.Companion.PERSON_ID_KEY
import com.adiandrodev.filmadda.R
import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.model.PeopleList
import com.adiandrodev.filmadda.databinding.FragmentTrendingBinding
import com.adiandrodev.filmadda.databinding.MediaListsBinding
import com.adiandrodev.filmadda.di.Injector
import com.adiandrodev.filmadda.domain.model.Trending
import com.adiandrodev.filmadda.presentation.activity.*
import com.adiandrodev.filmadda.presentation.adapter.IntroPagerAdapter
import com.adiandrodev.filmadda.presentation.adapter.MediaRecyclerAdapter
import com.adiandrodev.filmadda.presentation.adapter.PeopleRecyclerAdapter
import com.adiandrodev.filmadda.presentation.viewmodel.TrendingViewModel
import com.adiandrodev.filmadda.presentation.viewmodel.TrendingViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class TrendingFragment : Fragment() {

    private lateinit var binding: FragmentTrendingBinding

    @Inject
    lateinit var viewModelFactory: TrendingViewModelFactory
    private lateinit var viewModel: TrendingViewModel

    private var firstRunCompleted = false

    private var sliderMovieBackdropPath = ""
    private var sliderTvBackdropPath = ""
    private var sliderPeopleBackdropPath = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_trending, container, false)
        (requireActivity().application as Injector).createTrendingSubComponent().inject(this)
        viewModel = viewModelFactory.create(TrendingViewModel::class.java)

        if (firstRunCompleted) {
            binding.shimmerLayout.visibility = View.GONE
            binding.mainUi.visibility = View.VISIBLE
        } else {
            binding.shimmerLayout.visibility = View.VISIBLE
            binding.mainUi.visibility = View.GONE
        }

        setUpRecyclerViews()

        if ((requireActivity() as BaseActivity).isNetworkConnected()) {
            getTrendingData()
        } else {
            (requireActivity() as BaseActivity).showErrorDialog()
        }

        return binding.root
    }

    private fun setUpRecyclerViews() {
        binding.trendingMovies.mediaPeopleListRv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.trendingTvShows.mediaPeopleListRv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.trendingPeople.mediaPeopleListRv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.trendingMovies.mediaPeopleListRv.adapter = initMediaListAdapter(mediaType = MEDIA_TYPE_MOVIE)
        binding.trendingTvShows.mediaPeopleListRv.adapter = initMediaListAdapter(mediaType = MEDIA_TYPE_TV)
        binding.trendingPeople.mediaPeopleListRv.adapter = PeopleRecyclerAdapter(PeopleList(null, null, null, null), { peopleId ->
            val intent = Intent(activity, PersonActivity::class.java)
            intent.putExtra(PERSON_ID_KEY, peopleId)
            startActivity(intent)
        },{ imagePath, view ->
            loadAndSetImageUsingGlide(imagePath, R.drawable.custom_poster, view)
        })
    }

    private fun getTrendingData() {
        if (TrendingFragmentCacheDataHolder.trendingDataToday == null) {
            viewModel.getTrendingData().observe(viewLifecycleOwner) {
                it?.let {
                    TrendingFragmentCacheDataHolder.trendingDataToday = it
                    displayTrendingData()
                }
            }
        } else {
            displayTrendingData()
        }
    }

    private fun displayTrendingData() {
        TrendingFragmentCacheDataHolder.trendingDataToday?.trendingMovies?.let { list ->
            setMediaLists(TRENDING_MOVIES_NAME, binding.trendingMovies, list, MEDIA_TYPE_MOVIE)
        }
        TrendingFragmentCacheDataHolder.trendingDataToday?.trendingTvShows?.let { list ->
            setMediaLists(TRENDING_TV_NAME, binding.trendingTvShows, list, MEDIA_TYPE_TV)
        }
        TrendingFragmentCacheDataHolder.trendingDataToday?.trendingPeople?.let { people ->
            setPeopleList(binding.trendingPeople, people)
        }

        setUpIntroSlider()
        setClickListeners()
        if (!firstRunCompleted) {
            lifecycleScope.launch(Dispatchers.Main) {
                binding.shimmerLayout.visibility = View.GONE
                binding.mainUi.visibility = View.VISIBLE
                firstRunCompleted = true
            }
        }
    }

    private fun setMediaLists(mediaListName: String, listBinding: MediaListsBinding, mediaList: MediaList, mediaType: String) {
        listBinding.listNameTv.text = mediaListName
        (listBinding.mediaPeopleListRv.adapter as MediaRecyclerAdapter).changeDataList(mediaList, mediaType)
        setMediaBackdrop(listBinding, mediaList, mediaListName)
        if (mediaList.results.isNullOrEmpty()) {
            listBinding.mediaPeopleListRv.visibility = View.GONE
            listBinding.warningTv.visibility = View.VISIBLE
        } else {
            listBinding.mediaPeopleListRv.visibility = View.VISIBLE
            listBinding.warningTv.visibility = View.GONE
        }
    }

    private fun setMediaBackdrop(listBinding: MediaListsBinding, mediaList: MediaList, listName: String) {
        mediaList.results?.size?.let {
            val index1 = getRandomNumber(it - 1, -1)
            loadAndSetImageUsingGlide(
                mediaList.results[index1]?.backdrop_path?:"",
                R.drawable.custom_backdrop,
                listBinding.randomBackdropIv
            )
            val sliderIndex = getRandomNumber(it - 1, index1)
            when(listName) {
                TRENDING_MOVIES_NAME -> sliderMovieBackdropPath = mediaList.results[sliderIndex]?.backdrop_path.toString()
                TRENDING_TV_NAME -> sliderTvBackdropPath = mediaList.results[sliderIndex]?.backdrop_path.toString()
            }
        }
    }

    private fun setPeopleList(listBinding: MediaListsBinding, peopleList: PeopleList) {
        listBinding.listNameTv.text = PEOPLE_LIST_NAME
        (listBinding.mediaPeopleListRv.adapter as PeopleRecyclerAdapter).changeDataList(peopleList)
        setPeopleBackDrop(listBinding, peopleList)
        if (peopleList.results.isNullOrEmpty()) {
            listBinding.mediaPeopleListRv.visibility = View.GONE
            listBinding.warningTv.visibility = View.VISIBLE
        } else {
            listBinding.mediaPeopleListRv.visibility = View.VISIBLE
            listBinding.warningTv.visibility = View.GONE
        }
    }

    private fun setPeopleBackDrop(listBinding: MediaListsBinding, peopleList: PeopleList) {
        peopleList.results?.size?.let {
            val index = getRandomNumber(it - 1, -1)
            loadAndSetImageUsingGlide(
                peopleList.results[index]?.profile_path?:"",
                R.drawable.custom_backdrop,
                listBinding.randomBackdropIv
            )
            val sliderIndex = getRandomNumber(it - 1, index)
            sliderPeopleBackdropPath = peopleList.results[sliderIndex]?.profile_path?:""
        }
    }

    private fun initMediaListAdapter(mediaList: MediaList = MediaList(null, null, null, null), mediaType: String): MediaRecyclerAdapter {
        return MediaRecyclerAdapter(
            mediaList,
            "",
            mediaType,
            false,
            { _, _ , _, _ -> },
            { mediaId, mType ->
                val intent = when (mType) {
                    MEDIA_TYPE_MOVIE -> {
                        Intent(activity, MovieActivity::class.java)
                    }
                    else -> Intent(activity, TvShowsActivity::class.java)
                }
                intent.putExtra(MEDIA_ID_KEY, mediaId)
                startActivity(intent)
            }, { imagePath, view ->
                loadAndSetImageUsingGlide(
                    imagePath,
                    R.drawable.custom_poster,
                    view
                )
            }
        )
    }

    private fun setClickListeners() {
        binding.searchBtn.setOnClickListener { startActivity(Intent(activity, SearchActivity::class.java)) }

        binding.trendingMovies.radioGrp.setOnCheckedChangeListener { _, i ->
            binding.progressBar.visibility = View.VISIBLE
            radioGrpCheckedChanged(i, TRENDING_MOVIES_NAME, binding.trendingMovies)
        }

        binding.trendingTvShows.radioGrp.setOnCheckedChangeListener { _, i ->
            binding.progressBar.visibility = View.VISIBLE
            radioGrpCheckedChanged(i, TRENDING_TV_NAME, binding.trendingTvShows)
        }

        binding.trendingPeople.radioGrp.setOnCheckedChangeListener { _, i ->
            binding.progressBar.visibility = View.VISIBLE
            radioGrpCheckedChanged(i, PEOPLE_LIST_NAME, binding.trendingPeople)
        }
    }

    private fun radioGrpCheckedChanged(rbId: Int, listName: String, listBinding: MediaListsBinding) {
        when (listName) {
            TRENDING_TV_NAME, TRENDING_MOVIES_NAME -> {
                getTrendingMediaWithWindow(rbId, listName, listBinding)
            }
            PEOPLE_LIST_NAME -> {
                getTrendingPeopleWithWindow(rbId, listBinding)
            }
        }
    }

    private fun getTrendingPeopleWithWindow(windowId: Int, listBinding: MediaListsBinding) {
        val window = if (windowId == R.id.today_rb) { "day" } else { "week" }
        if (window == "day") {
            TrendingFragmentCacheDataHolder.trendingDataToday?.trendingPeople?.let {
                setPeopleList(listBinding, it)
            }
        } else {
            val list = TrendingFragmentCacheDataHolder.trendingPeopleThisWeak
            if (list != null) {
                setPeopleList(listBinding, list)
            } else {
                viewModel.getTrendingPeople(window).observe(viewLifecycleOwner) {
                    it?.let {
                        TrendingFragmentCacheDataHolder.trendingPeopleThisWeak = it
                        setPeopleList(listBinding, it)
                    }
                }
            }
        }
        binding.progressBar.visibility = View.GONE
    }

    private fun getTrendingMediaWithWindow(windowId: Int, listName: String, listBinding: MediaListsBinding) {
        val window = if (windowId == R.id.today_rb) { "day" } else { "week" }
        when(listName) {
            TRENDING_MOVIES_NAME -> {
                if (window == "day") {
                    TrendingFragmentCacheDataHolder.trendingDataToday?.trendingMovies?.let {
                        setMediaLists(listName, listBinding, it, MEDIA_TYPE_MOVIE)
                    }
                } else {
                    val list = TrendingFragmentCacheDataHolder.trendingMoviesThisWeak
                    if (list != null) {
                        setMediaLists(listName, listBinding, list, MEDIA_TYPE_MOVIE)
                    } else {
                        viewModel.getTrendingMovies(window).observe(viewLifecycleOwner) {
                            it?.let {
                                TrendingFragmentCacheDataHolder.trendingMoviesThisWeak = it
                                setMediaLists(listName, listBinding, it, MEDIA_TYPE_MOVIE)
                            }
                        }
                    }
                }
            }
            TRENDING_TV_NAME -> {
                if (window == "day") {
                    TrendingFragmentCacheDataHolder.trendingDataToday?.trendingTvShows?.let {
                        setMediaLists(listName, listBinding, it, MEDIA_TYPE_TV)
                    }
                } else {
                    val list = TrendingFragmentCacheDataHolder.trendingTvShowsThisWeak
                    if (list != null) {
                        setMediaLists(listName, listBinding, list, MEDIA_TYPE_TV)
                    } else {
                        viewModel.getTrendingTvShows(window).observe(viewLifecycleOwner) {
                            it?.let {
                                TrendingFragmentCacheDataHolder.trendingTvShowsThisWeak = it
                                setMediaLists(listName, listBinding, it, MEDIA_TYPE_TV)
                            }
                        }
                    }
                }
            }
        }
        binding.progressBar.visibility = View.GONE
    }

    private fun setUpIntroSlider() {
        val sfm = childFragmentManager
        val context = activity
        if (context != null) {
            binding.introViewPager.adapter = IntroPagerAdapter(context, sfm, lifecycle, sliderMovieBackdropPath, sliderTvBackdropPath, sliderPeopleBackdropPath)
        }
        TabLayoutMediator(binding.introTabLayout, binding.introViewPager) { _, _ ->}.attach()
    }

    private fun getRandomNumber(n: Int, exclude: Int): Int {
        require(n >= 0) { "n must be a non-negative integer" }
        val res = Random.nextInt(n + 1)
        if (res == exclude) {
            return getRandomNumber(n, exclude)
        }
        return res
    }

    companion object {
        private const val TRENDING_MOVIES_NAME = "Trending Movies"
        private const val TRENDING_TV_NAME = "Trending Tv"
        private const val PEOPLE_LIST_NAME = "Trending People"
    }

    private fun loadAndSetImageUsingGlide(imagePath: String, customDrawable: Int, imageView: ImageView, imageBaseUrl: String = BuildConfig.IMAGE_URL) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            try {
                if (imagePath.isNotEmpty()) {
                    if (imagePath[0] == '/') {
                        imagePath.removeRange(0, 0)
                    }
                    Glide.with(this@TrendingFragment)
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

object TrendingFragmentCacheDataHolder {
    var trendingDataToday: Trending? = null
    var trendingMoviesThisWeak: MediaList? = null
    var trendingTvShowsThisWeak: MediaList? = null
    var trendingPeopleThisWeak: PeopleList? = null
}