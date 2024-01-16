package com.adiandrodev.filmadda.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.adiandrodev.filmadda.BuildConfig
import com.adiandrodev.filmadda.MyApplication
import com.adiandrodev.filmadda.MyApplication.Companion.APPLICATION_PREFERENCES
import com.adiandrodev.filmadda.MyApplication.Companion.COLLECTION_ID_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.COUNTRY_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_TYPE_MOVIE
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_TYPE_TV
import com.adiandrodev.filmadda.MyApplication.Companion.PERSON_ID_KEY
import com.adiandrodev.filmadda.R
import com.adiandrodev.filmadda.data.model.AllList
import com.adiandrodev.filmadda.data.model.CollectionList
import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.model.PeopleList
import com.adiandrodev.filmadda.databinding.ActivitySearchBinding
import com.adiandrodev.filmadda.databinding.MediaListsBinding
import com.adiandrodev.filmadda.di.Injector
import com.adiandrodev.filmadda.domain.model.SearchResult
import com.adiandrodev.filmadda.presentation.adapter.CollectionRecyclerAdapter
import com.adiandrodev.filmadda.presentation.adapter.MediaRecyclerAdapter
import com.adiandrodev.filmadda.presentation.adapter.PeopleRecyclerAdapter
import com.adiandrodev.filmadda.presentation.viewmodel.SearchViewModel
import com.adiandrodev.filmadda.presentation.viewmodel.SearchViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class SearchActivity : BaseActivity() {

    private lateinit var binding: ActivitySearchBinding

    @Inject
    lateinit var viewModelFactory: SearchViewModelFactory
    private lateinit var viewModel: SearchViewModel

    private var searchHistoryList = ArrayList<String>()

    private var searchedData = SearchResult(null, null, null, null)
    private var region = "US"

    private val emptyMediaList = MediaList(null, null, null, null)
    private val emptyCollectionList = CollectionList(null, null, null, null)
    private val emptyPeopleList = PeopleList(null, null, null, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        (application as Injector).createSearchSubComponent().inject(this)
        viewModel = viewModelFactory.create(SearchViewModel::class.java)
        region = getSharedPreferences(APPLICATION_PREFERENCES, Context.MODE_PRIVATE).getString(COUNTRY_KEY, "US") ?: "US"

        setViews()

        getSearchHistoryFromDatabase()

        initAdapters()
    }

    private fun initAdapters() {
        binding.collectionsList.mediaPeopleListRv.adapter = CollectionRecyclerAdapter(emptyCollectionList, {
            val intent = Intent(this, CollectionActivity::class.java)
            intent.putExtra(COLLECTION_ID_KEY, it)
            startActivity(intent)
        },{ imagePath, view ->
            loadAndSetImageUsingGlide(imagePath, R.drawable.custom_poster, view)
        })
        binding.collectionsList.mediaPeopleListRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.moviesList.mediaPeopleListRv.adapter = getMediaAdapter(MEDIA_TYPE_MOVIE)
        binding.moviesList.mediaPeopleListRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.tvShowsList.mediaPeopleListRv.adapter = getMediaAdapter(MEDIA_TYPE_TV)
        binding.tvShowsList.mediaPeopleListRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.peopleList.mediaPeopleListRv.adapter = PeopleRecyclerAdapter(emptyPeopleList, {
            val intent = Intent(this, PersonActivity::class.java)
            intent.putExtra(PERSON_ID_KEY, it)
            startActivity(intent)
        }, { imagePath, view ->
            loadAndSetImageUsingGlide(imagePath, R.drawable.custom_poster, view)
        })
        binding.peopleList.mediaPeopleListRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.collectionsList.warningTv.visibility = View.GONE
        binding.moviesList.warningTv.visibility = View.GONE
        binding.tvShowsList.warningTv.visibility = View.GONE
        binding.peopleList.warningTv.visibility = View.GONE
    }

    private fun getMediaAdapter(mediaType: String): MediaRecyclerAdapter {
        return MediaRecyclerAdapter(
            emptyMediaList,
            "",
            mediaType,
            false,
            { _, _, _, _ -> },
            { mediaId, mType ->
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
            }, { imagePath, view ->
                loadAndSetImageUsingGlide(imagePath, R.drawable.custom_poster, view)
            })
    }

    private fun setViews() {
        binding.collectionsList.listNameTv.text = getString(R.string.collections)
        binding.moviesList.listNameTv.text = getString(R.string.movies)
        binding.tvShowsList.listNameTv.text = getString(R.string.tvshows)
        binding.peopleList.listNameTv.text = getString(R.string.people)
        binding.collectionsList.radioGrp.visibility = View.GONE
        binding.moviesList.radioGrp.visibility = View.GONE
        binding.tvShowsList.radioGrp.visibility = View.GONE
        binding.peopleList.radioGrp.visibility = View.GONE
    }

    private fun getSearchHistoryFromDatabase() {
        viewModel.getSearchHistoryList().observe(this@SearchActivity) {
            it?.let { res ->
                res.reversed().forEachIndexed { index, searchHistoryEntity ->
                    searchHistoryList.add(index, searchHistoryEntity.searchedQuery)
                }
            }
            setClickListeners()
        }
    }

    private fun setClickListeners() {
        binding.backBtn.setOnClickListener { finish() }
        binding.clearIv.setOnClickListener { binding.searchView.text.clear() }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, searchHistoryList)
        binding.searchView.setAdapter(adapter)
        binding.searchView.setOnEditorActionListener { textView, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                val query = textView?.text
                if (!query.isNullOrEmpty()) {
                    hideKeyboard()
                    binding.noResultIv.visibility = View.GONE
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.searchResultLl.visibility = View.GONE
                    if (super.isNetworkConnected()) {
                        getSearchedDataFromDatabase(query.toString())
                    } else {
                        super.showErrorDialog()
                    }
                    if (!searchHistoryList.contains(query)) {
                        viewModel.insertSearchQuery(query.toString())
                        searchHistoryList.add(0, query.toString())
                        binding.searchView.setAdapter(ArrayAdapter(this, android.R.layout.simple_list_item_1, searchHistoryList))
                    }
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun getSearchedDataFromDatabase(query: String) {
        viewModel.getSearchResult(query, region).observe(this) {
            Log.i("TEST_TAG", "getSearchedDataFromDatabase: $it")
            it?.let { res ->
                searchedData = res
                setSearchedDataOnUi()
            }
        }
    }

    private fun setSearchedDataOnUi() {
        setUpCollectionsList(binding.collectionsList)
        setUpMediaList(searchedData.movies?: emptyMediaList, binding.moviesList)
        setUpMediaList(searchedData.tvShows ?: emptyMediaList, binding.tvShowsList)
        setUpPeopleList()
        binding.shimmerLayout.visibility = View.GONE
        binding.searchResultLl.visibility = View.VISIBLE
        if (areAllListEmpty()) {
            binding.searchResultLl.visibility = View.GONE
            binding.noResultIv.visibility = View.VISIBLE
        } else {
            binding.searchResultLl.visibility = View.VISIBLE
            binding.noResultIv.visibility = View.GONE
        }
    }

    private fun areAllListEmpty(): Boolean {
        return searchedData.people?.results.isNullOrEmpty() &&
                searchedData.collections?.results.isNullOrEmpty() &&
                searchedData.movies?.results.isNullOrEmpty() &&
                searchedData.tvShows?.results.isNullOrEmpty()
    }

    private fun setUpPeopleList() {
        if (searchedData.people?.results.isNullOrEmpty()) {
            binding.peopleList.root.visibility = View.GONE
        } else {
            binding.peopleList.root.visibility = View.VISIBLE
            (binding.peopleList.mediaPeopleListRv.adapter as PeopleRecyclerAdapter).changeDataList(searchedData.people?: emptyPeopleList)
            setBackdrop(binding.peopleList, searchedData.people)
        }
    }

    private fun setUpMediaList(mediaList: MediaList, listBinding: MediaListsBinding) {
        if (mediaList.results.isNullOrEmpty()) {
            listBinding.root.visibility = View.GONE
        } else {
            listBinding.root.visibility = View.VISIBLE
            (listBinding.mediaPeopleListRv.adapter as MediaRecyclerAdapter).changeDataList(mediaList)
            setBackdrop(listBinding, mediaList)
        }
    }

    private fun setUpCollectionsList(listBinding: MediaListsBinding) {
        if (searchedData.collections?.results.isNullOrEmpty()) {
            listBinding.root.visibility = View.GONE
        } else {
            listBinding.root.visibility = View.VISIBLE
            (listBinding.mediaPeopleListRv.adapter as CollectionRecyclerAdapter).changeDataList(searchedData.collections ?: emptyCollectionList)
            setBackdrop(listBinding, searchedData.collections)
        }
    }

    private fun setBackdrop(listBinding: MediaListsBinding, list: AllList?) {
        list?.results?.size?.let {
            val index = getRandomNumber(it - 1)
            val backdropPath = if (list is CollectionList || list is MediaList) {
                list.results?.get(index)?.backdrop_path
            } else {
                (list as PeopleList).results?.get(index)?.profile_path
            }
            loadAndSetImageUsingGlide(backdropPath?:"", R.drawable.custom_backdrop, listBinding.randomBackdropIv)
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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
                    Glide.with(this@SearchActivity)
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