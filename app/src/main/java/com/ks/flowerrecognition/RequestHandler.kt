package com.ks.flowerrecognition

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONException
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

    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun flowerRecognize(
        bitmap: Bitmap, response:  Response.Listener<NetworkResponse>,
        context: Context, ROOT_URL: String = "$URL/flower-api/model_predict/"
    ) {
        val volleyMultipartRequest: VolleyMultipartRequest = object : VolleyMultipartRequest(
            Method.PUT, ROOT_URL, response,
            Response.ErrorListener { error ->
                Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
                Log.e("GotError", "" + error.message)
            }) {
            override fun getByteData(): Map<String, DataPart> {
                val params: MutableMap<String, DataPart> = HashMap()
                val imagename = System.currentTimeMillis()
                params["file"] = DataPart("$imagename.png", getFileDataFromDrawable(bitmap))
                return params
            }
        }
        queue.add(volleyMultipartRequest)
    }


}