package com.example.buttongame.Networking

import android.util.Log
import com.example.buttongame.Database.DatabaseObject
import com.example.buttongame.LOG
import com.example.buttongame.SocketMessage
import com.example.buttongame.TOKEN
import kotlinx.coroutines.delay
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.io.*
import java.net.Socket
import kotlin.concurrent.thread



object Networking {

    var  socket : Socket? = null
    val networkHandler = NetworkAPIHandler()
    var socketCommunicator : PrintStream? = null

    fun establishConnection(msg: SocketMessage) = doAsync{
        try {
            var tries = 0
            socket = Socket("192.168.8.100", 3366)

            Log.d(LOG, "${socket!!.isConnected}")

            while(!socket!!.isConnected && tries < 5){
                socket = Socket("192.168.8.100", 3366)
                Thread.sleep(1000)
                tries++
            }

            if(socket!!.isConnected){
                thread { setListener() }
                setPrinter(msg)

            }else{
                Log.d(LOG, "connection to server failed, try again later")
            }



        }catch (e: IOException){
            Log.d(LOG, "error establishin socket connection. error msg: $e")
        }
    }

    private fun setListener(){
        // testing out the data reader -- so far works !!!
        val reader = socket!!.getInputStream().bufferedReader()
        val iterator = reader.lineSequence().iterator()
        while(iterator.hasNext()) {
            val line = iterator.next()
            val parsed = JSONObject(line)
            val s = parsed.getString("msg")
            val s_parsed = JSONObject(s)
            Log.d(LOG, s_parsed.getInt("clickAmount").toString())
        }
    }

    // function to enter the room. the call is coming from beginning of the game activity.

    private fun setPrinter(msg: SocketMessage){
        socketCommunicator = PrintStream(socket!!.getOutputStream() ,true)
        sendData(msg)
    }

    // function to communicate between the client and the server. Will be called whenever necessary.

    fun sendData(msg: SocketMessage) = doAsync{
        Log.d(LOG, "$msg")
        socketCommunicator?.println("test")
        val socket_message = """{
            |"username":"${msg.username}",
            |"roomNumber":${msg.roomNumber},
            |"token":"${msg.token}",
            |"event":"${msg.event}"
            |}""".trimMargin()
        socketCommunicator?.println(socket_message)
    }

}
