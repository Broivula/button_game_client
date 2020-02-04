package com.example.buttongame.Networking

import android.provider.ContactsContract
import android.util.Log
import com.example.buttongame.Database.DatabaseObject
import com.example.buttongame.LOG
import com.example.buttongame.TOKEN
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.io.*
import java.net.Socket
import kotlin.concurrent.thread



object SocketHandler {

    var  socket : Socket? = null
    val networkHandler = NetworkAPIHandler()
    var socketCommunicator : PrintStream? = null

    fun establishConnection(msg: SocketMessage, callback : (GameStateSocketMessage?) -> Unit) = doAsync{
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
                thread { setListener(callback) }
                setPrinter(msg)

            }else{
                Log.d(LOG, "connection to server failed, try again later")
            }



        }catch (e: IOException){
            Log.d(LOG, "error establishin socket connection. error msg: $e")
        }
    }

    // receive the incoming data and send it further to be parsed
    private fun setListener(callback: (GameStateSocketMessage?) -> Unit){
        val reader = socket!!.getInputStream().bufferedReader()
        val iterator = reader.lineSequence().iterator()
        while(iterator.hasNext()) {
            val line = iterator.next()
            val msg = SocketMessageParser.parse(line)
            Log.d(LOG, "msg incoming: $msg")
            callback(msg)
        }
    }

    private fun setPrinter(msg: SocketMessage){
        socketCommunicator = PrintStream(socket!!.getOutputStream() ,true)
        sendData(msg)
    }

    // function to communicate between the client and the server. Will be called whenever necessary.

    private fun sendData(msg: SocketMessage) = doAsync{
        Log.d(LOG, "$msg")
        val socketMessage = """{
            |"username":"${msg.username}",
            |"roomNumber":${msg.roomNumber},
            |"token":"${msg.token}",
            |"event":"${msg.event}",
            |"playerScore":${msg.playerScore}
            |}""".trimMargin()
        socketCommunicator?.println(socketMessage)
    }

    fun sendClick(roomNumber: Int, playerScore: Int?) = doAsync{
        sendData(
            SocketMessage(
                DatabaseObject.getUsername(),
                TOKEN,
                roomNumber,
                SocketEvent.SEND_CLICK,
                playerScore
            )
        )
    }

    fun newGame(roomNumber: Int) = doAsync {
        sendData(
            SocketMessage(
                DatabaseObject.getUsername(),
                TOKEN,
                roomNumber,
                SocketEvent.NEW_GAME,
                null
            )
        )
    }

    fun exitRoom() = doAsync {
        sendData(
            SocketMessage(
                DatabaseObject.getUsername(),
                TOKEN,
                null,
                SocketEvent.EXIT_ROOM,
                null
            )
        )
    }

}
