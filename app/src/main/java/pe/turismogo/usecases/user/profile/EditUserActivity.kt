package pe.turismogo.usecases.user.profile

import android.util.Log
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import pe.turismogo.R
import pe.turismogo.data.DatabaseManager
import pe.turismogo.model.domain.User
import pe.turismogo.model.session.UserSingleton
import pe.turismogo.observable.rtdatabase.DatabaseObserver
import pe.turismogo.usecases.base.UserDataActivity
import pe.turismogo.util.Constants
import pe.turismogo.util.Navigation
import pe.turismogo.util.Utils

class EditUserActivity :
    UserDataActivity(),
    DatabaseObserver.EventInsertObserver{

    override fun onResume() {
        super.onResume()
        DatabaseManager.getInstance().addEventInsertObservable(this)
    }

    override fun onPause() {
        super.onPause()
        DatabaseManager.getInstance().removeEventInsertObservable(this)
    }

    override fun isPasswordValid(password: String): Boolean {
        return true
    }

    override fun actionButton() {
        Log.d(Constants.TAG_COMMON, "actionButton - EditUser pressed")
        UserSingleton.getInstance().getUser()?.let { user ->
            user.name = binding.etNames.editableText.toString()
            user.lastname = binding.etLastNames.editableText.toString()
            user.documentId = binding.etDocumentId.editableText.toString()
            user.dateOfBirth = binding.etDateOfBirth.editableText.toString()
            binding.etPhone.editableText.let { user.phone = it.toString() }

            showWaitDialog()
            DatabaseManager.getInstance().updateUser(user)
        }
    }

    override fun setMenuConditions() {
        binding.llTermsConditions.visibility = View.GONE
        binding.tilPasswordRegister.visibility = View.GONE
        binding.tilEmailRegister.isEnabled = false

        binding.tvCreateBusinessTitle.text = context.getString(R.string.prompt_edit_user)
        binding.btnCreateUserAccount.text = context.getString(R.string.action_save)
        binding.btnCreateUserAccount.icon = AppCompatResources.getDrawable(context, R.drawable.icon_save)

        UserSingleton.getInstance().getUser()?.let { user ->
            binding.etNames.setText(user.name)
            binding.etLastNames.setText(user.lastname)
            binding.etDocumentId.setText(user.documentId)
            binding.etEmailRegister.setText(user.email)
            binding.etDateOfBirth.setText(user.dateOfBirth)
            binding.etPhone.setText(user.phone)
        }

    }

    override fun notifyEventInsertObservers(isSuccessful: Boolean) {
        hideWaitDialog()
        if(isSuccessful) {
            Utils.showToast(context, getString(R.string.success_update_user))
            finish()
        } else {
            Utils.showSnackBar(binding.root, getString(R.string.error_update_user))
        }
    }
}