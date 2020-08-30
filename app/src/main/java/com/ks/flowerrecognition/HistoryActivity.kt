package com.ks.flowerrecognition

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ks.flowerrecognition.adapters.FlowerAdapter
import com.ks.flowerrecognition.entities.Flower
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

        repeat(10) {
            flowerAdapter.addItem(Flower(it, "Flower $it",
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.", ""))
        }

    }
}