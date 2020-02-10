package com.example.buttongame.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buttongame.R
import com.example.buttongame.Dialogs.UsernameDialog

class UserCreationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_creation)
        startUsernameDialog()
    }

    private fun startUsernameDialog(){
        val dialog = UsernameDialog {
            dialogDismissed()
        }
        dialog.enterTransition = R.anim.slide_in_left
        dialog.exitTransition = R.anim.slide_out_left
        dialog.isCancelable = false

        dialog.show(supportFragmentManager, "username_dialog")
    }

    fun dialogDismissed(){
        val intent = Intent(baseContext, MainActivity::class.java )
        Thread.sleep(1000)
        startActivity(intent)
    }

    override fun finish() {
        super.finish()
    }
}

