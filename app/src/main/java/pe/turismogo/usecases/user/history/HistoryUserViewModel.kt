package pe.turismogo.usecases.user.history

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pe.turismogo.data.DatabaseManager
import pe.turismogo.model.domain.Event
import pe.turismogo.observable.rtdatabase.DatabaseObserver
import pe.turismogo.util.Constants

class HistoryUserViewModel : ViewModel(), DatabaseObserver.EventsObserver {

    private val eventList : MutableLiveData<List<Event>> = MutableLiveData()

    fun init() {
        Log.d(Constants.TAG_COMMON, "init - HistoryUserViewModel")
        DatabaseManager.getInstance().addEventsObservable(this)
        historyUpdate()
    }

    fun detach() {
        DatabaseManager.getInstance().removeEventsObservable(this)
    }

    fun getEventListLiveData() : MutableLiveData<List<Event>> = eventList

    private fun historyUpdate() {
        if(DatabaseManager.getInstance().getAllHistoryEvents().isNotEmpty()) {
            val events = DatabaseManager.getInstance().getAllHistoryEvents()
            Log.d(Constants.TAG_COMMON, "history events has ${events.size}")
            eventList.value = events
        }
    }

    override fun notifyEventsObservers(events: List<Event>) {
        historyUpdate()
    }
}