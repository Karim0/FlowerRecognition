package com.ks.flowerrecognition.activities

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.ks.flowerrecognition.R
import com.ks.flowerrecognition.helper.RequestHandler
import com.squareup.picasso.*
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.min

class FlowerDescActivity : AppCompatActivity(), Target {

    private lateinit var titleView: TextView
    private lateinit var descView: TextView
    private lateinit var carouselView: CarouselView
    private lateinit var request: RequestHandler

    private lateinit var images: ArrayList<Bitmap?>

    private var imageListener =
        ImageListener { position, imageView -> imageView.setImageBitmap(images[position]) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flower_desc)

        val id = intent.getIntExtra("id", -1)
        val title = intent?.getStringExtra("title")
        val desc = intent?.getStringExtra("desc")

        titleView = findViewById(R.id.desc_flower_title)
        descView = findViewById(R.id.desc_flower_desc)

        titleView.text = title
        descView.text = desc

        request = RequestHandler("http://192.168.1.5:8000", Volley.newRequestQueue(this))

        images = ArrayList()
        carouselView = findViewById(R.id.pager_images)

        request.getPhotosByFlowerId(id) {
            val res_arr = it["images"] as JSONArray
            for (i in (0 until min(res_arr.length(), 5))) {
                Picasso.Builder(this).build()
                    .load("http://192.168.1.5:8000" + (res_arr[i] as JSONObject)["url"] as String)
                    .into(this)
            }
        }
        carouselView.setImageListener(imageListener)
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?) {
        try {
            images.add(bitmap)
            carouselView.pageCount = images.size
        } catch (e: Exception) {
            Log.e("myLogs", e.message!!)
        }
    }

    override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
    }


}
