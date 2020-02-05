package com.example.buttongame

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.AnimatedVectorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.RecyclerView
import com.example.buttongame.Activities.GameActivity
import com.example.buttongame.Networking.Score
import kotlinx.android.synthetic.main.activity_game_nameplate.view.*
import kotlinx.android.synthetic.main.lobby_room_layout.view.*
import org.jetbrains.anko.custom.style


// an adapter for the main lobby, displaying the rooms and data in rooms

class MainAdapter (val context: Context, var dataOfRooms : List<RoomData>) : RecyclerView.Adapter<CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val row = layoutInflater.inflate(R.layout.lobby_room_layout, parent, false)
        return CustomViewHolder(row, null, null)
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
        return CustomViewHolder(row, null, null)
    }

    override fun getItemCount(): Int {
        return gameScores.count()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val player = gameScores.get(position)
        holder.username = player.username
        holder.turnHolder = player.turnHolder
        if(player.turnHolder)holder.view.findViewById<RelativeLayout>(R.id.name_plate_parent_layout).background = holder.view.context.getDrawable(R.drawable.lobby_recyclerview_background_turnholder)

        val turnHolderIconAnimationView = holder.view.findViewById<ImageView>(R.id.name_plate_turnholder_imageview)
        if(player.turnHolder){
           (holder.view.findViewById<RelativeLayout>(R.id.name_plate_parent_layout).layoutParams as ConstraintLayout.LayoutParams).setMargins(0, 0, 50, 0)
        }



        val resultIconAnimationView = holder.view.findViewById<ImageView>(R.id.name_plate_result_image_view)
        if(player.didClickWin && player.turnHolder)(resultIconAnimationView.drawable as? AnimatedVectorDrawable)?.start()
        //if(player.turnHolder)(iconAnimationView.drawable as? AnimatedVectorDrawable)?.start()
        holder.view.name_plate_text_view.text = player.username + " "  + player.score
    }
}

class CustomViewHolder(val view: View, var username: String?, var turnHolder:  Boolean?) : RecyclerView.ViewHolder(view)