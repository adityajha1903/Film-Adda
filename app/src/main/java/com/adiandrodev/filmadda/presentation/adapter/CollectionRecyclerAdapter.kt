package com.adiandrodev.filmadda.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.adiandrodev.filmadda.data.model.CollectionList
import com.adiandrodev.filmadda.databinding.MediaOrPersonRecyclerItemBinding

class CollectionRecyclerAdapter(
    private var collectionList: CollectionList,
    private val clickListener: (collectionId: Int) -> Unit,
    private val setImage: (imagePath: String, view: ImageView) -> Unit
) : RecyclerView.Adapter<CollectionRecyclerAdapter.ViewHolder>() {

    class ViewHolder(binding: MediaOrPersonRecyclerItemBinding): RecyclerView.ViewHolder(binding.root) {
        val poster = binding.mediaOrPersonIv
        val text1 = binding.mediaItemTv1
        val text2 = binding.mediaItemTv2
        val deleteBtn = binding.deleteMediaButton
        val rateView = binding.ratingFrameLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MediaOrPersonRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = collectionList.results?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.deleteBtn.visibility = View.GONE
        holder.rateView.visibility = View.GONE
        holder.text2.visibility = View.GONE
        val collection = collectionList.results?.get(position)
        val posterPath = collection?.poster_path
        posterPath?.let {
            setImage.invoke(it, holder.poster)
        }
        holder.text1.text = collection?.name
        holder.itemView.setOnClickListener {
            collection?.id?.let {
                clickListener.invoke(it)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeDataList(collectionList: CollectionList) {
        this.collectionList = collectionList
        notifyDataSetChanged()
    }

}
