package pe.turismogo.usecases.business.panel.edit_events

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import pe.turismogo.R
import pe.turismogo.data.CacheManager
import pe.turismogo.data.DatabaseManager
import pe.turismogo.factory.MessageFactory
import pe.turismogo.model.domain.Event
import pe.turismogo.observable.rtdatabase.DatabaseManagerObserver
import pe.turismogo.usecases.business.panel.base.EventsActivity
import pe.turismogo.util.Constants
import pe.turismogo.util.Permissions

class AdminEditEventsActivity : EventsActivity(), DatabaseManagerObserver.EventInsertObserver {

    private lateinit var event : Event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        event = CacheManager.getInstance().getEventCache(activity)
        updateFields()
    }

    private fun updateFields() {
        loadImage(event.image)

        imageUri = event.image

        binding.etExcursionName.setText(event.title)
        binding.etExcursionCost.setText(event.cost)
        binding.etExcursionAvailableSeats.setText(event.availableSeats.toString())

        binding.etExcursionDeparture.setText(event.departureDate)
        binding.etExcursionReturn.setText(event.returnDate)


        binding.etExcursionDescription.setText(event.description)
        binding.etExcursionItinerary.setText(event.itinerary)

        binding.etExcursionDestination.setText(event.destinationPoint)
        binding.etExcursionMeetingPoint.setText(event.meetingPoint)

        if(!event.active)
            lockEdit()
    }

    private fun lockEdit() {
        binding.etExcursionName.isEnabled = false
        binding.etExcursionCost.isEnabled = false
        binding.etExcursionAvailableSeats.isEnabled = false
        binding.etExcursionDeparture.isEnabled = false
        binding.etExcursionReturn.isEnabled = false
        binding.etExcursionMeetingPoint.isEnabled = false
        binding.etExcursionDestination.isEnabled = false
        binding.etExcursionDescription.isEnabled = false
        binding.etExcursionItinerary.isEnabled = false

        binding.btnAddImage.isEnabled = false
        binding.btnSaveEvent.isEnabled = false
    }

    override fun saveEvent() {

        event.title = binding.etExcursionName.text.toString()
        event.cost = binding.etExcursionCost.text.toString()
        event.availableSeats = binding.etExcursionAvailableSeats.text.toString().toInt()

        event.departureDate = binding.etExcursionDeparture.text.toString()
        event.returnDate = binding.etExcursionReturn.text.toString()

        event.description = binding.etExcursionDescription.text.toString()
        event.itinerary = binding.etExcursionItinerary.text.toString()

        event.meetingPoint = binding.etExcursionMeetingPoint.text.toString()
        event.destinationPoint = binding.etExcursionDestination.text.toString()

        dialog = MessageFactory().getDialog(context, MessageFactory.TYPE_LOAD).create()
        dialog.show()

        if(imageUri != null && imageUri != event.image)
            DatabaseManager.getInstance().updateEvent(event, imageUri.toString())
        else
            DatabaseManager.getInstance().updateEvent(event)

    }

    override fun notifyEventInsertObservers(isSuccessful: Boolean) {
        dialog.dismiss()
        if(isSuccessful) {
            Constants.showToast(context, context.getString(R.string.success_event_updated))
            finish()
        } else {
            Constants.showToast(context, context.getString(R.string.error_event_update))
        }
    }
}