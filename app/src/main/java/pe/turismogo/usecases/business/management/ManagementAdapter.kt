package pe.turismogo.usecases.business.management

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.turismogo.R
import pe.turismogo.model.domain.User

class ManagementAdapter : RecyclerView.Adapter<ManagementViewHolder>() {

    private var dataSet = listOf<User>()

    fun setData(data: List<User>) {
        dataSet = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagementViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_data_example_3, parent, false)
        return ManagementViewHolder(view)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ManagementViewHolder, position: Int) {
        val user = dataSet[position]
        holder.bind(user)
    }
}