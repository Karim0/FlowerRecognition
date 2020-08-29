package com.ks.flowerrecognition

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Entities.FlowerAdapter
import com.ks.flowerrecognition.entities.Flower
import kotlinx.android.synthetic.main.activity_history.*
import java.util.*
import kotlin.collections.ArrayList

class HistoryActivity : AppCompatActivity() {
    private lateinit var flowerAdapter: FlowerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            flowerAdapter = FlowerAdapter()
            recycler_view.adapter = flowerAdapter
        }

        repeat(10) {
            flowerAdapter.addItem(Flower(it, "Flower $it", "", ""))
        }

    }
}