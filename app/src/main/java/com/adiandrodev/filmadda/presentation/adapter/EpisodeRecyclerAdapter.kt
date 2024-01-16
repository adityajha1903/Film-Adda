package com.adiandrodev.filmadda.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adiandrodev.filmadda.data.model.Episode
import com.adiandrodev.filmadda.databinding.EpisodeRecyclerItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EpisodeRecyclerAdapter(
    private var episodes: ArrayList<Episode?>?,
    private val scope: CoroutineScope
): RecyclerView.Adapter<EpisodeRecyclerAdapter.ViewHolder>() {

    class ViewHolder(binding: EpisodeRecyclerItemBinding): RecyclerView.ViewHolder(binding.root) {
        val episodeNo = binding.episodeNoTv
        val episodeName = binding.episodeNameTv
        val airDate = binding.airDateTv
        val runtime = binding.runtimeTv
        val overview = binding.overviewTv
        val rating = binding.ratingTextView
        val ratingProgress = binding.ratingView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(EpisodeRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = episodes?.size?:0

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        scope.launch(Dispatchers.Default) {
            val episode = episodes?.get(position)
            episode?.episode_number?.let {
                holder.episodeNo.text = "$it)"
            }
            episode?.name?.let {
                holder.episodeName.text = it
            }
            episode?.air_date?.let {
                if (it.isNotEmpty()) {
                    holder.airDate.text = formatDate(it)
                }
            }
            episode?.runtime?.let {
                if (it != 0) {
                    val h = (it / 60).toString()
                    val m = (it % 60).toString()
                    holder.runtime.text = "${h}h ${m}m"
                }
            }
            episode?.overview?.let {
                if (it.isNotEmpty()) {
                    holder.overview.text = it
                }
            }
            episode?.vote_average?.let {
                holder.ratingProgress.setProgress(roundToOneDecimalPlace(it))
                holder.rating.text = roundToOneDecimalPlace(it).toString()
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
}
