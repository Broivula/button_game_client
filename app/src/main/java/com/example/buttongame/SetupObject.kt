package com.example.buttongame

import android.util.Log
import com.example.buttongame.Networking.Networking
import kotlinx.coroutines.runBlocking

object SetupObject {

    fun setup() = runBlocking {
        Log.d("KIKKEL", "setup object starting..")
        Networking.establishConnection()
    }
}