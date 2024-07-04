package pe.turismogo.usecases.base

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import pe.turismogo.util.Constants

abstract class ActivityBase : AppCompatActivity() {
    val context : Context
        get() = this

    val activity : android.app.Activity
        get() = this

    abstract fun setDependencies()
    abstract fun setClickEvents()
    abstract fun setInputEvents()

    abstract fun validateInputs() : Boolean //funcion que debe ser implementada

    //funcion que permite validar si la entrada de texto es vacia o nula, agregando un mensaje de error
    fun validateInput(tiet : TextInputEditText, tilValue : TextInputLayout, messageError : String) : Boolean {
        val editable = tiet.editableText
        Log.d(Constants.TAG_COMMON, "editable text: $editable");

        return if (editable.isNullOrEmpty() || editable.isBlank()) {
            Log.d(Constants.TAG_COMMON, "invalid input")
            tilValue.error = messageError
            false
        } else {
            Log.d(Constants.TAG_COMMON, "valid input")
            true
        }
    }
}