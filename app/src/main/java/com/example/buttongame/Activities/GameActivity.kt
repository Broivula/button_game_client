package com.example.buttongame.Activities

import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buttongame.*
import com.example.buttongame.Database.DatabaseObject
import com.example.buttongame.Dialogs.GameOverDialog
import com.example.buttongame.Dialogs.ServerErrorDialog
import com.example.buttongame.Networking.GameStateSocketMessage
import com.example.buttongame.Networking.SocketEvent
import com.example.buttongame.Networking.SocketHandler
import com.example.buttongame.Networking.SocketMessage
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {

    private var playerScore : Int? = null
    private var roomNumber: Int? = null
    private var turnHolderPlayer : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        roomNumber = intent.extras?.getInt("roomNumber")!!



        enableButtons(false)
        game_click_button.setOnClickListener {
            // add stuff when clicked
            SocketHandler.sendClick(roomNumber!! + 1, playerScore)
            it.isEnabled = false
            game_click_button.background = getDrawable(R.drawable.game_click_button_disabled)
        }

        game_end_turn_button.setOnClickListener {
            SocketHandler.endTurn(roomNumber!! + 1)
        }

        game_namelist_recycler_view.addItemDecoration(RecyclerviewItemDecoration(7))
        establishConnectionAndListener(roomNumber!!)




    }



    private fun establishConnectionAndListener(roomNumber: Int) {
        SocketHandler.establishConnection(
            SocketMessage(
                DatabaseObject.getUsername(),
                TOKEN,
                roomNumber + 1,
                SocketEvent.JOIN_ROOM,
                null
            )
        ){
            if(it?.statusCode == 200)updateGameUI(it?.msg!!)
            else serverError(it?.err!!)

        }
    }

    private fun updateGameUI(gameState: GameStateSocketMessage){
        runOnUiThread {
            game_namelist_recycler_view.layoutManager = LinearLayoutManager(this)
            game_namelist_recycler_view.adapter = GameNameListAdapter(this, gameState.scores)
            game_click_button.isEnabled = gameState.myTurn
            playerScore = gameState.myScore
            game_to_next_win_textview.text = (10 -(gameState.clickAmount % 10)).toString()


            // to handle the arrow animation, we have to do this whole thing


            enableButtons(gameState.myTurn)
            if(playerScore!! <= 0){
                gameOver()
            }
        }
    }

    private fun enableButtons(state: Boolean){
        if(state){
            game_click_button.isEnabled = true
            game_end_turn_button.isEnabled = true
            game_end_turn_button.background = getDrawable(R.drawable.game_click_button_background)
            game_click_button.background = getDrawable(R.drawable.game_click_button_background)
        }else{

            game_click_button.isEnabled = false
            game_end_turn_button.isEnabled = false
            game_end_turn_button.background = getDrawable(R.drawable.game_click_button_disabled)
            game_click_button.background = getDrawable(R.drawable.game_click_button_disabled)
        }
    }

    private fun serverError(errorMsg: String){

        val dialog = ServerErrorDialog(errorMsg)
        dialog.isCancelable = false
        dialog.show(supportFragmentManager, "server_error")
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
