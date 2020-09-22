package com.ks.flowerrecognition.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ks.flowerrecognition.database.Database
import com.ks.flowerrecognition.R
import com.ks.flowerrecognition.adapters.FlowerAdapter
import kotlinx.android.synthetic.main.activity_history.*

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

        val database = Database(this)

        database.allFlowers.iterator().forEach {
            flowerAdapter.addItem(it)
        }

    }
}