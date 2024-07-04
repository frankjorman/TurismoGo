package pe.turismogo.factory

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import java.util.Calendar

class DatePickerFactory  {

    companion object {
        const val TYPE_FWD_DATE = "typeOnlyFwdDate"
        const val TYPE_ANY_DATE = "typeAnyDate"
    }

    fun getPickerDialog(context : Context, type : String, listener : OnDateSetListener) : DatePickerDialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(context, null, year, month, day)
        datePickerDialog.setOnDateSetListener(listener)

        when (type) {
            TYPE_FWD_DATE -> {
                calendar[Calendar.YEAR] = year
                datePickerDialog.datePicker.minDate = calendar.timeInMillis
                return datePickerDialog
            }
            TYPE_ANY_DATE -> {
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month
                calendar[Calendar.DAY_OF_MONTH] = day
                return datePickerDialog
            }
            else -> {
                return datePickerDialog
            }
        }
    }
}