package com.ks.flowerrecognition.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.ks.flowerrecognition.R

class FlowerDescActivity : AppCompatActivity() {

    private lateinit var titleView: TextView
    private lateinit var descView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flower_desc)

        val id = intent.getIntExtra("id", -1)
        val title = intent?.getStringExtra("title")
        val desc = intent?.getStringExtra("desc")
        val img = intent?.getStringExtra("image")

        titleView = findViewById(R.id.desc_flower_title)
        descView = findViewById(R.id.desc_flower_desc)

        titleView.text = title
        descView.text = desc
    }
}