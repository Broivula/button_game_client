package com.example.buttongame.Networking

enum class SocketEvent {
    SEND_CLICK,
    JOIN_ROOM,
    END_TURN,
    EXIT_ROOM
}
// the base for the socket message class, will be modified further later
data class SocketMessage (val username: String, val token: String, val roomNumber: Int?, val event: SocketEvent)
data class GameStateSocketMessage(val roomNumber: Int, val clickAmount: Int, val scores: List<Score>)
data class Score(val username: String, val score: Int, val didClickWin: Boolean, val turnHolder: Boolean)