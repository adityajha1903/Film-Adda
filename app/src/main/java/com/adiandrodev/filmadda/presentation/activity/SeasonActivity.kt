package com.adiandrodev.filmadda.presentation.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.adiandrodev.filmadda.BuildConfig
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_ID_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.SEASON_NO_KEY
import com.adiandrodev.filmadda.R
import com.adiandrodev.filmadda.data.model.Season
import com.adiandrodev.filmadda.databinding.ActivitySeasonBinding
import com.adiandrodev.filmadda.di.Injector
import com.adiandrodev.filmadda.presentation.adapter.EpisodeRecyclerAdapter
import com.adiandrodev.filmadda.presentation.viewmodel.SeasonViewModel
import com.adiandrodev.filmadda.presentation.viewmodel.SeasonViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class SeasonActivity : BaseActivity() {

    private lateinit var binding: ActivitySeasonBinding

    @Inject
    lateinit var viewModelFactory: SeasonViewModelFactory
    private lateinit var viewModel: SeasonViewModel

    private var seasonId = -1
    private var seasonNo = -1

    private lateinit var season: Season

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_season)
        (application as Injector).createSeasonSubComponent().inject(this)
        viewModel = viewModelFactory.create(SeasonViewModel::class.java)
        seasonId = intent.getIntExtra(MEDIA_ID_KEY, -1)
        seasonNo = intent.getIntExtra(SEASON_NO_KEY, -1)
        binding.backBtn.setOnClickListener { finish() }

        if (seasonId != -1 && seasonNo != -1) {
            if (super.isNetworkConnected()) {
                getSeasonDetails()
            } else {
                super.showErrorDialog()
            }
        }
    }

    private fun getSeasonDetails() {
        viewModel.getSeasonDetails(seasonId, seasonNo).observe(this@SeasonActivity) {
            it?.let { res ->
                season = res
                lifecycleScope.launch(Dispatchers.Default) {
                    season.poster_path?.let { posterPath ->
                        loadAndSetImageUsingGlide(posterPath, R.drawable.custom_poster, binding.seasonPosterIv)
                    }
                    if (!season.episodes.isNullOrEmpty() && !season.episodes?.get(0)?.still_path.isNullOrEmpty()) {
                        season.episodes?.get(0)?.still_path?.let { stillPath ->
                            loadAndSetImageUsingGlide(stillPath, imageView = binding.backgroundPosterIv)
                        }
                    } else {
                        binding.tv1.setTextColor(ResourcesCompat.getColor(resources, R.color.background_color_rev, null))
                        binding.seasonNoTv.setTextColor(ResourcesCompat.getColor(resources, R.color.background_color_rev, null))
                    }
                    season.season_number?.let { no ->
                        binding.seasonNoTv.text = no.toString()
                    }
                    season.air_date?.let { airDate ->
                        if (airDate.isNotEmpty()) {
                            binding.firstAirDateTv.text = formatDate(airDate)
                        }
                    }
                    season.vote_average?.let { rating ->
                        binding.ratingTextView.text = roundToOneDecimalPlace(rating).toString()
                        binding.ratingView.setProgress(roundToOneDecimalPlace(rating))
                    }
                    season.overview?.let {  overview ->
                        if (overview.isEmpty()) {
                            binding.overviewLl.visibility = View.GONE
                        } else {
                            binding.overviewTv.text = overview
                        }
                    }
                }
                lifecycleScope.launch(Dispatchers.Main) {
                    try {
                        binding.episodesRv.layoutManager = LinearLayoutManager(this@SeasonActivity, LinearLayoutManager.HORIZONTAL, false)
                        binding.episodesRv.adapter = EpisodeRecyclerAdapter(season.episodes, lifecycleScope)
                        binding.shimmerLayout.visibility = View.GONE
                        binding.mainUi.visibility = View.VISIBLE
                    } catch (e: java.lang.Exception) {
                        Log.e("TEST_TAG", "getSeasonDetails: ${e.message}")
                    }
                }
            }
        }
    }

    private fun roundToOneDecimalPlace(number: Double): Double {
        return String.format("%.1f", number).toDouble()
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

    private fun loadAndSetImageUsingGlide(imagePath: String, customDrawable: Int? = null, imageView: ImageView, imageBaseUrl: String = BuildConfig.IMAGE_URL) {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                if (imagePath.isNotEmpty()) {
                    if (imagePath[0] == '/') {
                        imagePath.removeRange(0, 0)
                    }
                    if (customDrawable == null) {
                        Glide.with(this@SeasonActivity)
                            .load(imageBaseUrl + imagePath)
                            .centerCrop()
                            .into(imageView)
                    } else {
                        Glide.with(this@SeasonActivity)
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