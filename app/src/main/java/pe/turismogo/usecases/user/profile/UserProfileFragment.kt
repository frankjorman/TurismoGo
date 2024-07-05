package pe.turismogo.usecases.user.profile

import pe.turismogo.R
import pe.turismogo.model.domain.User
import pe.turismogo.usecases.base.FragmentProfile
import pe.turismogo.util.Navigation


class UserProfileFragment : FragmentProfile() {

    override fun toEditProfile() {
        Navigation.toEditUser(fragContext)
    }

    override fun updateUserData(user : User) {
        val nameInfo = "${user.name} ${user.lastname}"
        val documentInfo = "${fragContext.getString(R.string.prompt_document_id)}: ${user.documentId}"

        binding.content.profile.optionTitle.text = nameInfo
        binding.content.profile.optionDescription.text = documentInfo

        binding.content.profile.optionDescription2.text = user.email
        binding.content.profile.optionDescription3.text = user.phone.ifEmpty {
            fragContext.getString(R.string.prompt_no_phone)
        }
    }
}
