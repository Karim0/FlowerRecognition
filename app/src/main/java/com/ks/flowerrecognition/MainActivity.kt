package com.ks.flowerrecognition

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import java.io.BufferedInputStream


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val REQUEST_CHOOSER = 1

    //    private lateinit var imgView: ImageView
    private lateinit var btnCamera: ImageButton
    private lateinit var btnHistory: ImageButton
    private lateinit var requestHandler: RequestHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivity(Intent(this, SplashScreenActivity::class.java))

        btnCamera = findViewById(R.id.main_btn)
        btnHistory = findViewById(R.id.history_btn)

        btnCamera.setOnClickListener(this)
        btnHistory.setOnClickListener(this)

//        requestHandler = RequestHandler("192.168.1.5:8000", Volley.newRequestQueue(this))
        requestHandler = RequestHandler("http://37.151.172.141:8000", Volley.newRequestQueue(this))
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            (R.id.main_btn) -> {
                val galleryIntent = Intent()
                galleryIntent.type = "image/*"
                galleryIntent.action = Intent.ACTION_PICK

                val cameraIntent =
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                        takePictureIntent.resolveActivity(packageManager)
                    }

                val chooser = Intent(Intent.ACTION_CHOOSER)
                chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent)
                chooser.putExtra(Intent.EXTRA_TITLE, "Выберите способ подгрузки картинки:")


                val intentArray = arrayOf<Intent>(cameraIntent)
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                startActivityForResult(chooser, REQUEST_CHOOSER)
//                requestHandler.getFlowerById(1) { response ->
//                    Log.i("MyLogs", response.toString())
//                }
            }

            (R.id.history_btn) -> {
                startActivity(Intent(this, HistoryActivity::class.java))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CHOOSER -> {
                    try {
                        if (data?.scheme != null) {
                            val bis = BitmapFactory.decodeStream(
                                BufferedInputStream(
                                    contentResolver.openInputStream(data.data!!)!!
                                )
                            )
                            requestHandler.flowerRecognize(
                                bis
                            ) { response -> Log.i("MyLogs", response.toString()) }
                        } else {
                            val imageBitmap = data?.extras?.get("data") as Bitmap
//                            imgView.setImageBitmap(imageBitmap)
                        }

                    } catch (e: Exception) {
                        Log.i("MyLogs", e.toString())
                        Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    }
}