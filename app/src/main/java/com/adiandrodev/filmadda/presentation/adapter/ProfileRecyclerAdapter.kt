package com.adiandrodev.filmadda.presentation.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import com.adiandrodev.filmadda.data.model.Images
import com.adiandrodev.filmadda.databinding.ImagesRecyclerItemBinding

class ProfileRecyclerAdapter(
    private val context: Context,
    private var images: Images?,
    private val setImages: (imagePath: String, view: ImageView) -> Unit,
    private val clickListener: (imagePath: String) -> Unit
): RecyclerView.Adapter<ProfileRecyclerAdapter.ViewHolder>() {

    class ViewHolder(binding: ImagesRecyclerItemBinding): RecyclerView.ViewHolder(binding.root) {
        val image = binding.imageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ImagesRecyclerItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount() = images?.profiles?.size?:0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val params = RelativeLayout.LayoutParams(100.toPixels(), 150.toPixels())
        params.setMargins(4.toPixels())
        holder.image.layoutParams = params
        val profile = images?.profiles?.get(position)
        profile?.file_path?.let {
            setImages.invoke(it, holder.image)
        }
        holder.itemView.setOnClickListener {
            profile?.file_path?.let { path ->
                clickListener.invoke(path)
            }
        }
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
