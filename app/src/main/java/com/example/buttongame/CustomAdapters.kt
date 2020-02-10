package com.example.buttongame

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.buttongame.Activities.GameActivity
import com.example.buttongame.Networking.RoomData
import com.example.buttongame.Networking.Score
import kotlinx.android.synthetic.main.activity_game_nameplate.view.*
import kotlinx.android.synthetic.main.lobby_room_layout.view.*


// an adapter for the main lobby, displaying the rooms and data in rooms

class MainAdapter (val context: Context, var dataOfRooms : List<RoomData>) : RecyclerView.Adapter<CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val row = layoutInflater.inflate(R.layout.lobby_room_layout, parent, false)
        return CustomViewHolder(row)
    }

    override fun getItemCount(): Int {
        return dataOfRooms.count()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val room = dataOfRooms.get(position)
        holder.view.lobby_card_room_number_textview.text = holder.view.context.getString(R.string.lobby_card_room_title, room.roomNumber.toString())
        holder.view.lobby_card_room_playercount_textview.text = holder.view.context.getString(R.string.lobby_card_room_occupants, room.playerCount.toString())
        holder.view.lobby_card_current_click_textview.text = holder.view.context.getString(R.string.lobby_card_room_current_score, room.curClick.toString())
        holder.view.lobby_card_join_button.setOnClickListener {
            // try to join the room
            val intent = Intent(holder.view.context, GameActivity::class.java)
            intent.putExtra("roomNumber", position)
            holder.view.context.startActivity(intent)
        }
    }
}

// An adapter for the recyclerview in game -activity, displaying names and scores of the players

class GameNameListAdapter (val context: Context, var gameScores : List<Score>) : RecyclerView.Adapter<CustomViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val row = layoutInflater.inflate(R.layout.activity_game_nameplate, parent, false)
        return CustomViewHolder(row)
    }

    override fun getItemCount(): Int {
        return gameScores.count()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val player = gameScores.get(position)

        if(player.turnHolder){
            holder.view.findViewById<RelativeLayout>(R.id.name_plate_parent_layout).background = holder.view.context.getDrawable(R.drawable.lobby_recyclerview_background_turnholder)
           (holder.view.findViewById<RelativeLayout>(R.id.name_plate_parent_layout).layoutParams as ConstraintLayout.LayoutParams).setMargins(50, 0, 0, 0)
        }
        if(player.didClickWin && player.turnHolder){
            val amountView = holder.view.findViewById<TextView>(R.id.name_plate_amount_gained)
            val anim = AnimationUtils.loadAnimation(context, R.anim.anim_slide_up_all_the_way)
            amountView.text = "+" + player.amountWon.toString()
            anim.reset()
            anim.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    amountView.text = "   "
                }

                override fun onAnimationStart(animation: Animation?) {
                }
            })
            amountView.startAnimation(anim)

        }


 //       val resultIconAnimationView = holder.view.findViewById<ImageView>(R.id.name_plate_result_image_view)
     //   if(player.didClickWin && player.turnHolder)(resultIconAnimationView.drawable as? AnimatedVectorDrawable)?.start()

        holder.view.name_plate_username.text = player.username
        holder.view.name_plate_score.text = player.score.toString()
    }
}

class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view)

