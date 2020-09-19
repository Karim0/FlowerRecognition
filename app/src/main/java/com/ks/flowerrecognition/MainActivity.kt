package com.ks.flowerrecognition

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.ks.flowerrecognition.entities.Flower
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val REQUEST_CHOOSER = 1

    //    private lateinit var imgView: ImageView
    private lateinit var btnCamera: ImageButton
    private lateinit var btnHistory: ImageButton
    private lateinit var requestHandler: RequestHandler
    private lateinit var dialog: ProgressDialog
    private lateinit var db: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivity(Intent(this, SplashScreenActivity::class.java))

        btnCamera = findViewById(R.id.main_btn)
        btnHistory = findViewById(R.id.history_btn)

        btnCamera.setOnClickListener(this)
        btnHistory.setOnClickListener(this)

//        requestHandler = RequestHandler("192.168.1.5:8000", Volley.newRequestQueue(this))
        requestHandler = RequestHandler("http://192.168.1.5:8000", Volley.newRequestQueue(this))
        dialog = ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT)
        dialog.setTitle("Sending photo")
        dialog.setMessage("Loading")


        db = Database(this)
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
                        dialog.show()
                        if (data?.scheme != null) {
                            val bis = BitmapFactory.decodeStream(
                                BufferedInputStream(
                                    contentResolver.openInputStream(data.data!!)!!
                                )
                            )
//                            Toast.makeText(this, "photo taken", Toast.LENGTH_LONG).show()

//                            val dialog = ProgressDialog.show(this, "Title", "Loading")
                            requestHandler.flowerRecognize(
                                bis,
                                { response ->
                                    try {
                                        val id =
                                            JSONObject(String(response.data))["flower_id"] as Int
                                        requestHandler.getFlowerById(id) {
                                            val intent =
                                                Intent(this, FlowerDescActivity::class.java).apply {
                                                    putExtra("title", it["name"].toString())
                                                    putExtra("id", id)
                                                    putExtra("desc", it["desc"].toString())
                                                    putExtra("image", "")
                                                }

                                            startActivity(intent)
                                            db.addFlower(
                                                Flower(
                                                    id,
                                                    it["name"].toString(),
                                                    it["desc"].toString(),
                                                    ""
                                                )
                                            )
                                            dialog.hide()
                                        }
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }, this
                            )

                        } else {
                            val imageBitmap = data?.extras?.get("data") as Bitmap
                            requestHandler.flowerRecognize(
                                imageBitmap,
                                { response ->
                                    try {
                                        val id =
                                            JSONObject(String(response.data))["flower_id"] as Int
                                        requestHandler.getFlowerById(id) {
                                            val intent =
                                                Intent(this, FlowerDescActivity::class.java).apply {
                                                    putExtra("title", it["name"].toString())
                                                    putExtra("id", id)
                                                    putExtra("desc", it["desc"].toString())
                                                    putExtra("image", "")
                                                }

                                            startActivity(intent)
                                            db.addFlower(
                                                Flower(
                                                    id,
                                                    it["name"].toString(),
                                                    it["desc"].toString(),
                                                    ""
                                                )
                                            )
                                            dialog.hide()
                                        }

                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }, this
                            )
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