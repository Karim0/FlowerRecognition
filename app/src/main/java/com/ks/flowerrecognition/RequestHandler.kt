package com.ks.flowerrecognition

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject

class RequestHandler(private val URL: String,
                    private val queue: RequestQueue) {

    fun getFlowerById(id: Int): JSONObject {
        var myJS = JSONObject()
//        myJS.put("id", id)
        val url = URL+"/flower-api/flower/{${id}}/"
        val req = JsonObjectRequest(Request.Method.GET, url, myJS,
            Response.Listener<JSONObject> { response ->
                run {
                    myJS = response
                }
            }, Response.ErrorListener{
                Log.d("ServerError", "Server didn't response!!")
            })

        queue.add(req)

        return myJS
    }

    fun getAllFlowers(): JSONArray {
        var myJS = JSONArray()
        val url = "$URL/flower-api/flower/"
        val req = JsonArrayRequest(Request.Method.GET, url, myJS,
            Response.Listener<JSONArray> { response ->
                run {
                    myJS = response
                }
            }, Response.ErrorListener{
                Log.d("ServerError", "Server didn't response!!")
            })

        queue.add(req)

        return myJS
    }



}