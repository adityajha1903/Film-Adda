package com.adiandrodev.filmadda.presentation.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.adiandrodev.filmadda.R
import com.adiandrodev.filmadda.presentation.fragment.IntroFragment

class IntroPagerAdapter(
    private val context: Context,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val movieBackdropPath: String,
    private val tvBackdropPath: String,
    private val peopleBackdropPath: String,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IntroFragment.newInstance(context.getString(R.string.intro_1), movieBackdropPath)
            1 -> IntroFragment.newInstance(context.getString(R.string.intro_2), tvBackdropPath)
            else -> IntroFragment.newInstance(context.getString(R.string.intro_3), peopleBackdropPath)
        }
    }

}
