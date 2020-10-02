package com.ks.flowerrecognition.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.ks.flowerrecognition.R
import com.ks.flowerrecognition.database.Database
import com.ks.flowerrecognition.entities.Flower
import com.ks.flowerrecognition.helper.RequestHandler
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val REQUEST_CHOOSER = 1
    private val SPASH_RESULT = 2
    private var mCurrentPhotoPath = ""

    private lateinit var btnCamera: ImageButton
    private lateinit var btnHistory: ImageButton
    private lateinit var requestHandler: RequestHandler
    private lateinit var dialog: ProgressDialog
    private lateinit var db: Database
    private lateinit var adBanner: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val s = getPreferences(MODE_PRIVATE)
            if (s.getBoolean("isSplash", true)) startActivityForResult(
                Intent(
                    this,
                    SplashScreenActivity::class.java
                ), SPASH_RESULT
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        btnCamera = findViewById(R.id.main_btn)
        btnHistory = findViewById(R.id.history_btn)

        adBanner = findViewById(R.id.adView)

        btnCamera.setOnClickListener(this)
        btnHistory.setOnClickListener(this)

//        requestHandler = RequestHandler("192.168.1.5:8000", Volley.newRequestQueue(this))
        requestHandler = RequestHandler("http://192.168.1.5:8000", Volley.newRequestQueue(this))
        dialog = ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT)
        dialog.setTitle("Sending photo")
        dialog.setMessage("Loading")

        db = Database(this)

        showBanner()
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
                        takePictureIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024000)
                    }
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (ex: Exception) {

                }

                if (photoFile != null) {
                    val photoURI = Uri.fromFile(photoFile)
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                }


                val chooser = Intent(Intent.ACTION_CHOOSER)
                chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent)
                chooser.putExtra(Intent.EXTRA_TITLE, "Выберите способ подгрузки картинки:")


                val intentArray = arrayOf<Intent>(cameraIntent)
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                startActivityForResult(chooser, REQUEST_CHOOSER)
            }

            (R.id.history_btn) -> {
                startActivity(Intent(this, HistoryActivity::class.java))
            }
        }
    }

    @SuppressLint("CommitPrefEdits")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CHOOSER -> {
                    try {
                        dialog.show()
                        if (data?.scheme != null) {
                            val bis = Bitmap.createScaledBitmap(
                                BitmapFactory.decodeStream(
                                    BufferedInputStream(
                                        contentResolver.openInputStream(
                                            data.data!!
                                        )!!
                                    )
                                ), 310, 310, true
                            )

                            requestHandler.flowerRecognize(
                                bis,
                                { response ->
                                    try {
                                        val id =
                                            JSONObject(String(response.data))["flower_id"] as Int
                                        requestHandler.getFlowerById(id) {
                                            val intent =
                                                Intent(
                                                    this,
                                                    FlowerDescActivity::class.java
                                                ).apply {
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
//                            val file: File = File(mCurrentPhotoPath)
                            val imageBitmap = Bitmap.createScaledBitmap(
                                BitmapFactory.decodeFile(mCurrentPhotoPath),
                                310,
                                310,
                                true
                            )
                            Log.i("myLogs", imageBitmap.width.toString())

                            requestHandler.flowerRecognize(
                                imageBitmap,
                                { response ->
                                    try {
                                        val id =
                                            JSONObject(String(response.data))["flower_id"] as Int
                                        requestHandler.getFlowerById(id) {
                                            val intent =
                                                Intent(
                                                    this,
                                                    FlowerDescActivity::class.java
                                                ).apply {
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

        if (requestCode == SPASH_RESULT && resultCode == RESULT_OK) {
            val s = getPreferences(MODE_PRIVATE)
            with(s.edit()) {
                data?.getBooleanExtra("isSplash", true)?.let {
                    putBoolean("isSplash", it)
                    apply()
                }
            }
//            data?.data.get
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image: File = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )

        mCurrentPhotoPath = image.getAbsolutePath()
        return image
    }


    private fun showBanner() {
        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        adBanner.loadAd(adRequest)
    }
}