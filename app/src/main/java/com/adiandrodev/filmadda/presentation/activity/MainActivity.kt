package com.adiandrodev.filmadda.presentation.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.adiandrodev.filmadda.R
import com.adiandrodev.filmadda.databinding.ActivityMainBinding
import com.adiandrodev.filmadda.presentation.fragment.MediaListsFragment
import com.adiandrodev.filmadda.presentation.fragment.PeopleFragment
import com.adiandrodev.filmadda.presentation.fragment.TrendingFragment
import com.google.android.play.core.review.ReviewManagerFactory

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (isNetworkConnected()) {
            setBottomNavigationView()
        } else {
            showErrorDialog()
        }

        setClickListeners()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.bottomNavigationView.selectedItemId == R.id.nav_home) {
                    finish()
                } else {
                    binding.bottomNavigationView.selectedItemId = R.id.nav_home
                }
            }
        })

        showReviewDialog()
    }

    private fun setBottomNavigationView() {
        binding.bottomNavigationView.itemIconTintList = ColorStateList(arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked)
        ), intArrayOf(
            ContextCompat.getColor(this, R.color.background_color),
            ContextCompat.getColor(this, R.color.icon_color)
        ))

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {loadFragment(FRAGMENT_HOME)}
                R.id.nav_moviesLists -> loadFragment(FRAGMENT_MOVIES)
                R.id.nav_tvLists -> loadFragment(FRAGMENT_TV_SHOWS)
                R.id.nav_persons -> loadFragment(FRAGMENT_PERSONS)
            }
            true
        }
        supportFragmentManager.beginTransaction()
            .add(binding.container.id, fragmentList[FRAGMENT_HOME] as Fragment, FRAGMENT_HOME)
            .commit()
    }

    private val fragmentList = mapOf(
        Pair(FRAGMENT_HOME, TrendingFragment()),
        Pair(FRAGMENT_MOVIES, MediaListsFragment.newInstance(MediaListsFragment.FRAGMENT_MOVIES_LISTS)),
        Pair(FRAGMENT_TV_SHOWS, MediaListsFragment.newInstance(MediaListsFragment.FRAGMENT_TV_SHOWS_LISTS)),
        Pair(FRAGMENT_PERSONS, PeopleFragment())
    )

    private var lastVisibleFragment = fragmentList[FRAGMENT_HOME] as Fragment

    private fun loadFragment(fragmentTag: String){
        val ft = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentByTag(fragmentTag)
        if (fragment == null) {
            fragment = fragmentList[fragmentTag] as Fragment
            ft.hide(lastVisibleFragment)
            ft.add(binding.container.id, fragment, fragmentTag)
        } else {
            ft.hide(lastVisibleFragment)
            ft.show(fragment)
        }
        lastVisibleFragment = fragment
        ft.commit()
    }

    private fun setClickListeners() {
        binding.settingButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showReviewDialog(){
        val reviewManager = ReviewManagerFactory.create(applicationContext)
        reviewManager.requestReviewFlow().addOnCompleteListener{
            if (it.isSuccessful){
                reviewManager.launchReviewFlow(this , it.result)
            }
        }
    }

    companion object {
        private const val FRAGMENT_HOME = "home_fragment"
        private const val FRAGMENT_MOVIES = "movies_fragment"
        private const val FRAGMENT_TV_SHOWS = "tv_shows_fragment"
        private const val FRAGMENT_PERSONS = "persons_fragment"
    }
}
