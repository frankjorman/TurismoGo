package pe.turismogo.data

import android.app.Activity
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import pe.turismogo.model.domain.Event
import pe.turismogo.model.domain.Review
import pe.turismogo.model.domain.User
import pe.turismogo.observable.auth.AuthObserver
import pe.turismogo.observable.rtdatabase.DatabaseObservable
import pe.turismogo.observable.rtdatabase.DatabaseObserver
import pe.turismogo.observable.storage.StoreObserver
import pe.turismogo.model.session.UserSingleton
import pe.turismogo.util.Constants
import java.text.SimpleDateFormat
import java.util.Locale

class DatabaseManager :
    DatabaseObservable.UserUpdateObservable,
    DatabaseObservable.EventInsertObservable,
    DatabaseObservable.EventDeleteObservable,
    DatabaseObservable.EventUpdateObservable,
    DatabaseObservable.EventsObservable,
    AuthObserver.AuthSession,
    AuthObserver.AuthCreation,
    StoreObserver.UploadImage,
    StoreObserver.DeleteImage {

    companion object {
        private var instance: DatabaseManager? = null

        fun getInstance(): DatabaseManager {
            if (instance == null) {
                instance = DatabaseManager()
            }
            return instance as DatabaseManager
        }

        fun destroyInstance() {
            instance = null
        }
    }

    private val authManager : AuthManager = AuthManager()
    private val storeManager : StoreManager = StoreManager()

    private lateinit var event : Event

    private val updateObserverList : ArrayList<DatabaseObserver.UserUpdateObserver> = arrayListOf()
    private val eventInsertObserverList : ArrayList<DatabaseObserver.EventInsertObserver> = arrayListOf()
    private val eventDeleteObserverList : ArrayList<DatabaseObserver.EventDeleteObserver> = arrayListOf()

    private val eventsFromUserObserverList : ArrayList<DatabaseObserver.EventUpdateObserver> = arrayListOf()
    private val allEventsObserverList : ArrayList<DatabaseObserver.EventsObserver> = arrayListOf()

    private var userReference = DatabaseReferences.getRootUsers()
    private var eventsReference = DatabaseReferences.getRootEvents()

    private var eventInsertForUserReference = DatabaseReferences.getRootEvents()
    private var eventDeleteForUserReference = DatabaseReferences.getRootEvents()
    private var allEventsReference = DatabaseReferences.getRootEvents()

    private val eventsFromUserList = arrayListOf<Event>()
    private val allEventsList = arrayListOf<Event>()
    private val allReservedEventsList = arrayListOf<Event>()
    private val allHistoryEventsList = arrayListOf<Event>()

    private val userReferenceListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.exists()) {
                val temporalUser = snapshot.getValue(User::class.java)
                if(temporalUser != null)
                    updateUserData(temporalUser)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(Constants.TAG_COMMON, "error fetching database user from AuthManager ${error.message}")
        }
    }

    private val dataInsertListener = OnCompleteListener<Void> {
        Log.d(Constants.TAG_COMMON, "event insert complete")
        notifyEventInsertObservers(it.isSuccessful)
    }

    private val dataDeleteListener = OnCompleteListener<Void> {
        Log.d(Constants.TAG_COMMON, "event delete complete")
        notifyEventDeleteObservers(it.isSuccessful)
    }

    private val eventsFromUserListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.exists()) {
                eventsFromUserList.clear()
                UserSingleton.getInstance().getUser()?.id.let { userId ->
                    snapshot.children.forEach {
                        val event = it.getValue(Event::class.java)
                        if(event != null && event.owner.id == userId) {
                            eventsFromUserList.add(event)
                        }
                    }
                }
                notifyEventUpdateObservers(eventsFromUserList)
            } else
                Log.d(Constants.TAG_COMMON, "no events found")
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(Constants.TAG_COMMON, "error fetching database events from DatabaseManager ${error.message}")
        }
    }

    private val allEventsListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.exists()) {

                allEventsList.clear()
                allReservedEventsList.clear()
                allHistoryEventsList.clear()

                snapshot.children.forEach { snap -> //si el snapshot tiene hijos, se recorren
                    UserSingleton.getInstance().getUser()?.let { user -> //si el usuario existe
                        val event = snap.getValue(Event::class.java) //se convierte el snapshot a event
                        event?.let { //si el evento no es nulo...
                            if(it.participants.contains(user)) { //el usuario es participante del evento
                                if(it.active) { //si el evento esta activo, entonces, es una reserva
                                    allReservedEventsList.add(event)
                                } else { //si el evento no esta activo, entonces es un evento viejo
                                    allHistoryEventsList.add(event)
                                }
                            } else { //si no pertenece, entonces es un evento nuevo o disponible
                                if(it.active)
                                    allEventsList.add(event)
                            }
                        }
                    }
                }
                notifyEventsObservers(allEventsList)
            } else
                Log.d(Constants.TAG_COMMON, "no events found")
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(Constants.TAG_COMMON, "error fetching database events from DatabaseManager ${error.message}")
        }
    }

    init {
        authManager.addAuthSessionObserver(this)
        authManager.addAuthCreationObserver(this)

        storeManager.addUploadImageObserver(this)
        storeManager.addDeleteImageObserver(this)
    }

    @JvmName("getAuth")
    fun getAuth() : AuthManager {
        return authManager
    }

    @JvmName("getEventsFromUserList")
    fun getEventsFromUserList() : List<Event> {
        return eventsFromUserList
    }

    @JvmName("getAllEvents")
    fun getAllEvents() : List<Event> {
        return allEventsList
    }

    @JvmName("getAllReservedEvents")
    fun getAllReservedEvents() : List<Event> {
        return allReservedEventsList
    }

    @JvmName("getAllHistoryEvents")
    fun getAllHistoryEvents() : List<Event> {
        return allHistoryEventsList
    }

    @JvmName("createActivity")
    fun createEvent(event: Event) {
        Log.d(Constants.TAG_COMMON, "creating event")
        UserSingleton.getInstance().getUser()?.let { user ->
            event.owner = user
            event.active = true

            this.event = event

            storeManager.uploadImage(event.id, event.image)
        }
    }

    @JvmName("updateEvent")
    fun updateEvent(event : Event) {
        this.event = event
        eventInsertForUserReference
            .child(event.id)
            .setValue(event)
            .addOnCompleteListener(dataInsertListener)
    }

    @JvmName("updateEvent")
    fun updateEvent(event: Event, imageUrl : String) {
        this.event = event
        storeManager.uploadImage(event.id, imageUrl)
    }

    @JvmName("updateUser")
    fun updateUser(user : User) {
        Log.d(Constants.TAG_COMMON, "updating user")
        userReference
            .child(user.id)
            .setValue(user)
            .addOnCompleteListener(dataInsertListener)
    }

    @JvmName("deleteEvent")
    fun deleteEvent(event : Event) {
        this.event = event
        storeManager.deleteImage(event.image)
    }

    @JvmName("softDeleteEvent")
    fun softDeleteEvent(event : Event) {
        this.event = event
        event.active = false
        eventDeleteForUserReference
            .child(event.id)
            .setValue(event)
            .addOnCompleteListener(dataDeleteListener)
    }

    @JvmName("joinEvent")
    fun joinEvent(event : Event) {
        UserSingleton.getInstance().getUser()?.let { user ->
            event.availableSeats -= 1
            event.participants.add(user)

            allEventsReference
                .child(event.id)
                .setValue(event)
                .addOnCompleteListener(dataInsertListener)
        }
    }

    @JvmName("isEventCollisionSave")
    fun isEventCollision(event : Event) : Boolean {
        val eventsReserved = allReservedEventsList
        return isEventCollide(event, eventsReserved)
    }

    @JvmName("saveReview")
    fun saveReview(event : Event, review : Review) {
        UserSingleton.getInstance().getUser()?.let { user ->
            review.id = DatabaseReferences.generatePushId()
            review.userId = user.id
            review.userName = user.name

            val reviewList = event.reviews
            reviewList.add(review)

            eventsReference
                .child(event.id)
                .child(Constants.DATABASE_REVIEWS)
                .setValue(reviewList)
                .addOnCompleteListener(dataInsertListener)
        }
    }

    fun isUserAvailableForReview(activity : Activity) : Boolean {
        val currentEvent = CacheManager.getInstance().getEventCache(activity)
        val user = UserSingleton.getInstance().getUser()
        return !currentEvent.reviews.any { it.userId == user?.id }
    }

    private fun isEventCollide(event1: Event, event2: Event): Boolean {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val departureDate1 = dateFormat.parse(event1.departureDate)
        val returnDate1 = dateFormat.parse(event1.returnDate)

        val departureDate2 = dateFormat.parse(event2.departureDate)
        val returnDate2 = dateFormat.parse(event2.returnDate)

        // Verificar si los intervalos se superponen
        if (departureDate1 != null && returnDate1 != null) {
            return !(returnDate1.before(departureDate2) || departureDate1.after(returnDate2))
        } else
            return false
    }

    private fun isEventCollide(event1: Event, eventList: List<Event>): Boolean {
        for (event in eventList) {
            if (isEventCollide(event1, event)) {
                return true // Hay colisión con al menos un evento en la lista
            }
        }
        return false // No hay colisiones con ningún evento en la lista
    }

    private fun setUserReference(id : String) {
        userReference
            .child(id)
            .addValueEventListener(userReferenceListener)
    }

    private fun createUser(user : User) {
        user.password = "" //por seguridad eliminamos el password del usuario
        userReference
            .child(user.id)
            .setValue(user).addOnCompleteListener(dataInsertListener)
    }

    private fun updateUserData(user : User) {
        UserSingleton.getInstance().setUser(user)
        notifyUserUpdateObservers(user)

        eventInsertForUserReference.addValueEventListener(eventsFromUserListener)
    }

    private fun removeUserListener() {
        userReference.removeEventListener(userReferenceListener)
        eventInsertForUserReference.removeEventListener(eventsFromUserListener)
    }

    override fun addUserUpdateObservable(observer: DatabaseObserver.UserUpdateObserver) {
        updateObserverList.add(observer)
    }

    override fun removeUserUpdateObservable(observer: DatabaseObserver.UserUpdateObserver) {
        updateObserverList.remove(observer)
    }

    override fun notifyUserUpdateObservers(user: User) {
        updateObserverList.forEach {
            it.notifyUserUpdateObservers(user)
        }
    }

    override fun addEventInsertObservable(observer: DatabaseObserver.EventInsertObserver) {
        eventInsertObserverList.add(observer)
    }

    override fun removeEventInsertObservable(observer: DatabaseObserver.EventInsertObserver) {
        eventInsertObserverList.remove(observer)
    }

    override fun notifyEventInsertObservers(isSuccessful : Boolean) {
        Log.d(Constants.TAG_COMMON, "notify event insert observers")
        eventInsertObserverList.forEach {
            it.notifyEventInsertObservers(isSuccessful)
        }
    }

    override fun addEventUpdateObservable(observer: DatabaseObserver.EventUpdateObserver) {
        eventsFromUserObserverList.add(observer)
    }

    override fun removeEventUpdateObservable(observer: DatabaseObserver.EventUpdateObserver) {
        eventsFromUserObserverList.remove(observer)
    }

    override fun notifyEventUpdateObservers(events: List<Event>) {
        Log.d(Constants.TAG_COMMON, "notify event update observers (fromUser)")
        eventsFromUserObserverList.forEach {
            it.notifyEventUpdateObservers(events)
        }
    }

    override fun addEventsObservable(observer: DatabaseObserver.EventsObserver) {
        allEventsObserverList.add(observer)
    }

    override fun removeEventsObservable(observer: DatabaseObserver.EventsObserver) {
        allEventsObserverList.remove(observer)
    }

    override fun notifyEventsObservers(events: List<Event>) {
        Log.d(Constants.TAG_COMMON, "notification to observers sent - allEvents")
        allEventsObserverList.forEach {
            it.notifyEventsObservers(events)
        }
    }

    override fun addEventDeleteObservable(observer: DatabaseObserver.EventDeleteObserver) {
        eventDeleteObserverList.add(observer)
    }

    override fun removeEventDeleteObservable(observer: DatabaseObserver.EventDeleteObserver) {
        eventDeleteObserverList.remove(observer)
    }

    override fun notifyEventDeleteObservers(isSuccessful: Boolean) {
        eventDeleteObserverList.forEach {
            it.notifyEventDeleteObservers(isSuccessful)
        }
    }

    override fun notifyAuthSessionObservers(isLogged: Boolean, uid: String?, exception: Any?) {
        if(isLogged && uid != null) {
            setUserReference(uid)
            allEventsReference.addValueEventListener(allEventsListener)
        }
    }

    override fun notifyAuthCreationObservers(isSuccessful: Boolean, message: Any?) {
        if(isSuccessful) {
            UserSingleton.getInstance().getUser()?.let { user -> createUser(user) }
        }
    }

    override fun notifyUploadImageObservers(isSuccessful: Boolean, url: String) {
        if(isSuccessful) {
            event.image = url
            eventInsertForUserReference
                .child(event.id)
                .setValue(event)
                .addOnCompleteListener(dataInsertListener)
        } else {
            notifyEventInsertObservers(false)
        }
    }

    override fun notifyDeleteImageObservers(isSuccessful: Boolean) {
        if(isSuccessful) {
            eventDeleteForUserReference
                .child(event.id)
                .removeValue()
                .addOnCompleteListener(dataDeleteListener)
        } else {
            notifyEventInsertObservers(false)
        }
    }

}