package com.mobillium.simplemovies.ui.view.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.mobillium.simplemovies.R
import com.mobillium.simplemovies.adapters.SliderAdapter
import com.mobillium.simplemovies.adapters.UpcomingAdapter
import com.mobillium.simplemovies.databinding.FragmentHomeBinding
import com.mobillium.simplemovies.response.playing.PlayingResult
import com.mobillium.simplemovies.response.upcoming.Result
import com.mobillium.simplemovies.ui.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    var navController: NavController? = null
    lateinit var adapterImageSlider: SliderAdapter
    private var runnable: Runnable? = null
    private val handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.vm = homeViewModel
        lifecycle.addObserver(homeViewModel)
//        activityListBinding.mainProgress.root.visibility = View.VISIBLE
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment?
        navController = navHostFragment!!.navController

        binding.swipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            homeViewModel.getData()
        })
        
        homeViewModel.showNowPlayingProgress.observe(requireActivity()) {
            if (it) {
                binding.includeProgress.root.visibility = View.VISIBLE
            } else {
                binding.includeProgress.root.visibility = View.GONE
                binding.swipeRefresh.isRefreshing = false
            }
        }
        homeViewModel.showUpcomingProgress.observe(requireActivity()) {
            if (it) {
                binding.includeProgress.root.visibility = View.VISIBLE
            } else {
                binding.includeProgress.root.visibility = View.GONE
                binding.swipeRefresh.isRefreshing = false

            }
        }
        homeViewModel.errorUpcomingMessage.observe(requireActivity()) {
            if (it.isNotEmpty()) {
                binding.includeOops.root.visibility = View.VISIBLE
                binding.includeProgress.root.visibility = View.GONE
                binding.includeOops.oopsDesc.text = it;
            } else {
                binding.includeOops.root.visibility = View.GONE
                binding.includeProgress.root.visibility = View.GONE
                binding.swipeRefresh.isRefreshing = false

            }
        }

        homeViewModel.errorNowPlayingMessage.observe(requireActivity()) {
            if (it.isNotEmpty()) {
                binding.includeOops.root.visibility = View.VISIBLE
                binding.includeProgress.root.visibility = View.GONE
                binding.includeOops.oopsDesc.text = it;
            } else {
                binding.includeOops.root.visibility = View.GONE
                binding.includeProgress.root.visibility = View.GONE
                binding.swipeRefresh.isRefreshing = false

            }
        }
        homeViewModel.nowPlayingResponse.observe(requireActivity()) {
            initComponent(it.results)
        }
        homeViewModel.upComingResponse.observe(requireActivity()) {
            setUpcomingList(it.results)
        }
        return binding.root
    }

    private fun setUpcomingList(result: List<Result>) {
        val adapterCallList = navController?.let {
            UpcomingAdapter(
                requireActivity(),
                it,
                result,
                layoutInflater,
                R.layout.item_upcoming_list
            )
        }
        val layoutMessageListManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.upcomingList.layoutManager = layoutMessageListManager
        binding.upcomingList.adapter = adapterCallList

    }


    private fun initComponent(result: List<PlayingResult>) {
        binding.nestedScrollView.visibility = View.VISIBLE
        adapterImageSlider = SliderAdapter(requireActivity(), navController, result)
        adapterImageSlider.setItems(result)
        binding.pager.adapter = adapterImageSlider
        // displaying selected image first
        binding.pager.currentItem = 0
        addBottomDots(binding.layoutDots, adapterImageSlider.count, 0)
        binding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                pos: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(pos: Int) {
                addBottomDots(binding.layoutDots, adapterImageSlider.count, pos)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        startAutoSlider(adapterImageSlider.count)
    }

    private fun addBottomDots(layout_dots: LinearLayout, size: Int, current: Int) {
        val dots = arrayOfNulls<ImageView>(size)
        layout_dots.removeAllViews()
        for (i in dots.indices) {
            dots[i] = ImageView(requireActivity())
            val width_height = 15
            val params =
                LinearLayout.LayoutParams(ViewGroup.LayoutParams(width_height, width_height))
            params.setMargins(10, 10, 10, 10)
            dots[i]!!.layoutParams = params
            dots[i]!!.setImageResource(R.drawable.shape_circle_outline)
            layout_dots.addView(dots[i])
        }
        if (dots.isNotEmpty()) {
            dots[current]!!.setImageResource(R.drawable.shape_circle)
        }
    }

    private fun startAutoSlider(count: Int) {
        runnable = Runnable {
            var pos: Int = binding.pager.currentItem
            pos += 1
            if (pos >= count) pos = 0
            binding.pager.currentItem = pos
            runnable?.let { handler.postDelayed(it, 3000) }
        }
        handler.postDelayed(runnable!!, 3000)
    }

    override fun onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable!!)
        super.onDestroy()
    }

}