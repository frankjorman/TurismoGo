package pe.turismogo.usecases.business.panel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pe.turismogo.data.DatabaseManager
import pe.turismogo.model.domain.Event
import pe.turismogo.observable.rtdatabase.DatabaseManagerObserver
import pe.turismogo.util.Constants

class PanelViewModel : ViewModel() , DatabaseManagerObserver.EventUpdateObserver {

    //opciones para el filtro
    companion object {
        const val TYPE_OPTION_ALL = "typeAll"
        const val TYPE_OPTION_NAME = "typeName"
        const val TYPE_OPTION_DATE = "typeDate"
        const val TYPE_OPTION_STATUS = "typeStatus"
    }

    //buffer de todos los eventos
    private val allEventsList : MutableLiveData<List<Event>> = MutableLiveData()
    //eventos para el mutable live data que tendra el observer
    private val eventList : MutableLiveData<List<Event>> = MutableLiveData()
    //variable para el filtro
    private val filterType : MutableLiveData<String> = MutableLiveData()

    //funcion para inicializar la configuracion del ViewModel
    fun init() {
        Log.d(Constants.TAG_COMMON, "init - PanelViewModel")
        filterType.value = TYPE_OPTION_ALL
        DatabaseManager.getInstance().addEventUpdateObservable(this)

        if(DatabaseManager.getInstance().getEventsFromUserList().isNotEmpty()) {
            val events = DatabaseManager.getInstance().getEventsFromUserList()
            eventList.value = events
            allEventsList.value = events
        }
    }

    //funcion para eliminar el observer
    fun detach() {
        filterType.value = TYPE_OPTION_ALL
        DatabaseManager.getInstance().removeEventUpdateObservable(this)
    }

    //funcion para establecer el filtro
    fun setFilterType(type : String) {
        filterType.value = type
    }

    //funcion para obtener el mutable live data de los eventos
    fun getEventListLiveData() : MutableLiveData<List<Event>> {
        return eventList
    }

    //funcion para filtrar los eventos
    fun filterEventsLiveData(search : String) {
        val filteredList = MutableLiveData<List<Event>>() //se crea un nuevo mutable live data

        if(search.isNotEmpty()) { //si la entrada no es vacia entonces...
            when(filterType.value) { //se evaluan los tipos de filtro
                TYPE_OPTION_NAME -> { //se busca por nombre del evento
                    val filteredEvents = allEventsList.value?.filter { it.title.contains(search, true) }
                    filteredList.value = filteredEvents ?: allEventsList.value
                }
                TYPE_OPTION_DATE -> { //se busca por fecha
                    Log.d(Constants.TAG_COMMON, "filtering for OPTION DATE")
                    val filteredEvents =  allEventsList.value?.filter { it.departureDate.contains(search, true) }
                    filteredList.value = filteredEvents ?: allEventsList.value
                }
                TYPE_OPTION_STATUS -> { //se busca por estado
                    val filteredEvents = allEventsList.value?.filter { it.active }
                    filteredList.value = filteredEvents ?: allEventsList.value
                }
                else -> filteredList.value = allEventsList.value //se muestra todos los eventos
            }
        } else filteredList.value = allEventsList.value //se muestra todos los eventos

        eventList.value = filteredList.value //se establece el nuevo mutable live data
    }

    //observer de database para cargar los datos una vez descargados
    override fun notifyEventUpdateObservers(events: List<Event>) {
        Log.d(Constants.TAG_COMMON, "notification event update - PanelViewModel")
        eventList.value = events
        allEventsList.value = events
    }
}