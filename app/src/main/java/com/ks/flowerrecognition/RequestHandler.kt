package com.ks.flowerrecognition

import android.graphics.Bitmap
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class RequestHandler(
    private val URL: String,
    private val queue: RequestQueue
) {

    fun getFlowerById(id: Int, response: Response.Listener<JSONObject>) {
        val myJS = JSONObject()

        val url = URL + "/flower-api/flower/${id}/"
        val req = JsonObjectRequest(Request.Method.GET, url, myJS,
            response, {
                Log.d("ServerError", "Server didn't response!!")
            })

        queue.add(req)
    }

    fun getAllFlowers(response: Response.Listener<JSONArray>) {
        val myJS = JSONArray()

        val url = "$URL/flower-api/flower/"
        val req = JsonArrayRequest(Request.Method.GET, url, myJS, response, {
            Log.d("ServerError", "Server didn't response!!")
        })

        queue.add(req)
    }

    fun flowerRecognize(f: Bitmap, response: Response.Listener<JSONObject>) {
        val myJS = JSONObject()
        val stream = ByteArrayOutputStream()
        f.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()
        f.recycle()

        myJS.put("file", byteArray)

        val url = "$URL/flower-api/model_predict/"
        val req = JsonObjectRequest(Request.Method.PUT, url, myJS, response, {
            Log.d("ServerError", "Server didn't response!!")
        })

        queue.add(req)
    }


}