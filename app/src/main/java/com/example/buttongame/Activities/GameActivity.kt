package com.example.buttongame.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buttongame.*
import com.example.buttongame.Database.DatabaseObject
import com.example.buttongame.Networking.SocketEvent
import com.example.buttongame.Networking.SocketHandler
import com.example.buttongame.Networking.SocketMessage
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val roomNumber : Int = intent.extras?.getInt("roomNumber")!!

        connectToRoom(roomNumber)
    }



    private fun connectToRoom(roomNumber: Int) {
        SocketHandler.establishConnection(
            SocketMessage(
                DatabaseObject.getUsername(),
                TOKEN,
                roomNumber + 1,
                SocketEvent.JOIN_ROOM
            )
        ){
            Log.d(LOG, "received in game activity, works lmao")
            runOnUiThread {
                game_namelist_recycler_view.layoutManager = LinearLayoutManager(this)
                game_namelist_recycler_view.adapter = GameNameListAdapter(this, it!!.scores)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SocketHandler.exitRoom()
    }
}
