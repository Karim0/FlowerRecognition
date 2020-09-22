package com.ks.flowerrecognition.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.ks.flowerrecognition.R

class SplashScreenActivity : AppCompatActivity() {
    private var handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        handler.postDelayed({
            finish()
        }, 3000)
    }
}