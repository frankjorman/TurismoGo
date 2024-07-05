package pe.turismogo.usecases.register

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import pe.turismogo.R
import pe.turismogo.assets.InputTextWatcher
import pe.turismogo.data.DatabaseManager
import pe.turismogo.databinding.ActivityRegisterBusinessBinding
import pe.turismogo.model.domain.User
import pe.turismogo.observable.auth.AuthObserver
import pe.turismogo.observable.rtdatabase.DatabaseObserver
import pe.turismogo.usecases.base.BusinessDataActivity
import pe.turismogo.util.Constants
import pe.turismogo.util.Enums
import pe.turismogo.util.Navigation
import pe.turismogo.util.Utils

class RegisterBusinessActivity :
    BusinessDataActivity(),
    AuthObserver.AuthCreation,
    DatabaseObserver.EventInsertObserver {

    override fun onResume() {
        super.onResume()
        DatabaseManager.getInstance().getAuth().addAuthCreationObserver(this)
        DatabaseManager.getInstance().addEventInsertObservable(this)
    }

    override fun onPause() {
        super.onPause()
        DatabaseManager.getInstance().getAuth().removeAuthCreationObserver(this)
        DatabaseManager.getInstance().removeEventInsertObservable(this)
    }

    override fun isPasswordValid(password: String): Boolean {
        return (password.length < 8)
    }

    override fun actionButton() {
        val user = User()
        user.businessName = binding.etBusinessName.editableText.toString()
        user.legalId = binding.etLegalId.editableText.toString()
        user.city = binding.etCity.editableText.toString()
        user.email = binding.etEmailRegister.editableText.toString()
        user.password = binding.etPasswordRegister.editableText.toString()
        user.role = Enums.Role.BUSINESS.name

        binding.etNamesLastnames.editableText.let {
            if(it.contains(Constants.SPACE)) {
                val names = it.split(Constants.SPACE)
                user.name = names[0]
                user.lastname = names[1]
            } else {
                user.name = it.toString()
            }
        }
        binding.etBusinessRole.editableText.let { user.businessRole = it.toString() }
        binding.etPhone.editableText.let { user.phone = it.toString() }

        DatabaseManager.getInstance().getAuth().createUserAccount(user)
    }

    override fun setMenuConditions() {
        binding.btnCreateBusinessAccount.isEnabled = false //se deshabilita el boton de creacion de cuenta

        //se agrega un listener para revisar cuando el check box esta o no seleccionado
        binding.cbTermsAndConditions.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked) {
                true -> binding.btnCreateBusinessAccount.isEnabled = true
                false -> binding.btnCreateBusinessAccount.isEnabled = false
            }
        }
    }

    override fun notifyAuthCreationObservers(isSuccessful: Boolean, message: Any?) {
        if(!isSuccessful) {
            hideWaitDialog()
            when(message) {
                is FirebaseAuthInvalidCredentialsException -> {
                    Utils.showSnackBar(binding.root, context.getString(R.string.error_invalid_credentials))
                }
                is FirebaseAuthEmailException -> {
                    Utils.showSnackBar(binding.root, context.getString(R.string.error_email_invalid))
                }
                else -> {
                    Utils.showSnackBar(binding.root, getString(R.string.error_account_creation_failed))
                }
            }
        }
    }

    override fun notifyEventInsertObservers(isSuccessful: Boolean) {
        hideWaitDialog()
        if(isSuccessful) {
            Utils.showToast(context, getString(R.string.success_account_creation))
            Navigation.toAdminHomeMenu(context)
        } else {
            Utils.showSnackBar(binding.root, getString(R.string.error_account_creation_failed))
        }
    }
}