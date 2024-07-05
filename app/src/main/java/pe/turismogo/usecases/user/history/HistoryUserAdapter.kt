package pe.turismogo.usecases.user.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.turismogo.R
import pe.turismogo.model.domain.Event

class HistoryUserAdapter : RecyclerView.Adapter<HistoryUserViewHolder>() {

    private var datase = listOf<Event>()

    fun setData(data : List<Event>) {
        datase = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryUserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.card_event_data, parent, false)
        return HistoryUserViewHolder(view)
    }

    override fun getItemCount(): Int = datase.size

    override fun onBindViewHolder(holder: HistoryUserViewHolder, position: Int) {
        val event = datase[position]
        holder.bind(event)
    }
}