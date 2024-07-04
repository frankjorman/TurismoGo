package pe.turismogo.factory

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import pe.turismogo.R

class MessageFactory {

    companion object {
        const val TYPE_ERROR = "typeError"
        const val TYPE_SUCCESS = "typeSuccess"
        const val TYPE_LOAD = "typeLoad"
        const val TYPE_WAIT = "typeWait"
        const val TYPE_ACCEPT_DIALOG = "typeAcceptDialog"
        const val TYPE_CONTINUE_DIALOG = "typeContinueDialog"
        const val TYPE_ACCEPT_CANCEL = "typeOkCancel"
    }

    fun getDialog(context : Context, type : String) : AlertDialog.Builder {
        when(type) {
            TYPE_ERROR -> {
                return AlertDialog.Builder(context)
                    .setMessage(context.getString(R.string.error_fetching_data))
            }
            TYPE_SUCCESS -> {
                return AlertDialog.Builder(context)
                    .setMessage(context.getString(R.string.success_loading_content))
            }
            TYPE_LOAD -> {
                return AlertDialog.Builder(context)
                    .setCancelable(true)
                    .setView(setProgressDialog(context, context.getString(R.string.prompt_loading)))
            }
            TYPE_WAIT -> {
                return AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setView(setProgressDialog(context, context.getString(R.string.prompt_please_wait)))
            }
            TYPE_ACCEPT_DIALOG -> {
                return AlertDialog.Builder(context)
                    .setPositiveButton(context.getString(R.string.action_accept)) { _, _ -> }
            }
            TYPE_CONTINUE_DIALOG -> {
                return AlertDialog.Builder(context)
                    .setPositiveButton(context.getString(R.string.action_continue)) { _, _ -> }
            }
            TYPE_ACCEPT_CANCEL -> {
                return AlertDialog.Builder(context)
                    .setPositiveButton(context.getString(R.string.action_accept)) { _, _ -> }
                    .setNegativeButton(context.getString(R.string.action_cancel)) { _, _ -> }
            }
            else ->
                return AlertDialog.Builder(context)
                    .setMessage(context.getString(R.string.invalid_type))
        }
    }


    // Function to display ProgressBar
    // inside AlertDialog
    private fun setProgressDialog(context : Context, text : String = "") : View {

        // Creating a Linear Layout
        val llPadding = 30
        val ll = LinearLayout(context)

        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER

        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        // Creating a ProgressBar inside the layout
        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam
        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER

        // Creating a TextView inside the layout
        val tvText = TextView(context)
        tvText.text = text
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20f
        tvText.layoutParams = llParam
        ll.addView(progressBar)
        ll.addView(tvText)

        return ll
    }
}