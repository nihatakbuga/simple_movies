package com.mobillium.simplemovies.ui.viewmodel

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import com.mobillium.simplemovies.util.ConnectivityReceiver


class MainViewModel(
    application: Application,
) : AndroidViewModel(application), LifecycleObserver {

    init {
        application.applicationContext.registerReceiver(
            ConnectivityReceiver(), IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION
            )
        )
    }
}