package com.mobillium.simplemovies.ui.view.activities

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mobillium.simplemovies.R
import com.mobillium.simplemovies.databinding.ActivityMainBinding
import com.mobillium.simplemovies.ui.viewmodel.MainViewModel
import com.mobillium.simplemovies.util.ConnectivityReceiver

class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    var navController: NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment?
        navController = navHostFragment!!.navController
        navController!!.navigate(R.id.homeFragment);
        binding.lifecycleOwner = this
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.vm = mainViewModel




        lifecycle.addObserver(mainViewModel)
    }


    override fun onResume() {
        super.onResume()
        this.registerReceiver(
            ConnectivityReceiver(), IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION
            )
        )
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (!isConnected) {
            binding.includeNoInternet.root.visibility = View.VISIBLE;
            binding.fragment.visibility = View.GONE
        } else {
            binding.includeNoInternet.root.visibility = View.GONE;
            binding.fragment.visibility = View.VISIBLE
        }
    }
}