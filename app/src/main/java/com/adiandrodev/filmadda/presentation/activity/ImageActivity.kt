package com.adiandrodev.filmadda.presentation.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.adiandrodev.filmadda.BuildConfig
import com.adiandrodev.filmadda.MyApplication.Companion.IMAGE_PATH_KEY
import com.adiandrodev.filmadda.R
import com.adiandrodev.filmadda.databinding.ActivityImageBinding

class ImageActivity : BaseActivity() {

    private lateinit var binding: ActivityImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image)

        binding.backBtn.setOnClickListener { finish() }

        val imagePath = intent.getStringExtra(IMAGE_PATH_KEY)

        imagePath?.let {
            if (super.isNetworkConnected()) {
                loadAndSetImageUsingGlide(it, binding.imageView)
            } else {
                super.showErrorDialog()
            }
        }
    }

    private fun loadAndSetImageUsingGlide(imagePath: String, imageView: ImageView, imageBaseUrl: String = BuildConfig.IMAGE_URL) {
        if (imagePath.isNotEmpty()) {
            if (imagePath[0] == '/') {
                imagePath.removeRange(0, 0)
            }
            Glide.with(this)
                .load(imageBaseUrl + imagePath)
                .into(imageView)
        }
    }
}