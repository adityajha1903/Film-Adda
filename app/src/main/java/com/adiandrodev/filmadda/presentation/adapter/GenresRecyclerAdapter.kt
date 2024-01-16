package com.adiandrodev.filmadda.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adiandrodev.filmadda.data.model.Genre
import com.adiandrodev.filmadda.databinding.GenreItemBinding

class GenresRecyclerAdapter(
    private val genresList: ArrayList<Genre?>
): RecyclerView.Adapter<GenresRecyclerAdapter.ViewHolder>() {

    class ViewHolder(binding: GenreItemBinding): RecyclerView.ViewHolder(binding.root) {
        val tv = binding.genreTv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(GenreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = genresList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv.text = genresList[position]?.name
    }

}
