package pe.turismogo.usecases.base

import android.content.Context
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import pe.turismogo.R
import pe.turismogo.model.domain.User
import pe.turismogo.util.Constants

abstract class ActivityCreateAccount : ActivityBase() {

    abstract fun navigateToAccountMainMenu(context : Context) //funcion que debe ser implementada

    fun createUserAccount(user : User, view : View) {
        FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    user.id = it.result?.user?.uid.toString()
                    saveUser(user, view)
                } else {
                    Constants.showSnackBar(view, getString(R.string.error_account_creation_failed))
                }
            }
            .addOnFailureListener {
                Constants.showSnackBar(view, getString(R.string.error_account_creation_failed))
            }
    }

    private fun saveUser(user : User, view : View) {
        FirebaseDatabase
            .getInstance()
            .getReference(Constants.DATABASE_USERS)
            .child(user.id)
            .setValue(user)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    Constants.showSnackBar(view, getString(R.string.success_account_creation))
                    navigateToAccountMainMenu(context)
                } else {
                    Constants.showSnackBar(view, getString(R.string.error_account_creation_failed))
                }
            }
    }
}

