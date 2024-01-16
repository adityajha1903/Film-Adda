package com.adiandrodev.filmadda.presentation.fragment

import android.content.Context
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
import com.adiandrodev.filmadda.MyApplication
import com.adiandrodev.filmadda.MyApplication.Companion.COUNTRY_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_ID_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_TYPE_MOVIE
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_TYPE_TV
import com.adiandrodev.filmadda.R
import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.databinding.FragmentMediaListsBinding
import com.adiandrodev.filmadda.databinding.MediaListsBinding
import com.adiandrodev.filmadda.di.Injector
import com.adiandrodev.filmadda.domain.model.MoviesLists
import com.adiandrodev.filmadda.domain.model.TvShowsLists
import com.adiandrodev.filmadda.presentation.activity.BaseActivity
import com.adiandrodev.filmadda.presentation.activity.MovieActivity
import com.adiandrodev.filmadda.presentation.activity.TvShowsActivity
import com.adiandrodev.filmadda.presentation.adapter.MediaRecyclerAdapter
import com.adiandrodev.filmadda.presentation.viewmodel.MediaListsViewModel
import com.adiandrodev.filmadda.presentation.viewmodel.MediaListsViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class MediaListsFragment : Fragment() {

    private var fragmentName: String? = null

    @Inject
    lateinit var viewModelFactory: MediaListsViewModelFactory
    private lateinit var viewModel: MediaListsViewModel

    private lateinit var binding: FragmentMediaListsBinding

    private var region: String = "US"

    private lateinit var moviesLists: MoviesLists
    private lateinit var tvShowsLists: TvShowsLists

    private lateinit var popularListAdapter: MediaRecyclerAdapter
    private lateinit var airingTodayOrNowPlayingListAdapter: MediaRecyclerAdapter
    private lateinit var onTheAirOrUpcomingListAdapter: MediaRecyclerAdapter
    private lateinit var topRatedListAdapter: MediaRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fragmentName = it.getString(FRAGMENT_NAME_PARAM)
        }
        requireActivity().getSharedPreferences(MyApplication.APPLICATION_PREFERENCES, Context.MODE_PRIVATE).getString(COUNTRY_KEY, "US")?.let {
            region = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_media_lists, container, false)
        (requireActivity().application as Injector).createMediaListsSubComponent().inject(this)
        viewModel = viewModelFactory.create(MediaListsViewModel::class.java)

        setListsNames()

        if ((requireActivity() as BaseActivity).isNetworkConnected()) {
            setActivityStates()
        } else {
            (requireActivity() as BaseActivity).showErrorDialog()
        }

        return binding.root
    }

    private fun setActivityStates() {
        when (fragmentName) {
            FRAGMENT_MOVIES_LISTS -> {
                viewModel.getMoviesLists(region).observe(viewLifecycleOwner) { list ->
                    list?.let {
                        moviesLists = it
                        moviesLists.popular?.let { popularList ->
                            popularListAdapter = getAdapter(popularList, MEDIA_TYPE_MOVIE)
                            setLists(binding.popularList, popularListAdapter, popularList)
                            setMediaBackdrop(binding.popularList, popularList)
                        }
                        moviesLists.nowPlaying?.let { nowPlayingList ->
                            airingTodayOrNowPlayingListAdapter = getAdapter(nowPlayingList, MEDIA_TYPE_MOVIE)
                            setLists(binding.airingTodayOrNowPlayingList, airingTodayOrNowPlayingListAdapter, nowPlayingList)
                            setMediaBackdrop(binding.airingTodayOrNowPlayingList, nowPlayingList)
                        }
                        moviesLists.upcoming?.let { upcomingList ->
                            onTheAirOrUpcomingListAdapter = getAdapter(upcomingList, MEDIA_TYPE_MOVIE)
                            setLists(binding.onTheAirOrUpcomingList, onTheAirOrUpcomingListAdapter, upcomingList)
                            setMediaBackdrop(binding.onTheAirOrUpcomingList, upcomingList)
                        }
                        moviesLists.topRated?.let { topRatedList ->
                            topRatedListAdapter = getAdapter(topRatedList, MEDIA_TYPE_MOVIE)
                            setLists(binding.topRatedList, topRatedListAdapter, topRatedList)
                            setMediaBackdrop(binding.topRatedList, topRatedList)
                        }
                        binding.shimmerLayout.visibility = View.GONE
                        binding.mainUi.visibility = View.VISIBLE
                    }
                }
            }
            FRAGMENT_TV_SHOWS_LISTS -> {
                viewModel.getTvShowsLists().observe(viewLifecycleOwner) { list ->
                    list?.let {
                        tvShowsLists = it
                        tvShowsLists.popular?.let { popularList ->
                            popularListAdapter = getAdapter(popularList, MEDIA_TYPE_TV)
                            setLists(binding.popularList, popularListAdapter, popularList)
                            setMediaBackdrop(binding.popularList, popularList)
                        }
                        tvShowsLists.airingToday?.let { airingTodayList ->
                            airingTodayOrNowPlayingListAdapter = getAdapter(airingTodayList, MEDIA_TYPE_TV)
                            setLists(binding.airingTodayOrNowPlayingList, airingTodayOrNowPlayingListAdapter, airingTodayList)
                            setMediaBackdrop(binding.airingTodayOrNowPlayingList, airingTodayList)
                        }
                        tvShowsLists.onTheAir?.let { onTheAirList ->
                            onTheAirOrUpcomingListAdapter = getAdapter(onTheAirList, MEDIA_TYPE_TV)
                            setLists(binding.onTheAirOrUpcomingList, onTheAirOrUpcomingListAdapter, onTheAirList)
                            setMediaBackdrop(binding.onTheAirOrUpcomingList, onTheAirList)
                        }
                        tvShowsLists.topRated?.let { topRatedList ->
                            topRatedListAdapter = getAdapter(topRatedList, MEDIA_TYPE_TV)
                            setLists(binding.topRatedList, topRatedListAdapter, topRatedList)
                            setMediaBackdrop(binding.topRatedList, topRatedList)
                        }
                        binding.shimmerLayout.visibility = View.GONE
                        binding.mainUi.visibility = View.VISIBLE
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

    private fun getAdapter(mediaList: MediaList, mediaType: String): MediaRecyclerAdapter {
        return MediaRecyclerAdapter(
            mediaList,
            "",
            mediaType,
            false,
            { _, _, _, _ -> },
            { mediaId, mType ->
                val intent = when (mType) {
                    MEDIA_TYPE_MOVIE -> {
                        Intent(requireActivity(), MovieActivity::class.java)
                    }
                    else -> {
                        Intent(requireActivity(), TvShowsActivity::class.java)
                    }
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

    private fun setLists(
        listBinding: MediaListsBinding,
        adapter: MediaRecyclerAdapter,
        list: MediaList,
    ) {
        listBinding.mediaPeopleListRv.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        listBinding.mediaPeopleListRv.adapter = adapter
        if (list.results.isNullOrEmpty()) {
            listBinding.mediaPeopleListRv.visibility = View.GONE
            listBinding.warningTv.visibility = View.VISIBLE
        } else {
            listBinding.mediaPeopleListRv.visibility = View.VISIBLE
            listBinding.warningTv.visibility = View.GONE
        }
    }

    private fun setListsNames() {
        binding.popularList.listNameTv.text = LIST_POPULAR_NAME
        binding.popularList.radioGrp.visibility = View.GONE
        binding.topRatedList.listNameTv.text = LIST_TOP_RATED_NAME
        binding.topRatedList.radioGrp.visibility = View.GONE
        binding.onTheAirOrUpcomingList.radioGrp.visibility = View.GONE
        binding.airingTodayOrNowPlayingList.radioGrp.visibility = View.GONE
        when (fragmentName) {
            FRAGMENT_MOVIES_LISTS -> {
                binding.onTheAirOrUpcomingList.listNameTv.text = LIST_UPCOMING_NAME
                binding.airingTodayOrNowPlayingList.listNameTv.text = LIST_NOW_PLAYING_NAME
            }
            FRAGMENT_TV_SHOWS_LISTS -> {
                binding.onTheAirOrUpcomingList.listNameTv.text = LIST_ON_THE_AIR_NAME
                binding.airingTodayOrNowPlayingList.listNameTv.text = LIST_AIRING_TODAY_NAME
            }
        }
    }

    companion object {
        const val FRAGMENT_NAME_PARAM = "fragment_name"
        const val FRAGMENT_MOVIES_LISTS = "movies_lists_fragment"
        const val FRAGMENT_TV_SHOWS_LISTS = "tv_shows_lists_fragment"

        // Lists names
        private const val LIST_TOP_RATED_NAME = "Top Rated"
        private const val LIST_POPULAR_NAME = "Popular"
        private const val LIST_ON_THE_AIR_NAME = "On The Air"
        private const val LIST_AIRING_TODAY_NAME = "Airing Today"
        private const val LIST_UPCOMING_NAME = "Upcoming"
        private const val LIST_NOW_PLAYING_NAME = "Now Playing"

        fun newInstance(fragmentName: String): MediaListsFragment {
            return MediaListsFragment().apply {
                arguments = Bundle().apply {
                    putString(FRAGMENT_NAME_PARAM, fragmentName)
                }
            }
        }
    }

    private fun loadAndSetImageUsingGlide(imagePath: String, customDrawable: Int, imageView: ImageView, imageBaseUrl: String = BuildConfig.IMAGE_URL) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            try {
                if (imagePath.isNotEmpty()) {
                    if (imagePath[0] == '/') {
                        imagePath.removeRange(0, 0)
                    }
                    Glide.with(this@MediaListsFragment)
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