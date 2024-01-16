package com.adiandrodev.filmadda.presentation.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.adiandrodev.filmadda.BuildConfig
import com.adiandrodev.filmadda.MyApplication.Companion.APPLICATION_PREFERENCES
import com.adiandrodev.filmadda.MyApplication.Companion.COUNTRY_KEY
import com.adiandrodev.filmadda.MyApplication.Companion.COUNTRY_NAME
import com.adiandrodev.filmadda.R
import com.adiandrodev.filmadda.data.model.Country
import com.adiandrodev.filmadda.databinding.ActivitySettingsBinding
import com.adiandrodev.filmadda.databinding.CountriesListDialogBinding
import com.adiandrodev.filmadda.di.Injector
import com.adiandrodev.filmadda.presentation.adapter.CountriesRecyclerAdapter
import com.adiandrodev.filmadda.presentation.viewmodel.SettingsViewModel
import com.adiandrodev.filmadda.presentation.viewmodel.SettingsViewModelFactory
import javax.inject.Inject

class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding

    @Inject
    lateinit var viewModelFactory: SettingsViewModelFactory
    private lateinit var viewModel: SettingsViewModel

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var selectedCountryKey: String
    private lateinit var selectedCountryName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        (application as Injector).createSettingsSubComponent().inject(this)
        viewModel = viewModelFactory.create(SettingsViewModel::class.java)
        sharedPreferences = getSharedPreferences(APPLICATION_PREFERENCES, Context.MODE_PRIVATE)
        binding.includedAppBar.activityTitle.text = getString(R.string.settings)
        selectedCountryName = sharedPreferences.getString(COUNTRY_NAME, "United States of America").toString()
        selectedCountryKey = sharedPreferences.getString(COUNTRY_KEY, "US").toString()
        binding.countryNameTv.text = selectedCountryName

        binding.includedAppBar.backBtn.setOnClickListener { finish() }

        binding.selectCountryButton.setOnClickListener {
            if (super.isNetworkConnected()) {
                getAvailableCountries()
            } else {
                super.showErrorDialog()
            }
        }

        binding.shareAppButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Share App")
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sharing_text) + "\n\n" + BuildConfig.PLAY_STORE_SHARE_LINK + packageName)
            startActivity(Intent.createChooser(intent, "Share App Via..."))
        }

        binding.rateAppButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.PLAY_STORE_SHARE_LINK + packageName))
            startActivity(intent)
        }

        binding.giveFeedbackButton.setOnClickListener {
            sendFeedBackToDev()
        }

        binding.developedByButton.setOnClickListener {
            launchDevTwitter()
        }

        binding.tmdbWebsiteButton.setOnClickListener {
            launchTMDBWebsite()
        }
    }

    private fun launchDevTwitter() {
        val twitterPageUrl = BuildConfig.TWITTER_BASE_URL + BuildConfig.DEVELOPER_TWITTER
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(twitterPageUrl))
        startActivity(intent)
    }

    private fun launchTMDBWebsite() {
        try {
            val intent = CustomTabsIntent.Builder().build()
            intent.launchUrl(this, Uri.parse(BuildConfig.TMDB_URL))
        } catch (e: Exception) {
            Toast.makeText(this, "Please Check your Internet connection.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getAvailableCountries() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getAllAvailableCountries().observe(this) { countries ->
            binding.progressBar.visibility = View.GONE
            countries?.let {
                showCountryListDialog(it)
            }
        }
    }

    private fun showCountryListDialog(countries: ArrayList<Country>) {
        var cName = selectedCountryName
        var cKey = selectedCountryKey
        val dialog = Dialog(this)
        val dialogBinding = CountriesListDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialogBinding.backBtn.setOnClickListener { dialog.dismiss() }
        dialogBinding.countriesRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        dialogBinding.countriesRv.adapter = CountriesRecyclerAdapter(this, countries, selectedCountryKey) { countryName, countryKey ->
            cKey = countryKey
            cName = countryName
        }
        dialogBinding.applyBtn.setOnClickListener {
            if (cKey != selectedCountryKey) {
                sharedPreferences.edit().putString(COUNTRY_KEY, cKey).putString(COUNTRY_NAME, cName).apply()
                selectedCountryKey = cKey
                selectedCountryName = cName
                binding.countryNameTv.text = cName
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun sendFeedBackToDev() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(BuildConfig.DEVELOPER_EMAIL))

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Your phone doesn't have any email app", Toast.LENGTH_SHORT).show()
        }
    }
}