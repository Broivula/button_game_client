package com.example.buttongame.Networking

import android.util.Log
import com.example.buttongame.Database.DatabaseObject
import com.example.buttongame.LOG
import kotlinx.coroutines.delay
import java.io.*
import java.net.Socket
import kotlin.concurrent.thread

object Networking {

    var  socket : Socket? = null
    val networkHandler = NetworkAPIHandler()
    var socketCommunicator : PrintStream? = null

     suspend fun establishConnection(){
        try {
            var tries = 0
            socket = Socket("192.168.8.100", 3366)

            Log.d(LOG, "${socket!!.isConnected}")

            while(!socket!!.isConnected && tries < 5){
                socket = Socket("192.168.8.100", 3366)
                delay(1000)
                tries++
            }

            if(socket!!.isConnected){
                thread { setListener() }
                setPrinter()

            }else{
                Log.d(LOG, "connection to server failed, try again later")
            }



        }catch (e: IOException){
            Log.d(LOG, "error establishin socket connection. error msg: $e")
        }
    }

    fun setListener(){
        val reader = socket!!.getInputStream().bufferedReader()
        val iterator = reader.lineSequence().iterator()
        while(iterator.hasNext()) {

            val line = iterator.next()
            Log.d(LOG, line)
        }
    }

    fun setPrinter(){
        socketCommunicator = PrintStream(socket!!.getOutputStream() ,true)
        val username = DatabaseObject.getUsername()
        val data = """{
            |"username": "$username",
            |"roomNumber": "3"
            |}""".trimMargin()
        socketCommunicator?.println(data)
    }

    fun sendData(data: String){
        val username = DatabaseObject.getUsername()
        socketCommunicator?.println(username)
    }

    /*
    fun checkIfUsernameAvailable(){
        networkHandler.checkUsernameAvailability()
    }


     */
}
