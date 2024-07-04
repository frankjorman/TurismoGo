package pe.turismogo.usecases.user.events

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import pe.turismogo.R
import pe.turismogo.interfaces.IOnEventClickListener
import pe.turismogo.model.domain.Event
import pe.turismogo.util.Constants

class EventUserViewHolder
    (view : View, private val listener : IOnEventClickListener)
    : RecyclerView.ViewHolder(view) {

    private val context = view.context
    private val ivEventImage: ImageView = view.findViewById(R.id.ivEventCardData1)
    private val tvEventName: MaterialTextView = view.findViewById(R.id.tvEventNameCardData1)
    private val tvEventDescription: MaterialTextView = view.findViewById(R.id.tvEventDescriptionCardData1)
    private val tvEventCost: MaterialTextView = view.findViewById(R.id.tvEventCostCardData1)
    private val tvEventDates: MaterialTextView = view.findViewById(R.id.tvEventDatesCardData1)
    private val tvEventRoomAvailable: MaterialTextView = view.findViewById(R.id.tvEventRoomAvailableCardData1)
    private val btnEventJoin: MaterialButton = view.findViewById(R.id.btnEventJoinCardData1)

    fun bind(event: Event) {
        Glide.with(context).load(event.image).into(ivEventImage)

        val name = event.title
        val description = event.description
        val days = Constants.calculateDaysBetween(event.departureDate, event.returnDate)

        val date = "${context.getString(R.string.prompt_date_departure)}: ${event.departureDate} - $days days"
        val cost = "${context.getString(R.string.prompt_cost)}: S/${event.cost}"
        val availableSeats =
            "${context.getString(R.string.prompt_available_seats)} : ${event.availableSeats}"

        tvEventName.text = name
        tvEventDescription.text = description
        tvEventCost.text = cost
        tvEventDates.text = date
        tvEventRoomAvailable.text = availableSeats

        if(event.availableSeats == 0) {
            btnEventJoin.isEnabled = false
        } else {
            btnEventJoin.isEnabled = true
        }

        btnEventJoin.setOnClickListener {
            listener.onClickDetails(event)
        }

    }
}