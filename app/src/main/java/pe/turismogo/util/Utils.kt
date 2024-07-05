package pe.turismogo.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object Utils {

    @SuppressLint("NewApi")
    fun calculateAge(dateOfBirth: String): Int {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse(dateOfBirth, formatter)
        val hoy = LocalDate.now()

        val period = Period.between(date, hoy)
        return period.years
    }

    @SuppressLint("NewApi")
    fun calculateDaysBetween(startDate: String, endDate: String): Int {
        val formatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val startLocalDate = LocalDate.parse(startDate, formatter)
        val endLocalDate = LocalDate.parse(endDate, formatter)
        val days = ChronoUnit.DAYS.between(startLocalDate, endLocalDate).toInt()

        return if(days == 0) return 1 else days
    }

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