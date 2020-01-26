package com.example.buttongame

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment

class UserCreationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_creation)
        startUsernameDialog()
    }

    private fun startUsernameDialog(){
        val dialog = UsernameDialog()
        dialog.enterTransition = R.anim.slide_in_left
        dialog.exitTransition = R.anim.slide_out_left
        dialog.isCancelable = false
        Log.d(LOG, "slide anim: ${dialog.enterTransition}")

        dialog.show(supportFragmentManager, "test dialog")

    }
}
