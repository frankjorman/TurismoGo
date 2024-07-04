package pe.turismogo.usecases.business.panel

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import pe.turismogo.R
import pe.turismogo.interfaces.IOnEventPanelClickListener
import pe.turismogo.model.domain.Event
import pe.turismogo.util.Constants

class EventBusinessViewHolder
    (view : View, private val listener : IOnEventPanelClickListener) : RecyclerView.ViewHolder(view) {

    private val context = view.context
    private var tvEventName : MaterialTextView = view.findViewById(R.id.tvItemData1Example1)
    private var tvEventDate : MaterialTextView = view.findViewById(R.id.tvItemData2Example1)
    private val tvEventStatus : MaterialTextView = view.findViewById(R.id.tvItemData3Example1)
    private val btnEdit : MaterialButton = view.findViewById(R.id.btnEdit)
    private val btnDelete : MaterialButton = view.findViewById(R.id.btnDelete)

    fun bind(event: Event) {
        val name = event.title
        val date = event.departureDate
        val status =  if (event.active) {
            context.getString(R.string.prompt_active)
        } else {
            context.getString(R.string.prompt_inactive)
        }

        tvEventName.text = name
        tvEventDate.text = date
        tvEventStatus.text = status

        btnEdit.setOnClickListener {
            listener.onEditClick(event)
        }

        btnDelete.setOnClickListener {
            listener.onDeleteClick(event)
        }
    }
}