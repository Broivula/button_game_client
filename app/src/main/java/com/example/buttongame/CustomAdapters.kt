package com.example.buttongame

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.buttongame.Activities.GameActivity
import com.example.buttongame.Database.DatabaseObject
import com.example.buttongame.Networking.Networking
import kotlinx.android.synthetic.main.lobby_room_layout.view.*
import kotlin.concurrent.thread

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

class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view)