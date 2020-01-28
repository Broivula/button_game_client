package com.example.buttongame

import android.app.ActivityOptions
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.buttongame.Activities.MainActivity
import com.example.buttongame.Database.DatabaseObject
import com.example.buttongame.Networking.Networking
import com.jakewharton.rxbinding.widget.RxTextView
import org.jetbrains.anko.activityManager
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.support.v4.startActivity
import java.lang.IllegalStateException
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

enum class ErrorTextCode{
    TAKEN,
    INVALID,
    LENGTH,
    CLEAR
}

enum class ValidationState{
    WAITING,
    AVAILABLE,
    NOT_AVAILABLE,
    CLEAR
}

class UsernameDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val layoutView = inflater.inflate(R.layout.username_dialog_layout, null)
            val editTextField = layoutView.findViewById<EditText>(R.id.username_dialog_edittext)
            val availabilityAnimation = layoutView.findViewById<ImageView>(R.id.correct_anim)
            val loadingAnim = layoutView.findViewById<ProgressBar>(R.id.username_dialog_progress_bar)
            val submitButton =  layoutView.findViewById<Button>(R.id.dialog_submit_button)
            loadingAnim.alpha = 0f

            // handle the submit button click
            submitButton.isEnabled = false
            submitButton.setOnClickListener {
                submitButton.isEnabled = false
                val name = editTextField.text.toString()

                Networking.networkHandler.postUsernameInfo(name){uid ->
                    DatabaseObject.addUserToDB(uid, name)
                    dismiss()
                }
            }


            // if user types something, handle the animation
            RxTextView.textChanges(editTextField).subscribe{
                if(editTextField.text.count() > 0){
                    runOnUiThread {
                        errorTextHandler(ErrorTextCode.CLEAR)
                        setAvailabilityAnimation(ValidationState.WAITING, availabilityAnimation, loadingAnim, submitButton)
                    }
                }else{
                    setAvailabilityAnimation(ValidationState.CLEAR, availabilityAnimation, loadingAnim, submitButton)
                }
            }

            // another listener to check when user has stopped writing
            RxTextView.textChanges(editTextField).debounce(1, TimeUnit.SECONDS).subscribe{_ ->
                if(editTextField.text.count() > 0){

                    if(UsernameFormChecker.checkName(editTextField.text.toString())){
                        Networking.networkHandler.checkUsernameAvailability(editTextField.text.toString()) { availability ->
                            runOnUiThread {
                                when(availability){
                                    true -> {
                                        setAvailabilityAnimation(ValidationState.AVAILABLE, availabilityAnimation, loadingAnim, submitButton)
                                    }
                                    false -> {
                                        setAvailabilityAnimation(ValidationState.NOT_AVAILABLE, availabilityAnimation, loadingAnim, submitButton)
                                        errorTextHandler(ErrorTextCode.TAKEN)
                                    }
                                }
                            }
                        }
                    }else{
                        // regex failed bra
                        runOnUiThread {
                            setAvailabilityAnimation(ValidationState.NOT_AVAILABLE, availabilityAnimation, loadingAnim, submitButton)
                            if(editTextField.text.length > 2 || editTextField.text.length > 15) errorTextHandler(ErrorTextCode.INVALID)
                            else errorTextHandler(ErrorTextCode.LENGTH)
                        }
                    }


                }

            }

            builder.setView(layoutView)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setAvailabilityAnimation(animState: ValidationState, availabilityAnim: ImageView, loadingAnim: ProgressBar, submitButton: Button){
        submitButton.isEnabled = false
        when(animState){
            ValidationState.WAITING -> {
                // name is being valued, set animations correctly
                availabilityAnim.alpha = 0f
                loadingAnim.alpha = 1f
            }
            ValidationState.AVAILABLE -> {
                // name valued and available, set here
                availabilityAnim.setImageResource(R.drawable.avd_done_animation)
                loadingAnim.alpha = 0f
                availabilityAnim.alpha = 1f
                submitButton.isEnabled = true
            }
            ValidationState.NOT_AVAILABLE -> {
                // name valued and unavailable, set here
                availabilityAnim.setImageResource(R.drawable.avd_incorrect_animation)
                loadingAnim.alpha = 0f
                availabilityAnim.alpha = 1f
            }
            ValidationState.CLEAR -> {
                // clear the animations
                loadingAnim.alpha = 0f
                availabilityAnim.alpha = 0f
            }
        }
        (availabilityAnim.drawable as AnimatedVectorDrawable).start()
    }

    private fun errorTextHandler(errCode: ErrorTextCode){

        val errorText =  dialog.findViewById<TextView>(R.id.username_dialog_error_text)
        runOnUiThread {

            when(errCode){
                ErrorTextCode.INVALID -> {
                    // regex failed, show error code
                    errorText?.text = getString(R.string.username_error_text_fail_regex)

                }
                ErrorTextCode.TAKEN -> {
                    // name taken, show error code
                    errorText?.text = getString(R.string.username_error_text_name_taken)
                }
                ErrorTextCode.LENGTH -> {
                    // name length too long/short
                    errorText?.text = getString(R.string.username_error_text_name_length)
                }
                ErrorTextCode.CLEAR -> {
                    // clear the error text
                    errorText?.text= ""
                }
            }
        }

    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)

    }

    //TODO Currently crashes, because content is sometimes null, has to be fixed later
    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        Thread.sleep(1000)
        val intent = Intent(context, MainActivity::class.java )
        startActivity(intent)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.window?.setWindowAnimations(R.style.UserCreationDialogTheme)
    }

}


class UsernameFormChecker(){

    companion object UsernameFormChecker {

        val legitName = Regex("^(?=.{3,15}\$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])\$")
        fun checkName(name: String) : Boolean{
            return legitName.containsMatchIn(name)
        }
    }
}
