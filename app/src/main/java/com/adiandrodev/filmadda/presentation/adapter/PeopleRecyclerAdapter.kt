package com.adiandrodev.filmadda.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.adiandrodev.filmadda.data.model.PeopleList
import com.adiandrodev.filmadda.databinding.MediaOrPersonRecyclerItemBinding

class PeopleRecyclerAdapter(
    private var peopleList: PeopleList,
    private val clickListener: (peopleId: Int) -> Unit,
    private val setImage: (imagePath: String, view: ImageView) -> Unit
): RecyclerView.Adapter<PeopleRecyclerAdapter.PeopleViewHolder>() {

    class PeopleViewHolder(binding: MediaOrPersonRecyclerItemBinding): RecyclerView.ViewHolder(binding.root) {
        val deleteBtn = binding.deleteMediaButton
        val image = binding.mediaOrPersonIv
        val ratingFrame = binding.ratingFrameLayout
        val text1 = binding.mediaItemTv1
        val text2 = binding.mediaItemTv2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        return PeopleViewHolder(MediaOrPersonRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = peopleList.results?.size ?: 0

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        holder.deleteBtn.visibility = View.GONE
        holder.ratingFrame.visibility = View.GONE
        peopleList.results?.get(position)?.let { person ->
            person.profile_path?.let {
                setImage.invoke(it, holder.image)
            }
            holder.text1.text = person.name
            holder.text2.text = person.known_for_department
            holder.itemView.setOnClickListener {
                person.id?.let{
                    clickListener.invoke(it)
                }
            }
        }
    }

    fun addList(extraPeopleList: PeopleList) {
        extraPeopleList.results?.let {
            peopleList.results?.addAll(it)
            var s = peopleList.results?.size?:0
            val e = s + it.size
            while (s < e) {
                notifyItemInserted(s)
                s++
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeDataList(peopleList: PeopleList) {
        this.peopleList = peopleList
        notifyDataSetChanged()
    }
}
