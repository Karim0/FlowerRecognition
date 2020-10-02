package com.ks.flowerrecognition.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ks.flowerrecognition.R
import java.lang.Exception

class SplashScreenActivity : AppCompatActivity() {
    private var handler: Handler = Handler()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        findViewById<CheckBox>(R.id.splash_chbox).setOnCheckedChangeListener { _, p1 ->
//            applicationContext.getSharedPreferences("isSplash", (if (p1) 1 else 0).toInt())
            intent.putExtra("isSplash", !p1)
            setResult(RESULT_OK, intent);
        }

        handler.postDelayed({
            finish()
        }, 3000)

    }
}