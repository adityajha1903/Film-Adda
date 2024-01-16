package com.adiandrodev.filmadda.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.adiandrodev.filmadda.data.model.Provider
import com.adiandrodev.filmadda.databinding.WatchProviderRvItemBinding

class WatchProviderRecyclerAdapter(
    private val providerList: ArrayList<Provider?>?,
    private val setLogo: (logoPath: String, view: ImageView) -> Unit
): RecyclerView.Adapter<WatchProviderRecyclerAdapter.ViewHolder>() {

    class ViewHolder(binding: WatchProviderRvItemBinding): RecyclerView.ViewHolder(binding.root) {
        val logo = binding.logoIv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(WatchProviderRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = providerList?.size?:0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        providerList?.get(position)?.logo_path?.let {
            setLogo.invoke(it, holder.logo)
        }
    }
}