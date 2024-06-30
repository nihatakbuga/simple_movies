package com.mobillium.simplemovies.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bumptech.glide.Glide
import com.mobillium.simplemovies.R
import com.mobillium.simplemovies.databinding.FragmentDetailBinding
import com.mobillium.simplemovies.ui.viewmodel.DetailViewModel


class DetailFragment : Fragment() {
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: FragmentDetailBinding
    var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        binding.vm = detailViewModel
        lifecycle.addObserver(detailViewModel)

        val bundle = requireArguments()
        val movieId = bundle.getString("MOVIE_ID") ?: ""
        detailViewModel.getDetailMovie(movieId)

        //Swipe Refresh
        binding.swipeDetail.setOnRefreshListener(OnRefreshListener {
            detailViewModel.getDetailMovie(movieId)
        })

        detailViewModel.showDetailProgress.observe(requireActivity()) {
            if (it) {
                binding.includeProgress.root.visibility = View.VISIBLE
            } else {
                binding.includeProgress.root.visibility = View.GONE
                binding.swipeDetail.isRefreshing = false

            }
        }
        detailViewModel.errorDetailMessage.observe(requireActivity()) {
            if (it.isNotEmpty()) {
                binding.includeOops.root.visibility = View.VISIBLE
                binding.includeProgress.root.visibility = View.GONE
                binding.includeOops.oopsDesc.text = it;
            } else {
                binding.includeOops.root.visibility = View.GONE
                binding.swipeDetail.isRefreshing = false
                binding.includeProgress.root.visibility = View.GONE
            }
        }

        detailViewModel.detailResponse.observe(requireActivity()) {
            if (it != null) {
                binding.swipeDetail.visibility = View.VISIBLE
                val url: String =
                    "http://image.tmdb.org/t/p/w500" + it.backdrop_path
                Glide.with(requireActivity())
                    .load(url)
                    .placeholder(R.drawable.oops)
                    .dontAnimate().into(binding.includeMovieDetail.detailImage);
                binding.includeMovieDetail.detailDate.text = it.release_date
                binding.includeMovieDetail.detailTitle.text = it.original_title
                binding.includeMovieDetail.detailDesc.text = it.overview
                binding.includeMovieDetail.voteAverage.text = it.vote_average.toString()
                binding.includeMovieDetail.voteCount.text = it.vote_count.toString()
            }
        }
        return binding.root
    }

}