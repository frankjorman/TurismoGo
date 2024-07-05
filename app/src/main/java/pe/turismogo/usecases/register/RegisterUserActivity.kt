package pe.turismogo.usecases.register

import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import pe.turismogo.R
import pe.turismogo.data.AuthManager
import pe.turismogo.data.DatabaseManager
import pe.turismogo.model.domain.User
import pe.turismogo.observable.auth.AuthObserver
import pe.turismogo.observable.rtdatabase.DatabaseObserver
import pe.turismogo.usecases.base.UserDataActivity
import pe.turismogo.util.Constants
import pe.turismogo.util.Navigation
import pe.turismogo.util.Utils

class RegisterUserActivity :
    UserDataActivity(),
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
        val user : User = User()
        user.name = binding.etNames.editableText.toString()
        user.lastname = binding.etLastNames.editableText.toString()
        user.documentId = binding.etDocumentId.editableText.toString()
        user.email = binding.etEmailRegister.editableText.toString()
        user.password = binding.etPasswordRegister.editableText.toString()
        user.dateOfBirth = binding.etDateOfBirth.editableText.toString()
        binding.etPhone.editableText.let { user.phone = it.toString() }

        showWaitDialog()
        DatabaseManager.getInstance().getAuth().createUserAccount(user)
    }

    override fun setMenuConditions() {
        binding.btnCreateUserAccount.isEnabled = false

        binding.cbTermsAndConditions.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> binding.btnCreateUserAccount.isEnabled = true
                false -> binding.btnCreateUserAccount.isEnabled = false
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
            Navigation.toUserHomeMenu(context)
        } else {
            Utils.showSnackBar(binding.root, getString(R.string.error_account_creation_failed))
        }
    }
}