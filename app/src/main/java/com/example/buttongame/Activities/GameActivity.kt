package com.example.buttongame.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buttongame.*
import com.example.buttongame.Database.DatabaseObject
import com.example.buttongame.Networking.Networking
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val roomNumber : Int = intent.extras?.getInt("roomNumber")!!

        thread { connectToRoom(roomNumber) }
    }



    private fun connectToRoom(roomNumber: Int) = runBlocking{
        Networking.establishConnection(SocketMessage(DatabaseObject.getUsername(), TOKEN, roomNumber + 1, SocketEvent.JOIN_ROOM))
    }
}
