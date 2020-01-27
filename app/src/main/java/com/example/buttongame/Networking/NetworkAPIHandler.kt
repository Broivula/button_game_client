package com.example.buttongame.Networking

import android.util.Log
import com.example.buttongame.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.io.IOException

class NetworkAPIHandler {

    private val client = OkHttpClient()
    val API_URL = BuildConfig.SERVER_URL

    fun checkUsernameAvailability(username: String, callback: (availability: Boolean) -> Unit) {
        val data = JSONObject()
        data.accumulate("username", username)
        val json = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(json, data.toString())
        val request = Request.Builder()
            .url(API_URL + USERNAME_AVAIL_ENDPOINT)
            .addHeader(AUTH, TOKEN)
            .post(body)
            .build()

        doAsync {
            try {
                val response = client.newCall(request).execute()

                Log.d(LOG, "response code: ${response.code() == 200}")
                val responseCode = response.code()
                if(responseCode == 200){
                    val parsedResponse = JSONObject(response.body()?.string()!!)
                    val usernameAvailability = parsedResponse.getBoolean("username_available")
                    callback(usernameAvailability)
                }

            }catch (e: IOException){
                Log.d(LOG, "error: ${e.toString()}")
            }
        }
    }


    fun postUsernameInfo(username: String, callback: (uid: String) -> Unit){
        val data = JSONObject()
        data.accumulate("username", username)
        val json = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(json, data.toString())
        val request = Request.Builder()
            .url(API_URL + ADD_USER_ENDPOINT)
            .addHeader(AUTH, TOKEN)
            .post(body)
            .build()

        doAsync {
            try {
                val response = client.newCall(request).execute()

                Log.d(LOG, "response code: ${response.code() == 200}")
                val responseCode = response.code()
                if(responseCode == 200){
                    val parsedResponse = JSONObject(response.body()?.string()!!)
                    val uid = parsedResponse.getString("uid")
                    callback(uid)
                }

            }catch (e: IOException){
                Log.d(LOG, "error: ${e.toString()}")
            }
        }
    }
}