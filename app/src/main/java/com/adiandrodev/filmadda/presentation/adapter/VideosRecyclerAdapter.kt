package com.adiandrodev.filmadda.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.adiandrodev.filmadda.data.model.Videos
import com.adiandrodev.filmadda.databinding.VideoRecyclerItemBinding

class VideosRecyclerAdapter(
    private var videos: Videos?,
    private val setThumbnail: (key: String, view: ImageView) -> Unit,
    private val clickListener: (key: String) -> Unit,
    private val showToast: (message: String) -> Unit
): RecyclerView.Adapter<VideosRecyclerAdapter.ViewHolder>() {

    class ViewHolder(binding: VideoRecyclerItemBinding): RecyclerView.ViewHolder(binding.root) {
        val thumbnail = binding.thumbnailIv
        val name = binding.videoNameTv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(VideoRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = videos?.results?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = videos?.results?.get(position)
        video?.key?.let {
            setThumbnail.invoke(it, holder.thumbnail)
        }
        video?.type?.let {
            holder.name.text = it
        }
        holder.itemView.setOnClickListener {
            if (video?.site == "YouTube") {
                video.key?.let {
                    clickListener.invoke(it)
                }
            } else {
                showToast.invoke("Video not available on youtube")
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun dataSetChanged(videos: Videos) {
        this.videos = videos
        notifyDataSetChanged()
    }
}