package com.example.buttongame.Networking

enum class SocketEvent {
    SEND_CLICK,
    JOIN_ROOM,
    END_TURN,
    EXIT_ROOM,
    NEW_GAME
}
// the base for the socket message class, will be modified further later
data class SocketMessage (val username: String, val token: String, val roomNumber: Int?, val event: SocketEvent, val playerScore: Int?)
data class ServerSocketResponse(val statusCode: Int, val msg : GameStateSocketMessage?, val err : String?)
data class GameStateSocketMessage(val roomNumber: Int, val clickAmount: Int, val scores: List<Score>, val myTurn : Boolean, val myScore: Int?)
data class Score(val username: String, val score: Int, val didClickWin: Boolean, val turnHolder: Boolean, val amountWon : Int?)