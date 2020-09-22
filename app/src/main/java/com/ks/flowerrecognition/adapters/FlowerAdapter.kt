package com.ks.flowerrecognition.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ks.flowerrecognition.activities.FlowerDescActivity
import com.ks.flowerrecognition.R
import com.ks.flowerrecognition.entities.Flower

class FlowerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<Flower> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)

        return FlowerViewHolder(root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FlowerViewHolder -> {
                holder.bind(items.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(flowerList: ArrayList<Flower>) {
        items = flowerList
    }

    fun addItem(f: Flower) {
        items.add(f)
    }

    class FlowerViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val root = itemView
        val flowerImage = itemView.findViewById<ImageView>(R.id.cardImage)
        val flowerTitle = itemView.findViewById<TextView>(R.id.flower_title)

        fun bind(card: Flower) {
            flowerTitle.text = card.name
            root.setOnClickListener {
                val intent = Intent(root.context, FlowerDescActivity::class.java).apply {
                    putExtra("title", card.name)
                    putExtra("id", card.id)
                    putExtra("desc", card.desc)
                    putExtra("image", card.image)
                }
                root.context.startActivity(intent)
            }
        }
    }
}