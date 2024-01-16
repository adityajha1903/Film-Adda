package com.adiandrodev.filmadda.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.adiandrodev.filmadda.data.model.Media
import com.adiandrodev.filmadda.databinding.CollectionMovieRecyclerAdapterBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CollectionMovieRecyclerAdapter(
    private val collectionParts: ArrayList<Media?>?,
    private val serImage: (posterPath: String, view: ImageView) -> Unit,
    private val clickListener: (mediaId: Int, mediaType: String) -> Unit
): RecyclerView.Adapter<CollectionMovieRecyclerAdapter.ViewHolder>() {

    class ViewHolder(binding: CollectionMovieRecyclerAdapterBinding): RecyclerView.ViewHolder(binding.root) {
        val poster = binding.moviePosterIv
        val title = binding.movieTitleTv
        val releaseDate = binding.movieReleaseDateTv
        val overview = binding.movieOverviewTv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CollectionMovieRecyclerAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = collectionParts?.size?:0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val part = collectionParts?.get(position)
        part?.poster_path?.let {
            serImage.invoke(it, holder.poster)
        }
        part?.title?.let {
            holder.title.text = it
        }
        part?.release_date?.let {
            holder.releaseDate.text = formatDate(it)
        }
        part?.overview?.let {
            holder.overview.text = it
        }
        holder.itemView.setOnClickListener {
            part?.id?.let { id ->
                part.media_type?.let { mediaType ->
                    clickListener.invoke(id, mediaType)
                }
            }
        }
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
