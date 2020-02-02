package com.example.buttongame

enum class SocketEvent {
    SEND_CLICK,
    JOIN_ROOM,
    END_TURN
}
// the base for the socket message class, will be modified further later
data class SocketMessage (val username: String, val token: String, val roomNumber: Int, val event: SocketEvent )