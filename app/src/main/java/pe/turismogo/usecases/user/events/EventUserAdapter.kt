package pe.turismogo.usecases.user.events

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.turismogo.R
import pe.turismogo.interfaces.IOnEventClickListener
import pe.turismogo.model.domain.Event

class EventUserAdapter
    (private val listener : IOnEventClickListener)
    : RecyclerView.Adapter<EventUserViewHolder>() {

    private var dataSet = listOf<Event>()

    fun setData(data: List<Event>) {
        dataSet = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventUserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.card_event_data, parent, false)
        return EventUserViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: EventUserViewHolder, position: Int) {
        val event = dataSet[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int = dataSet.size
}