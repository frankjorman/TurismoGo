package pe.turismogo.usecases.business.profile

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import pe.turismogo.R
import pe.turismogo.data.DatabaseManager
import pe.turismogo.model.domain.User
import pe.turismogo.model.session.UserSingleton
import pe.turismogo.observable.rtdatabase.DatabaseObserver
import pe.turismogo.usecases.base.BusinessDataActivity
import pe.turismogo.util.Constants
import pe.turismogo.util.Enums
import pe.turismogo.util.Utils

class BusinessEditActivity :
    BusinessDataActivity(),
    DatabaseObserver.EventInsertObserver {

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
        UserSingleton.getInstance().getUser()?.let { user ->
            showWaitDialog()
            user.businessName = binding.etBusinessName.editableText.toString()
            user.legalId = binding.etLegalId.editableText.toString()
            user.city = binding.etCity.editableText.toString()

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

            DatabaseManager.getInstance().updateUser(user)
        }
    }

    override fun setMenuConditions() {
        binding.llTermsConditions.visibility = View.GONE
        binding.tilPasswordRegister.visibility = View.GONE
        binding.tilEmailRegister.isEnabled = false

        binding.tvCreateBusinessTitle.text = context.getString(R.string.prompt_edit_user)
        binding.btnCreateBusinessAccount.text = context.getString(R.string.action_save)
        binding.btnCreateBusinessAccount.icon = AppCompatResources.getDrawable(context, R.drawable.icon_save)

        UserSingleton.getInstance().getUser()?.let { user ->
            binding.etBusinessName.setText(user.businessName)
            binding.etLegalId.setText(user.legalId)
            binding.etCity.setText(user.city)
            binding.etEmailRegister.setText(user.email)
            val names = "${user.name} ${user.lastname}"
            if(names.isNotEmpty() && names.isNotBlank()) binding.etNamesLastnames.setText(names)
            binding.etBusinessRole.setText(user.businessRole)
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