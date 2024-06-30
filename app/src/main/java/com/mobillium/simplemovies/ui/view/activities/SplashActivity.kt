package com.mobillium.simplemovies.ui.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.mobillium.simplemovies.R


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        try {
            Handler(Looper.getMainLooper()).postDelayed({
                val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(mainIntent)
                overridePendingTransition(R.anim.slide_down, R.anim.slide_down);
                finish()
            }, 6000)
        } catch (e: Exception) {

        }
    }
}