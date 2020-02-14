package com.example.buttongame.Networking

import android.util.Log
import com.example.buttongame.Database.DatabaseObject
import com.example.buttongame.LOG
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.IOException

class SocketMessageParser {
    companion object Parser {
        fun parse(line: String) : ServerSocketResponse?{
            try {
                val parsed = JSONObject(line)
                val statusCode = parsed.getInt("status")
                val content = parsed.getString("msg")
                if (statusCode == 200) {
                    var myScore: Int? = null
                    var sortedList = listOf<Score>()
                    val scoreList = mutableListOf<Score>()
                    val contentParsed = JSONObject(content)
                    val scores = contentParsed.getJSONArray("scores")
                    val players = contentParsed.getJSONArray("players")
                    val turnHolder = contentParsed.getInt("turnHolder")
                    Log.d(LOG, "scores are: $scores")
                    val didClickWin = contentParsed.getBoolean("didClickWin")
                    val myTurn = players[turnHolder] == DatabaseObject.getUsername()
                    val playerArr = mutableListOf<String>()
                    // for the recyclerview
                    for (i in 0 until players.length()) {
                        playerArr.add(players.getString(i))
                    }
                    runBlocking {

                         launch {
                             for (i in 0 until scores.length()) {
                                 val user = JSONObject(scores.getString(i))
                                 val name = user.getString("username")
                                 val points = user.getInt("score")
                                 val playersTurn = players[turnHolder] == name
                                 if (DatabaseObject.getUsername() == name) myScore = points
                                 var amountWon: Int? = null

                                 if (didClickWin) amountWon = contentParsed.getInt("amountWon")

                                 Log.d(
                                     LOG,
                                     "the data: $name, $points, $didClickWin, $playersTurn, $amountWon"
                                 )
                                 scoreList.add(Score(name, points, didClickWin, playersTurn, amountWon))
                             }
                         }.join()


                            Log.d(LOG, "players are: $playerArr")
                            Log.d(LOG, "the score list is: $scoreList")

                             sortedList = playerArr.map { name ->
                                scoreList.filter { score -> (score.username == name) }[0]
                            }

                    }

                    // for rest of the activity layout
                    val roomNumber = contentParsed.getInt("roomNumber")
                    val clickAmount = contentParsed.getInt("clickAmount")

                    return ServerSocketResponse(
                        statusCode,
                        GameStateSocketMessage(
                            roomNumber,
                            clickAmount,
                            sortedList,
                            myTurn,
                            myScore
                        ),
                        null
                    )
                } else {
                    // if the statuscode was something else, implement
                    // some error handling here, toast msg or something
                    val errorMsg = JSONObject(content).getString("errorMsg")
                    return ServerSocketResponse(statusCode, null, errorMsg)
                }

            }catch (e : IOException) {
                Log.d(LOG, "error parsing.. error msg: ")
                Log.d(LOG, e.toString())
                SocketHandler.exitRoom()
                return null
            }
        }
    }
}