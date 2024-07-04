package pe.turismogo.usecases.business.panel

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.turismogo.R
import pe.turismogo.interfaces.IOnEventPanelClickListener
import pe.turismogo.model.domain.Event
import pe.turismogo.util.Constants
import kotlin.Int


class EventBusinessAdapter
    (private val listener : IOnEventPanelClickListener) :
    RecyclerView.Adapter<EventBusinessViewHolder>() {

    private var dataSet = listOf<Event>()

    fun setData(data: List<Event>) {
        dataSet = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventBusinessViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_data_example_1, parent, false)
        return EventBusinessViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: EventBusinessViewHolder, position: Int) {
        val event = dataSet[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int  = dataSet.size

}