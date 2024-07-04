package pe.turismogo.usecases.user.events

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pe.turismogo.data.DatabaseManager
import pe.turismogo.model.domain.Event
import pe.turismogo.observable.rtdatabase.DatabaseManagerObserver
import pe.turismogo.util.Constants

class EventViewModel : ViewModel(), DatabaseManagerObserver.EventsObserver {

    //opciones para el filtro
    companion object {
        const val TYPE_OPTION_ALL = "typeAll"
        const val TYPE_OPTION_DATES = "typeDates"
        const val TYPE_OPTION_DESCRIPTION = "typeDescription"
    }

    private val allEventsList : MutableLiveData<List<Event>> = MutableLiveData()

    private val eventList : MutableLiveData<List<Event>> = MutableLiveData()
    private val filterType : MutableLiveData<String> = MutableLiveData()

    fun init() {
        Log.d(Constants.TAG_COMMON, "init - EventViewModel")
        filterType.value = TYPE_OPTION_ALL
        DatabaseManager.getInstance().addEventsObservable(this)

        if(DatabaseManager.getInstance().getAllEvents().isNotEmpty()) {
            val events = DatabaseManager.getInstance().getAllEvents()
            Log.d(Constants.TAG_COMMON, "events has $events.size")
            eventList.value = events
            allEventsList.value = events
        }
    }

    fun detach() {
        filterType.value = TYPE_OPTION_ALL
        DatabaseManager.getInstance().removeEventsObservable(this)
    }

    fun setFilterType(type : String) {
        filterType.value = type
    }

    fun getEventListLiveData() : MutableLiveData<List<Event>> {
        return eventList
    }

    fun filterEventsLiveData(search : String) {
        val filteredList = MutableLiveData<List<Event>>() //se crea un nuevo mutable live data

        if(search.isNotEmpty()) { //si la entrada no es vacia entonces...
            when(filterType.value) { //se evaluan los tipos de filtro
                TYPE_OPTION_DATES -> { //se busca por fechas inicio y fin
                    val departureEvents =  allEventsList.value?.filter { it.departureDate.contains(search, true) }
                    val returnEvents = departureEvents?.filter { it.returnDate.contains(search, true) }
                    filteredList.value = returnEvents ?: allEventsList.value
                }
                TYPE_OPTION_DESCRIPTION -> { //se busca por descripcion del evento
                    val filteredEvents = allEventsList.value?.filter { it.description.contains(search, true) }
                    filteredList.value = filteredEvents ?: allEventsList.value
                }
                else -> filteredList.value = allEventsList.value //se muestra todos los eventos
            }
        } else filteredList.value = allEventsList.value //se muestra todos los eventos

        eventList.value = filteredList.value //se establece el nuevo mutable live data
    }

    //observer de database para cargar los datos una vez descargados
    override fun notifyEventsObservers(events: List<Event>) {
        Log.d(Constants.TAG_COMMON, "notification event all - EventViewModel")
        allEventsList.value = events
        eventList.value = events
    }
}
