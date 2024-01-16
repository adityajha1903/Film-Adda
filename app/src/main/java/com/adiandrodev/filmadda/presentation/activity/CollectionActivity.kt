package com.adiandrodev.filmadda.presentation.activity

import android.content.Intent
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
import com.adiandrodev.filmadda.MyApplication.Companion.COLLECTION_ID_KEY
import com.adiandrodev.filmadda.R
import com.adiandrodev.filmadda.data.model.CollectionDetails
import com.adiandrodev.filmadda.databinding.ActivityCollectionBinding
import com.adiandrodev.filmadda.di.Injector
import com.adiandrodev.filmadda.presentation.adapter.CollectionMovieRecyclerAdapter
import com.adiandrodev.filmadda.presentation.viewmodel.CollectionViewModel
import com.adiandrodev.filmadda.presentation.viewmodel.CollectionViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CollectionActivity : BaseActivity() {

    private lateinit var binding: ActivityCollectionBinding

    @Inject
    lateinit var viewModelFactory: CollectionViewModelFactory
    private lateinit var viewModel: CollectionViewModel

    private var collectionId = -1
    private lateinit var collectionDetails: CollectionDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_collection)
        (application as Injector).createCollectionSubComponent().inject(this)
        viewModel = viewModelFactory.create(CollectionViewModel::class.java)
        collectionId = intent.getIntExtra(COLLECTION_ID_KEY, -1)

        binding.backBtn.setOnClickListener { finish() }

        if (collectionId != -1) {
            if (super.isNetworkConnected()) {
                getCollectionDetails()
            } else {
                super.showErrorDialog()
            }
        }
    }

    private fun getCollectionDetails() {
        viewModel.getCollectionDetails(collectionId).observe(this){
            it?.let { res ->
                collectionDetails = res
                collectionDetails.poster_path?.let { posterPath ->
                    loadAndSetImageUsingGlide(posterPath, binding.collectionPosterIv)
                }
                collectionDetails.backdrop_path?.let { backdropPath ->
                    loadAndSetImageUsingGlide(backdropPath, binding.backgroundPosterIv)
                }
                collectionDetails.name?.let { name ->
                    binding.collectionTitleTv.text = name
                }
                collectionDetails.overview?.let { overview ->
                    binding.overviewTv.text = overview
                }
                setUpRecyclerView()
                binding.shimmerLayout.visibility = View.GONE
                binding.mainUi.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.movieRv.layoutManager = LinearLayoutManager(this)
        binding.movieRv.adapter = CollectionMovieRecyclerAdapter(collectionDetails.parts, { posterPath, view ->
            loadAndSetImageUsingGlide(posterPath, view)
        }, { mediaId, mediaType ->
            val intent = when (mediaType) {
                MyApplication.MEDIA_TYPE_MOVIE -> {
                    Intent(this, MovieActivity::class.java)
                }
                else -> Intent(this, TvShowsActivity::class.java)
            }
            intent.putExtra(MyApplication.MEDIA_ID_KEY, mediaId)
            startActivity(intent)
        })
    }

    private fun loadAndSetImageUsingGlide(imagePath: String, imageView: ImageView, imageBaseUrl: String = BuildConfig.IMAGE_URL) {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                if (imagePath.isNotEmpty()) {
                    if (imagePath[0] == '/') {
                        imagePath.removeRange(0, 0)
                    }

                    Glide.with(this@CollectionActivity)
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

}