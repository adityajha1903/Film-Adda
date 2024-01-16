package com.adiandrodev.filmadda.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.adiandrodev.filmadda.data.model.People
import com.adiandrodev.filmadda.databinding.CastRecyclerItemBinding

class CastRecyclerAdapter(
    private val castList: List<People?>,
    private val clickListener: (personId: Int) -> Unit,
    private val setImage: (imagePath: String, view :ImageView) -> Unit
): RecyclerView.Adapter<CastRecyclerAdapter.CastViewHolder>() {

    class CastViewHolder(binding: CastRecyclerItemBinding): RecyclerView.ViewHolder(binding.root) {
        val image = binding.castIv
        val tv1 = binding.tv1
        val tv2 = binding.tv2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(CastRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = castList.size

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val cast = castList[position]

        cast?.profile_path?.let {
            setImage.invoke(it, holder.image)
        }
        holder.tv1.text = cast?.name
        holder.tv2.text = cast?.character
        holder.itemView.setOnClickListener {
            cast?.id?.let {
                clickListener.invoke(it)
            }
        }
    }
}
