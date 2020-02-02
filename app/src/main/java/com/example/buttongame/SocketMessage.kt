package com.example.buttongame

enum class SocketEvent {
    SEND_CLICK,
    JOIN_ROOM,
    END_TURN,
    EXIT_ROOM
}
// the base for the socket message class, will be modified further later
data class SocketMessage (val username: String, val token: String, val roomNumber: Int?, val event: SocketEvent )