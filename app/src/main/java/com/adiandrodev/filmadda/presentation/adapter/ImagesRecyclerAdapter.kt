package com.adiandrodev.filmadda.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import com.adiandrodev.filmadda.data.model.Image
import com.adiandrodev.filmadda.data.model.Images
import com.adiandrodev.filmadda.databinding.ImagesRecyclerItemBinding


class ImagesRecyclerAdapter(
    private val context: Context,
    private var images: Images?,
    private var isPoster: Boolean,
    private val setImages: (imagePath: String, view: ImageView) -> Unit,
    private val clickListener: (imagePath: String) -> Unit
): RecyclerView.Adapter<ImagesRecyclerAdapter.ViewHolder>() {

    class ViewHolder(binding: ImagesRecyclerItemBinding): RecyclerView.ViewHolder(binding.root) {
        val image = binding.imageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ImagesRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return if (isPoster) {
            images?.posters?.size?:0
        } else {
            images?.backdrops?.size?:0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image: Image?
        if (isPoster) {
            val params = RelativeLayout.LayoutParams(100.toPixels(), 150.toPixels())
            params.setMargins(4.toPixels())
            holder.image.layoutParams = params
            image = images?.posters?.get(position)
        } else {
            val params = RelativeLayout.LayoutParams(240.toPixels(), 135.toPixels())
            params.setMargins(4.toPixels())
            holder.image.layoutParams = params
            image = images?.backdrops?.get(position)
        }
        image?.file_path?.let { path ->
            setImages.invoke(path, holder.image)
            holder.itemView.setOnClickListener {
                clickListener.invoke(path)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun dataSetChanged(images: Images?, isPoster: Boolean) {
        this.images = images
        this.isPoster = isPoster
        notifyDataSetChanged()
    }

    private fun Int.toPixels(): Int {
        val displayMetrics = context.resources.displayMetrics
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            displayMetrics
        ).toInt()
    }
}
