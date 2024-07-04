package pe.turismogo.assets

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

open class InputTextWatcher
    (val layout: TextInputLayout? = null , val editText : TextInputEditText? = null) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /*NOP*/ }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        layout?.error = null
        editText?.error = null
    }

    override fun afterTextChanged(s: Editable?) { /*NOP*/ }
}