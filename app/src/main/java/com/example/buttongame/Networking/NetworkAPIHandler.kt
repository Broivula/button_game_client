package com.example.buttongame.Networking

import android.util.Log
import com.example.buttongame.*
import okhttp3.*
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.io.IOException



class NetworkAPIHandler {

    private val client = OkHttpClient()
    private val REST_URL = "http://" + BuildConfig.SERVER_URL +":3003"

    fun checkUsernameAvailability(username: String, callback: (availability: Boolean) -> Unit) {
        val data = JSONObject()
        data.accumulate("username", username)
        val json = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(json, data.toString())
        val request = Request.Builder()
            .url(REST_URL + USERNAME_AVAIL_ENDPOINT)
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
            .url(REST_URL + ADD_USER_ENDPOINT)
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


    fun getRoomData(callback: (List<RoomData>) -> Unit) {

        val roomList : MutableList<RoomData> = mutableListOf()
        val request = Request.Builder()
            .url(REST_URL + GET_ROOM_DATA_ENDPOINT)
            .addHeader(AUTH, TOKEN)
            .build()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d(LOG, "getting room data failed")
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.code() == 200){


                    val parsedArray = CustomParser.parse(response.body()?.string()!!)
                    for(room in parsedArray){
                        val parsedRoom = JSONObject(room)
                        roomList.add(
                            RoomData(
                                parsedRoom.getInt("roomNumber"),
                                parsedRoom.getInt("playerCount"),
                                parsedRoom.getInt("curClick")
                            )
                        )
                    }
                    callback(roomList.toList())
                }
            }
        })
    }
}

class CustomParser{
    companion object ArrayParser{
        fun parse(string: String) : List<String>{
            val unwanted_char_list : List<Char> = listOf('[', ']')
            var s : String = ""
            val s_list : MutableList<String> = mutableListOf()
            for (c in string){
                // we don't want these
                if(!unwanted_char_list.contains(c)){
                    s = s+c
                }
                if(c == '}'){
                    if(s[0] == ',')s_list.add(s.replaceFirst(',',' '))
                    else s_list.add(s)
                    s = ""
                }
            }
            return s_list.toList()
        }
    }
}