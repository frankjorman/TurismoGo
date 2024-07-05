package pe.turismogo.usecases.business.management

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pe.turismogo.data.DatabaseManager
import pe.turismogo.model.domain.Event
import pe.turismogo.model.domain.User
import pe.turismogo.observable.rtdatabase.DatabaseObserver
import pe.turismogo.util.Constants

class ManagementViewModel : ViewModel() , DatabaseObserver.EventUpdateObserver {

    private val userList : MutableLiveData<List<User>> = MutableLiveData()
    private val eventList : MutableLiveData<List<Event>> = MutableLiveData()


    fun init() {
        Log.d(Constants.TAG_COMMON, "init - ManagementViewModel")
        DatabaseManager.getInstance().addEventUpdateObservable(this)

        if(DatabaseManager.getInstance().getEventsFromUserList().isNotEmpty()) {
            val events = getOnlyActiveEvents(DatabaseManager.getInstance().getEventsFromUserList())
            eventList.value = events
        }
    }

    fun detach() {
        DatabaseManager.getInstance().removeEventUpdateObservable(this)
    }

    fun getUserListLiveData() : MutableLiveData<List<User>> {
        return userList
    }

    fun getEventListLiveData() : MutableLiveData<List<Event>> {
        return eventList
    }

    fun loadUserFromEvent(position : Int) {
        val eventSelected = eventList.value?.get(position)
        val usersInEvent = eventSelected?.participants
        usersInEvent?.let {
            userList.value = it
        }
    }

    private fun getOnlyActiveEvents(events : List<Event>) : List<Event> {
        val activeEvents = arrayListOf<Event>()
        events.forEach {
            if(it.active) activeEvents.add(it)
        }
        return activeEvents
    }

    override fun notifyEventUpdateObservers(events: List<Event>) {
        val filteredEvents = getOnlyActiveEvents(events)
        eventList.value = filteredEvents
    }
}