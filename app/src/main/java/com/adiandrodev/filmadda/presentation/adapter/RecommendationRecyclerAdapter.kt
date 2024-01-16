package com.adiandrodev.filmadda.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_TYPE_MOVIE
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_TYPE_TV
import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.databinding.RecommendationRecyclerItemBinding

class RecommendationRecyclerAdapter(
    private val mediaList: MediaList?,
    private val setImage: (imagePath: String, view: ImageView) -> Unit,
    private val clickListener: (mediaId: Int, mediaType: String) -> Unit
): RecyclerView.Adapter<RecommendationRecyclerAdapter.ViewHolder>() {

    class ViewHolder(binding: RecommendationRecyclerItemBinding): RecyclerView.ViewHolder(binding.root) {
        val poster = binding.mediaIv
        val ratingView = binding.ratingView
        val ratingText = binding.ratingTextView
        val tv1 = binding.mediaItemTv1
        val tv2 = binding.mediaItemTv2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecommendationRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = mediaList?.results?.size?:0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val media = mediaList?.results?.get(position)
        media?.poster_path?.let {
            setImage.invoke(it, holder.poster)
        }
        media?.vote_average?.let {
            val rating = roundToOneDecimalPlace(it)
            holder.ratingText.text = rating.toString()
            holder.ratingView.setProgress(rating)
        }
        holder.itemView.setOnClickListener {
            media?.id?.let { id ->
                media.media_type?.let { type ->
                    clickListener.invoke(id, type)
                }
            }
        }
        if (media?.media_type == MEDIA_TYPE_MOVIE) {
            holder.tv1.text = media.title
            if (media.release_date.toString().length > 3)
                holder.tv2.text = media.release_date.toString().subSequence(0, 4)
        } else if (media?.media_type == MEDIA_TYPE_TV) {
            holder.tv1.text = media.name
            if (media.first_air_date.toString().length > 3)
                holder.tv2.text = media.first_air_date.toString().subSequence(0, 4)
        }
    }

    private fun roundToOneDecimalPlace(number: Double): Double {
        return String.format("%.1f", number).toDouble()
    }
}
