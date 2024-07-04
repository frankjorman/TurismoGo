package pe.turismogo.usecases.user.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.turismogo.R
import pe.turismogo.interfaces.IOnEventClickListener
import pe.turismogo.model.domain.Event

class ReservationUserAdapter
    (private val listener : IOnEventClickListener)
    : RecyclerView.Adapter<ReservationUserViewHolder>() {

    private var dataSet = listOf<Event>()

    fun setData(data: List<Event>) {
        dataSet = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationUserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.card_event_data, parent, false)
        return ReservationUserViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ReservationUserViewHolder, position: Int) {
        val event = dataSet[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int = dataSet.size
}
