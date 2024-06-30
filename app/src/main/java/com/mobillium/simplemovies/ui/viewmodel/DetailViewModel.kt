package com.mobillium.simplemovies.ui.viewmodel

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import com.mobillium.simplemovies.api.ApiClient
import com.mobillium.simplemovies.repository.MovieDetailRepository
import com.mobillium.simplemovies.util.ConnectivityReceiver


class DetailViewModel(
    application: Application,
) : AndroidViewModel(application), LifecycleObserver {

    private val apiClient = ApiClient(application.applicationContext)
    private val detailRepository = MovieDetailRepository(apiClient);

    val detailResponse = detailRepository.liveDataMovieDetail
    val showDetailProgress = detailRepository.showProgress
    val errorDetailMessage = detailRepository.errorMessage

    fun getDetailMovie(movieId: String) {
        detailRepository.getMovieDetailList(movieId)
    }
    init {
        application.applicationContext.registerReceiver(
            ConnectivityReceiver(), IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION
            )
        )
    }


}