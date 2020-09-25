package com.ks.flowerrecognition.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.Volley
import com.ks.flowerrecognition.activities.FlowerDescActivity
import com.ks.flowerrecognition.R
import com.ks.flowerrecognition.entities.Flower
import com.ks.flowerrecognition.helper.RequestHandler
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

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
        private val root = itemView
        private val flowerImage: ImageView = itemView.findViewById(R.id.cardImage)
        private val flowerTitle: TextView = itemView.findViewById(R.id.flower_title)

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

            RequestHandler(
                "http://192.168.1.5:8000",
                Volley.newRequestQueue(root.context)
            ).getPhotosByFlowerId(card.id) {
                if ((it["images"] as JSONArray).length() > 0) {
                    Picasso.Builder(root.context).build()
                        .load("http://192.168.1.5:8000" + ((it["images"] as JSONArray)[0] as JSONObject)["url"] as String)
                        .fit()
                        .centerCrop()
                        .into(flowerImage)
                }
            }
        }
    }
}