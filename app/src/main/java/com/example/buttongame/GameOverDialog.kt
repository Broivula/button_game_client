package com.example.buttongame

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.buttongame.Networking.SocketHandler
import org.jetbrains.anko.support.v4.runOnUiThread
import java.lang.IllegalStateException

class GameOverDialog(val roomNumber: Int) : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val layoutView = inflater.inflate(R.layout.game_over_dialog, null)

            val tryAgainButton = layoutView.findViewById<Button>(R.id.game_over_try_again_button)
            val exitButton = layoutView.findViewById<Button>(R.id.game_over_exit_button)
            tryAgainButton.setOnClickListener {
                runOnUiThread {
                    SocketHandler.newGame(roomNumber)
                    dismiss()
                }
            }

            exitButton.setOnClickListener {
                 SocketHandler.exitRoom()
                (activity as? Activity)!!.finish()
            }


            builder.setView(layoutView)

            builder.create()

        }?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.window?.setWindowAnimations(R.style.GameOverDialogTheme)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        Thread.sleep(1000)
    }
}