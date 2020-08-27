package com.ks.flowerrecognition

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity(), View.OnClickListener {

    //    var btnCamera: Button? = null
//    var imgView: ImageView? = null
    private val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCamera = findViewById<Button>(R.id.btn)
        val imgView = findViewById<ImageView>(R.id.imgView)

        btnCamera.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        showDialog(0)
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(packageManager)?.also {
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//            }
//        }
//        Toast.makeText(this, "asdasd", Toast.LENGTH_LONG).show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            findViewById<ImageView>(R.id.imgView).setImageBitmap(imageBitmap)
        }
    }

    override fun onCreateDialog(id: Int): Dialog {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater

        builder.setMessage("Как вы хотите загрузить цветок?")
        val root = inflater.inflate(R.layout.dialog_camera, null)
        builder.setView(root)
            // Add action buttons
            .setNegativeButton("cancel",
                DialogInterface.OnClickListener { dialog, id ->
                })
        root.findViewById<ImageView>(R.id.btnCamera).setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
        return builder.create()

    }
}