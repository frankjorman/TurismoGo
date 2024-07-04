package pe.turismogo.usecases.business.profile

import pe.turismogo.R
import pe.turismogo.model.domain.User
import pe.turismogo.usecases.base.FragmentProfile

class AdminProfileFragment : FragmentProfile() {

    override fun updateUserData(user : User) {
        val legalInfo = "${fragContext.getString(R.string.prompt_legal_id)}: ${user.legalId}"
        val cityInfo = "${fragContext.getString(R.string.prompt_city)}: ${user.city}"

        binding.content.profile.optionTitle.text = user.businessName
        binding.content.profile.optionDescription.text = legalInfo

        binding.content.profile.optionDescription2.text = cityInfo
        binding.content.profile.optionDescription3.text = user.email
    }

}