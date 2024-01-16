package com.adiandrodev.filmadda.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.adiandrodev.filmadda.BuildConfig
import com.adiandrodev.filmadda.R
import com.adiandrodev.filmadda.databinding.FragmentIntroBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val INTRO_TAG = "intro_tag"
private const val BACKDROP_PATH = "backdrop_path"

class IntroFragment : Fragment() {

    private var intro: String? = null
    private var backdropPath: String? = null

    private lateinit var binding: FragmentIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            intro = it.getString(INTRO_TAG)
            backdropPath = it.getString(BACKDROP_PATH)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_intro, container, false)

        binding.introTv.text = intro
        backdropPath?.let {
            loadAndSetImageUsingGlide(
                it,
                R.drawable.custom_poster,
                binding.introBackdropIv
            )
        }
        return binding.root
    }

    private fun loadAndSetImageUsingGlide(imagePath: String, customDrawable: Int, imageView: ImageView, imageBaseUrl: String = BuildConfig.IMAGE_URL) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            try {
                if (imagePath.isNotEmpty()) {
                    if (imagePath[0] == '/') {
                        imagePath.removeRange(0, 0)
                    }
                    Glide.with(this@IntroFragment)
                        .load(imageBaseUrl + imagePath)
                        .centerCrop()
                        .placeholder(customDrawable)
                        .into(imageView)
                }
            } catch (e: java.lang.Exception) {
                Log.e("TEST_TAG", "loadAndSetImageUsingGlide: ${e.message}")
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(introTag: String, backdropPath: String) =
            IntroFragment().apply {
                arguments = Bundle().apply {
                    putString(INTRO_TAG, introTag)
                    putString(BACKDROP_PATH, backdropPath)
                }
            }
    }
}