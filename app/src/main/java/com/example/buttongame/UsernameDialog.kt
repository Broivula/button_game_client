package com.example.buttongame

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.jakewharton.rxbinding.widget.RxTextView
import org.jetbrains.anko.image
import org.jetbrains.anko.support.v4.runOnUiThread
import java.lang.IllegalStateException
import java.util.concurrent.TimeUnit

class UsernameDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val layoutView = inflater.inflate(R.layout.username_dialog_layout, null)
            layoutView.findViewById<Button>(R.id.dialog_submit_button).setOnClickListener {
                Log.d(LOG, "pressed the button, yo!")
                Networking.checkIfUsernameAvailable()
                //dismiss()
            }


            val correctAnim = layoutView.findViewById<ImageView>(R.id.correct_anim)
            val loadingAnim = layoutView.findViewById<ProgressBar>(R.id.username_dialog_progress_bar)
            var drawable = correctAnim.drawable


            loadingAnim.alpha = 0f

            val editTextField = layoutView.findViewById<EditText>(R.id.username_dialog_edittext)
            RxTextView.textChanges(editTextField).debounce(1, TimeUnit.SECONDS).subscribe{_ ->
                if(editTextField.text.count() > 0){
                    Log.d(LOG, "typing stopped, query search.")
                   // layoutView.findViewById<ProgressBar>(R.id.username_dialog_progress_bar).alpha = 1f
                    runOnUiThread {
                        correctAnim.setImageResource(R.drawable.avd_done_animation)
                        (correctAnim.drawable as AnimatedVectorDrawable).start()
                    }




                }

            }

            builder.setView(layoutView)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.window?.setWindowAnimations(R.style.UserCreationDialogTheme)
    }
}