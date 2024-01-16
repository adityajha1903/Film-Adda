package com.adiandrodev.filmadda.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.adiandrodev.filmadda.data.model.Season
import com.adiandrodev.filmadda.databinding.SeasonRecyclerItemBinding

class SeasonsRecyclerAdapter(
    private val seasonsList: ArrayList<Season?>?,
    private val setImage: (posterPath: String, view: ImageView) -> Unit,
    private val clickListener: (seasonNo: Int) -> Unit
): RecyclerView.Adapter<SeasonsRecyclerAdapter.ViewHolder>() {

    class ViewHolder(binding: SeasonRecyclerItemBinding): RecyclerView.ViewHolder(binding.root) {
        val seasonNoTv = binding.seasonNoTv
        val noOfEpisodesTv = binding.noOfEpisodesTv
        val poster = binding.seasonPosterIv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SeasonRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = seasonsList?.size?:0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val season = seasonsList?.get(position)
        season?.poster_path?.let {
            setImage.invoke(it, holder.poster)
        }
        season?.season_number?.let {
            holder.seasonNoTv.text = it.toString()
        }
        season?.episode_count?.let {
            holder.noOfEpisodesTv.text = it.toString()
        }
        holder.itemView.setOnClickListener {
            season?.season_number?.let { seasonNo ->
                clickListener.invoke(seasonNo)
            }
        }
    }
}
