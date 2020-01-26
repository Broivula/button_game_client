package com.example.buttongame

import android.util.Log
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.io.IOException

class NetworkHandler {

    private val client = OkHttpClient()

    fun checkUsernameAvailability() : Boolean{
        val data = JSONObject()
        data.accumulate("username", "northernlion")
        val json = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(json, data.toString())
        val request = Request.Builder()
            .url("http://192.168.8.100:3000/get/check_user_availability")
            .addHeader(AUTH, "proof_of_concept_token_for_button_game")
            .post(body)
            .build()

        doAsync {
            try {
                val response = client.newCall(request).execute()
                Log.d(LOG, response.body()?.string()!!)
            }catch (e: IOException){
                Log.d(LOG, "error: ${e.toString()}")
            }
        }

        return true
    }
}