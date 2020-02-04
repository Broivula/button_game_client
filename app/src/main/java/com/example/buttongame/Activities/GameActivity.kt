package com.example.buttongame.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buttongame.*
import com.example.buttongame.Database.DatabaseObject
import com.example.buttongame.Networking.SocketEvent
import com.example.buttongame.Networking.SocketHandler
import com.example.buttongame.Networking.SocketMessage
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {

    private var playerScore : Int? = null
    private var roomNumber: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        roomNumber = intent.extras?.getInt("roomNumber")!!


        game_click_button.isEnabled = false
        game_click_button.setOnClickListener {
            // add stuff when clicked
            SocketHandler.sendClick(roomNumber!! + 1, playerScore)
        }

        game_namelist_recycler_view.addItemDecoration(RecyclerviewItemDecoration(7))

        connectToRoom(roomNumber!!)
    }



    private fun connectToRoom(roomNumber: Int) {
        SocketHandler.establishConnection(
            SocketMessage(
                DatabaseObject.getUsername(),
                TOKEN,
                roomNumber + 1,
                SocketEvent.JOIN_ROOM,
                null
            )
        ){
            Log.d(LOG, "received in game activity, works lmao")
            runOnUiThread {
                game_namelist_recycler_view.layoutManager = LinearLayoutManager(this)
                game_namelist_recycler_view.adapter = GameNameListAdapter(this, it!!.scores)
                game_click_button.isEnabled = it.myTurn
                playerScore = it.myScore

                if(playerScore!! <= 0){
                    gameOver()
                }
            }
        }
    }

    private fun gameOver() {
        val dialog = GameOverDialog(roomNumber!! + 1)
        dialog.enterTransition = R.anim.slide_in_up
        dialog.exitTransition = R.anim.anim_fade_out
        dialog.isCancelable = false
        dialog.show(supportFragmentManager, "username_dialog")
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        // remove the super call from this
        // was causing crashing on re-enter.
        // besides, we're not saving anything to the bundle, so all good.
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SocketHandler.exitRoom()
    }
}
