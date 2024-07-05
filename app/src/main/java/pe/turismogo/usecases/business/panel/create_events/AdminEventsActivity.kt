package pe.turismogo.usecases.business.panel.create_events

import pe.turismogo.R
import pe.turismogo.data.DatabaseManager
import pe.turismogo.data.DatabaseReferences
import pe.turismogo.factory.MessageFactory
import pe.turismogo.model.domain.Event
import pe.turismogo.observable.rtdatabase.DatabaseObserver
import pe.turismogo.usecases.business.panel.base.EventsActivity
import pe.turismogo.util.Constants
import pe.turismogo.util.Utils

class AdminEventsActivity : EventsActivity() , DatabaseObserver.EventInsertObserver {

    override fun saveEvent() {
        val event = Event()
        event.id = DatabaseReferences.generatePushId()
        event.image = imageUri.toString()

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

        DatabaseManager
            .getInstance()
            .createEvent(event)
    }

    override fun notifyEventInsertObservers(isSuccessful: Boolean) {
        dialog.dismiss()
        if(isSuccessful) {
            Utils.showToast(context, context.getString(R.string.success_event_creation))
            finish()
        } else {
            Utils.showToast(context, context.getString(R.string.error_event_creation))
        }
    }
}