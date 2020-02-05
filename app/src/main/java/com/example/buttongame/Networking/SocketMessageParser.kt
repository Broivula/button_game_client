package com.example.buttongame.Networking

import android.util.Log
import com.example.buttongame.Database.DatabaseObject
import com.example.buttongame.LOG
import org.json.JSONObject

class SocketMessageParser {
    companion object Parser {
        fun parse(line: String) : GameStateSocketMessage?{
            val parsed = JSONObject(line)
            val statusCode = parsed.getInt("status");
            if(statusCode == 200){
                var myScore: Int? = null
                val scoreList = mutableListOf<Score>()
                val content = parsed.getString("msg")
                val contentParsed = JSONObject(content)
                val scores = contentParsed.getJSONArray("scores")
                val players = contentParsed.getJSONArray("players")
                val turnHolder = contentParsed.getInt("turnHolder")
                val didClickWin = contentParsed.getBoolean("didClickWin")
                val myTurn = players[turnHolder] == DatabaseObject.getUsername()
                val playerArr = mutableListOf<String>()
                // for the recyclerview
                for(i in 0 until players.length()){
                    playerArr.add(players.getString(i))
                }
                Log.d(LOG, players.toString())
                for(i in 0 until scores.length()){
                    val user = JSONObject(scores.getString(i))
                    val name = user.getString("username")
                    val points = user.getInt("score")
                    val playersTurn = players[turnHolder] == name
                    if(DatabaseObject.getUsername() == name) myScore = points
                    scoreList.add(Score(name, points, didClickWin, playersTurn))
                }

                val sortedList = playerArr.map {name ->
                     scoreList.filter{score-> (score.username == name)}[0]
                }


                // for rest of the activity layout
                val roomNumber = contentParsed.getInt("roomNumber")
                val clickAmount = contentParsed.getInt("clickAmount")

                return GameStateSocketMessage(roomNumber, clickAmount, sortedList, myTurn, myScore)
            }
            else{
                // if the statuscode was something else, implement
                // some error handling here, toast msg or something
                return null
            }

        }
    }
}