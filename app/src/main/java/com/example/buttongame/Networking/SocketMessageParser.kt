package com.example.buttongame.Networking

import android.util.Log
import com.example.buttongame.LOG
import org.json.JSONObject

class SocketMessageParser {
    companion object Parser {
        fun parse(line: String) : GameStateSocketMessage?{
            val parsed = JSONObject(line)
            val statusCode = parsed.getInt("status");
            if(statusCode == 200){
                val scoreList = mutableListOf<Score>()
                val content = parsed.getString("msg")
                val contentParsed = JSONObject(content)
                val scores = contentParsed.getJSONArray("scores")
                val players = contentParsed.getJSONArray("players")
                val turnHolder = contentParsed.getInt("turnHolder")
                val didClickWin = contentParsed.getBoolean("didClickWin")

                // for the recyclerview
                for(i in 0 until scores.length()){
                    val user = JSONObject(scores.getString(i))
                    val name = user.getString("username")
                    val points = user.getInt("score")
                    val playersTurn = players[turnHolder] == name
                    scoreList.add(Score(name, points, didClickWin, playersTurn))
                }

                // for rest of the activity layout
                val roomNumber = contentParsed.getInt("roomNumber")
                val clickAmount = contentParsed.getInt("clickAmount")

                return GameStateSocketMessage(roomNumber, clickAmount, scoreList.toList() )
            }
            else{
                // if the statuscode was something else, implement
                // some error handling here, toast msg or something
                return null
            }

        }
    }
}