package com.mobillium.simplemovies.ui.viewmodel

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.mobillium.simplemovies.api.ApiClient
import com.mobillium.simplemovies.repository.NowPlayingRepository
import com.mobillium.simplemovies.repository.UpcomingRepository
import com.mobillium.simplemovies.util.ConnectivityReceiver


class HomeViewModel(
    application: Application,
) : AndroidViewModel(application), LifecycleObserver {

    private val apiClient = ApiClient(application.applicationContext)
    private val nowPlayingRepository = NowPlayingRepository(apiClient);

    private val upcomingRepository = UpcomingRepository(apiClient);



    val nowPlayingResponse = nowPlayingRepository.liveDataNowPlaying
    val upComingResponse = upcomingRepository.liveDataUpcoming

    val showNowPlayingProgress = nowPlayingRepository.showProgress
    val errorNowPlayingMessage = nowPlayingRepository.errorMessage
    val showUpcomingProgress = upcomingRepository.showProgress
    val errorUpcomingMessage = upcomingRepository.errorMessage

    init {
        application.applicationContext.registerReceiver(
            ConnectivityReceiver(), IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION
            )
        )
        getData()
    }
    fun  getData(){
        nowPlayingRepository.getNowPlayingList()
        upcomingRepository.getUpcomingList()
    }



}