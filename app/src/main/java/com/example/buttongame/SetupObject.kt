package com.example.buttongame

import android.util.Log
import kotlinx.coroutines.runBlocking

object SetupObject {

    fun setup() = runBlocking {
        Log.d("KIKKEL", "setup object starting..")
        Networking.establishConnection()
    }
}