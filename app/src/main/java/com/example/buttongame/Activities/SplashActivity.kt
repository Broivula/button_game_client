package com.example.buttongame.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.buttongame.Database.DatabaseObject
import com.example.buttongame.LOG

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // this activity takes care of checking if user already exists or not.

        // first, let's initiate the database
        DatabaseObject.initDatabase(this)


        val dat = DatabaseObject.getUserData()
        navigate(dat?.count())

    }

    private fun navigate(option: Int?){
        var intent : Intent? = null
       when(option){
           1 -> {
               // user data was found, open main menu
                intent = Intent(this, MainActivity::class.java )
           }
           0 -> {
               // user data was not found, open user creation menu
               intent = Intent(this, UserCreationActivity::class.java )
           }
           else -> {
               // error reading database data
           }
       }
        startActivity(intent)
        finish()
    }

}
