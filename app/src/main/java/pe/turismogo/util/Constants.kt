package pe.turismogo.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class Constants {

    companion object {
        const val ONE_SEC : Long = 1000L


        fun showToast(context : Context, message : String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun showSnackBar(layout: View?, message: String?) {
            val snackBar = Snackbar
                .make(
                    layout!!,
                    message!!,
                    Snackbar.LENGTH_LONG
                )
            snackBar.show()
        }
    }

}