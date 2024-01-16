package com.adiandrodev.filmadda.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.adiandrodev.filmadda.BuildConfig
import com.adiandrodev.filmadda.MyApplication.Companion.PERSON_ID_KEY
import com.adiandrodev.filmadda.R
import com.adiandrodev.filmadda.data.model.PeopleList
import com.adiandrodev.filmadda.databinding.FragmentPeopleBinding
import com.adiandrodev.filmadda.di.Injector
import com.adiandrodev.filmadda.presentation.activity.BaseActivity
import com.adiandrodev.filmadda.presentation.activity.PersonActivity
import com.adiandrodev.filmadda.presentation.adapter.PeopleRecyclerAdapter
import com.adiandrodev.filmadda.presentation.viewmodel.PopPeopleViewModel
import com.adiandrodev.filmadda.presentation.viewmodel.PopPeopleViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class PeopleFragment : Fragment() {

    private lateinit var binding: FragmentPeopleBinding

    @Inject
    lateinit var viewModelFactory: PopPeopleViewModelFactory
    private lateinit var viewModel: PopPeopleViewModel

    private var lastPageLoaded = 1
    private var totalPages = 0

    private lateinit var layoutManager: GridLayoutManager
    private lateinit var adapter: PeopleRecyclerAdapter
    private lateinit var peopleList: PeopleList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_people, container, false)
        (requireActivity().application as Injector).createPopPeopleSubComponent().inject(this)
        viewModel = viewModelFactory.create(PopPeopleViewModel::class.java)

        if ((requireActivity() as BaseActivity).isNetworkConnected()) {
            loadFirstPageData()
        } else {
            (requireActivity() as BaseActivity).showErrorDialog()
        }

        loadSubsequentPages()

        return binding.root
    }

    private fun loadSubsequentPages() {
        binding.popularPeopleRv.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = recyclerView.adapter?.itemCount ?: 0

                if (lastVisibleItemPosition == totalItemCount - 1) {
                    if (lastPageLoaded < totalPages) {
                        binding.progressBar.visibility = View.VISIBLE
                        lastPageLoaded += 1
                        if ((requireActivity() as BaseActivity).isNetworkConnected()) {
                            viewModel.getPopularPeople(lastPageLoaded).observe(viewLifecycleOwner) { list ->
                                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                                    delay(1000)
                                    list?.let { adapter.addList(it) }
                                    binding.progressBar.visibility = View.GONE
                                }
                            }
                        } else {
                            (requireActivity() as BaseActivity).showErrorDialog()
                        }
                    }
                }
            }
        })
    }

    private fun loadFirstPageData() {
        viewModel.getPopularPeople(lastPageLoaded).observe(viewLifecycleOwner) { list ->
            list?.total_pages?.let {
                totalPages = it
            }
            list?.let {
                peopleList = it
                adapter = PeopleRecyclerAdapter(it, { personId ->
                    val intent = Intent(activity, PersonActivity::class.java)
                    intent.putExtra(PERSON_ID_KEY, personId)
                    startActivity(intent)
                }, { imagePath, view ->
                    loadAndSetImageUsingGlide(imagePath, R.drawable.custom_poster, view)
                })
                layoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
                binding.popularPeopleRv.layoutManager = layoutManager
                binding.popularPeopleRv.adapter = adapter
                binding.shimmerLayout.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.popularPeopleRv.visibility = View.VISIBLE

            }
        }
    }

    private fun loadAndSetImageUsingGlide(imagePath: String, customDrawable: Int, imageView: ImageView, imageBaseUrl: String = BuildConfig.IMAGE_URL) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            try {
                if (imagePath.isNotEmpty()) {
                    if (imagePath[0] == '/') {
                        imagePath.removeRange(0, 0)
                    }
                    Glide.with(this@PeopleFragment)
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
}