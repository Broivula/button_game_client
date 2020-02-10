package com.example.buttongame.Dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.buttongame.Activities.MainActivity
import com.example.buttongame.R
import java.lang.IllegalStateException

class ServerErrorDialog(var errorMsg: String) : AppCompatDialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let{
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val layoutView = inflater.inflate(R.layout.dialog_server_error_layout, null)


            builder.setView(layoutView)

            layoutView.findViewById<TextView>(R.id.error_dialog_text).text = errorMsg

            layoutView.findViewById<Button>(R.id.error_dialog_button).setOnClickListener {
                val intent = Intent(context, MainActivity::class.java )
                startActivity(intent)
            }

            builder.create()

        }?: throw IllegalStateException("Activity cannot be null")
    }
}