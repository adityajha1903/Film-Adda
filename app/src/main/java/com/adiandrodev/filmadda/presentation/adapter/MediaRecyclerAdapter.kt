package com.adiandrodev.filmadda.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_TYPE_MOVIE
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_TYPE_TV
import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.databinding.MediaOrPersonRecyclerItemBinding

class MediaRecyclerAdapter(
    private var mediaList: MediaList,
    private val listType: String = "",
    private var mediaType: String,
    private val keepDeleteButton: Boolean,
    private val deleteClickListener: (mediaId: Int, mediaType: String, position: Int, listType: String) -> Unit,
    private val clickListener: (mediaId: Int, mediaType: String) -> Unit,
    private val setImage: (imagePath: String, view: ImageView) -> Unit
): RecyclerView.Adapter<MediaRecyclerAdapter.MediaHolder>() {

    class MediaHolder(binding: MediaOrPersonRecyclerItemBinding): RecyclerView.ViewHolder(binding.root) {
        val deleteBtn = binding.deleteMediaButton
        val image = binding.mediaOrPersonIv
        val ratingText = binding.ratingTextView
        val ratingView = binding.ratingView
        val ratingFrame = binding.ratingFrameLayout
        val text1 = binding.mediaItemTv1
        val text2 = binding.mediaItemTv2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaHolder {
        return MediaHolder(MediaOrPersonRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = mediaList.results?.size ?: 0

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MediaHolder, position: Int) {
        if (keepDeleteButton) {
            holder.deleteBtn.visibility = View.VISIBLE
            holder.deleteBtn.setOnClickListener {
                val mediaId = mediaList.results?.get(position)?.id ?: 0
                deleteClickListener.invoke(mediaId, mediaType, position, listType)
            }
        } else {
            holder.deleteBtn.visibility = View.GONE
        }

        val media = mediaList.results?.get(position)
        holder.ratingFrame.visibility = View.VISIBLE
        media?.vote_average?.let {
            val rating = roundToOneDecimalPlace(it)
            holder.ratingText.text = rating.toString()
            holder.ratingView.setProgress(rating)
        }
        media?.poster_path?.let {
            setImage.invoke(it, holder.image)
        }
        val mediaId = media?.id ?: 0
        holder.itemView.setOnClickListener {
            clickListener.invoke(mediaId, mediaType)
        }
        if (mediaType == MEDIA_TYPE_MOVIE) {
            holder.text1.text = media?.title
            if (media?.release_date.toString().length > 3)
                holder.text2.text = media?.release_date.toString().subSequence(0, 4)
        } else if (mediaType == MEDIA_TYPE_TV) {
            holder.text1.text = media?.name
            if (media?.first_air_date.toString().length > 3)
                holder.text2.text = media?.first_air_date.toString().subSequence(0, 4)
        }
    }

    private fun roundToOneDecimalPlace(number: Double): Double {
        return String.format("%.1f", number).toDouble()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeDataList(newMediaList: MediaList, newMediaType: String = mediaType){
        this.mediaList = newMediaList
        this.mediaType = newMediaType
        notifyDataSetChanged()
    }
}