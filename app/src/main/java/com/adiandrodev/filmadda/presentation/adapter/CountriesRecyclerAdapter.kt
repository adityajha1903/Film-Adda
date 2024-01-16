package com.adiandrodev.filmadda.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.adiandrodev.filmadda.R
import com.adiandrodev.filmadda.data.model.Country
import com.adiandrodev.filmadda.databinding.CountriesRvItemBinding

class CountriesRecyclerAdapter(
    private val context: Context,
    private val countriesList: ArrayList<Country>,
    private var selectedCountryKey: String,
    private val countryClickListener: (countryName: String, countryKey: String) -> Unit
): RecyclerView.Adapter<CountriesRecyclerAdapter.CountryItemHolder>() {

    class CountryItemHolder(binding: CountriesRvItemBinding): RecyclerView.ViewHolder(binding.root) {
        val countryName = binding.countryNameTv
        val selectionIndicator = binding.selectedOrNotIv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryItemHolder {
        return CountryItemHolder(CountriesRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = countriesList.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CountryItemHolder, position: Int) {
        val country = countriesList[position]
        holder.countryName.text = country.english_name
        if (country.iso_3166_1 == selectedCountryKey) {
            holder.selectionIndicator.setBackgroundResource(R.drawable.selected)
        } else {
            holder.selectionIndicator.setBackgroundResource(R.drawable.not_selected)
        }
        holder.itemView.setOnClickListener {
            if (country.english_name != null && country.iso_3166_1 != null){
                selectedCountryKey = country.iso_3166_1
                notifyDataSetChanged()
                countryClickListener.invoke(country.english_name, country.iso_3166_1)
            } else {
                Toast.makeText(context, "Country Not Available", Toast.LENGTH_SHORT).show()
            }
        }
    }
}