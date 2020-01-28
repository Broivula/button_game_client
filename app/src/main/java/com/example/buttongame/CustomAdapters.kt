package com.example.buttongame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.lobby_room_layout.view.*

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

        holder.view.lobby_card_room_number_textview.text = room.roomNumber.toString()
        holder.view.lobby_card_room_playercount_textview.text = room.playerCount.toString()
        holder.view.lobby_card_current_click_textview.text = room.curClick.toString()
    }
}

class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view)