package pe.turismogo.usecases.business.management

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import pe.turismogo.R
import pe.turismogo.model.domain.User
import pe.turismogo.util.Constants

class ManagementViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    private val context = view.context
    private val tvUserName : MaterialTextView = view.findViewById(R.id.tvItemData1Example3)
    private val tvUserAge : MaterialTextView = view.findViewById(R.id.tvItemData2Example3)
    private val tvUserPhone : MaterialTextView = view.findViewById(R.id.tvItemData3Example3)

    fun bind(user : User) {
        val name = "${user.name} ${user.lastname}"
        val age = Constants.calculateAge(user.dateOfBirth)
        val phone = user.phone

        tvUserName.text = name
        tvUserAge.text = age.toString()
        tvUserPhone.text = user.phone
    }

}