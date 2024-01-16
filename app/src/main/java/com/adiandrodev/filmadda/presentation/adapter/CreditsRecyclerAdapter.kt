package com.adiandrodev.filmadda.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_TYPE_MOVIE
import com.adiandrodev.filmadda.data.model.CastOrCrewIn
import com.adiandrodev.filmadda.databinding.RecommendationRecyclerItemBinding

class CreditsRecyclerAdapter(
    private var castOrCrewList: ArrayList<CastOrCrewIn?>?,
    private var isCast: Boolean,
    private val mediaType: String,
    private val clickListener: (mediaId: Int, mediaType: String) -> Unit,
    private val setImage: (imagePath: String, view: ImageView) -> Unit
): RecyclerView.Adapter<CreditsRecyclerAdapter.ViewHolder>() {

    class ViewHolder(binding: RecommendationRecyclerItemBinding): RecyclerView.ViewHolder(binding.root) {
        val poster = binding.mediaIv
        val rating = binding.ratingTextView
        val ratingView = binding.ratingView
        val tv1 = binding.mediaItemTv1
        val tv2 = binding.mediaItemTv2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecommendationRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = castOrCrewList?.size?:0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val castOrCrew = castOrCrewList?.get(position)
        castOrCrew?.poster_path?.let {
            setImage.invoke(it, holder.poster)
        }
        castOrCrew?.vote_average?.let {
            val rating = roundToOneDecimalPlace(it)
            holder.rating.text = rating.toString()
            holder.ratingView.setProgress(rating)
        }
        when (mediaType) {
            MEDIA_TYPE_MOVIE -> {
                castOrCrew?.title?.let {
                    holder.tv1.text = it
                }
            }
            else -> {
                castOrCrew?.name?.let {
                    holder.tv1.text = it
                }
            }
        }
        if (isCast) {
            castOrCrew?.character?.let {
                holder.tv2.text = it
            }
        } else {
            castOrCrew?.job?.let {
                holder.tv2.text = it
            }
        }
        holder.itemView.setOnClickListener {
            castOrCrew?.id?.let { id ->
                clickListener.invoke(id, mediaType)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeDataSet(newList: ArrayList<CastOrCrewIn?>?, isCast: Boolean) {
        this.castOrCrewList = newList
        this.isCast = isCast
        notifyDataSetChanged()
    }

    private fun roundToOneDecimalPlace(number: Double): Double {
        return String.format("%.1f", number).toDouble()
    }
}