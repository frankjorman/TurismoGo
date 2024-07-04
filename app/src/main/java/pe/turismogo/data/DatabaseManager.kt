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
import pe.turismogo.observable.rtdatabase.DatabaseManagerObservable
import pe.turismogo.observable.rtdatabase.DatabaseManagerObserver
import pe.turismogo.observable.storage.StoreObserver
import pe.turismogo.model.session.UserSingleton
import pe.turismogo.util.Constants
import java.text.SimpleDateFormat
import java.util.Locale

class DatabaseManager :
    DatabaseManagerObservable.UserUpdateObservable,
    DatabaseManagerObservable.EventInsertObservable,
    DatabaseManagerObservable.EventDeleteObservable,
    DatabaseManagerObservable.EventUpdateObservable,
    DatabaseManagerObservable.EventsObservable,
    AuthObserver.AuthSession,
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

    private val updateObserverList : ArrayList<DatabaseManagerObserver.UserUpdateObserver> = arrayListOf()
    private val eventInsertObserverList : ArrayList<DatabaseManagerObserver.EventInsertObserver> = arrayListOf()
    private val eventDeleteObserverList : ArrayList<DatabaseManagerObserver.EventDeleteObserver> = arrayListOf()

    private val eventsFromUserObserverList : ArrayList<DatabaseManagerObserver.EventUpdateObserver> = arrayListOf()
    private val allEventsObserverList : ArrayList<DatabaseManagerObserver.EventsObserver> = arrayListOf()

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
                    updateUser(temporalUser)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(Constants.TAG_COMMON, "error fetching database user from AuthManager ${error.message}")
        }
    }

    private val eventInsertListener = OnCompleteListener<Void> {
        Log.d(Constants.TAG_COMMON, "event insert complete")
        notifyEventInsertObservers(it.isSuccessful)
    }

    private val eventDeleteListener = OnCompleteListener<Void> {
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
                snapshot.children.forEach {
                    val event = it.getValue(Event::class.java)
                    if(event != null && event.active) {
                        if(!event.participants.contains(UserSingleton.getInstance().getUser()))
                            allEventsList.add(event)
                        else {
                            Log.d(Constants.TAG_COMMON, "sending event to reserved list or history")
                            if(event.active)
                                allReservedEventsList.add(event)
                            else
                                allHistoryEventsList.add(event)
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
            .addOnCompleteListener(eventInsertListener)
    }

    @JvmName("updateEvent")
    fun updateEvent(event: Event, imageUrl : String) {
        this.event = event
        storeManager.uploadImage(event.id, imageUrl)
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
            .addOnCompleteListener(eventDeleteListener)
    }

    @JvmName("joinEvent")
    fun joinEvent(event : Event) {
        UserSingleton.getInstance().getUser()?.let { user ->
            event.availableSeats -= 1
            event.participants.add(user)

            allEventsReference
                .child(event.id)
                .setValue(event)
                .addOnCompleteListener(eventInsertListener)
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
                .addOnCompleteListener(eventInsertListener)
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
        userReference.child(id).addValueEventListener(userReferenceListener)
    }

    private fun updateUser(user : User) {
        UserSingleton.getInstance().setUser(user)
        notifyUserUpdateObservers(user)

        eventInsertForUserReference.addValueEventListener(eventsFromUserListener)
    }

    private fun removeUserListener() {
        userReference.removeEventListener(userReferenceListener)
        eventInsertForUserReference.removeEventListener(eventsFromUserListener)
    }

    override fun addUserUpdateObservable(observer: DatabaseManagerObserver.UserUpdateObserver) {
        updateObserverList.add(observer)
    }

    override fun removeUserUpdateObservable(observer: DatabaseManagerObserver.UserUpdateObserver) {
        updateObserverList.remove(observer)
    }

    override fun notifyUserUpdateObservers(user: User) {
        updateObserverList.forEach {
            it.notifyUserUpdateObservers(user)
        }
    }

    override fun addEventInsertObservable(observer: DatabaseManagerObserver.EventInsertObserver) {
        eventInsertObserverList.add(observer)
    }

    override fun removeEventInsertObservable(observer: DatabaseManagerObserver.EventInsertObserver) {
        eventInsertObserverList.remove(observer)
    }

    override fun notifyEventInsertObservers(isSuccessful : Boolean) {
        Log.d(Constants.TAG_COMMON, "notify event insert observers")
        eventInsertObserverList.forEach {
            it.notifyEventInsertObservers(isSuccessful)
        }
    }

    override fun addEventUpdateObservable(observer: DatabaseManagerObserver.EventUpdateObserver) {
        eventsFromUserObserverList.add(observer)
    }

    override fun removeEventUpdateObservable(observer: DatabaseManagerObserver.EventUpdateObserver) {
        eventsFromUserObserverList.remove(observer)
    }

    override fun notifyEventUpdateObservers(events: List<Event>) {
        Log.d(Constants.TAG_COMMON, "notify event update observers (fromUser)")
        eventsFromUserObserverList.forEach {
            it.notifyEventUpdateObservers(events)
        }
    }

    override fun addEventsObservable(observer: DatabaseManagerObserver.EventsObserver) {
        allEventsObserverList.add(observer)
    }

    override fun removeEventsObservable(observer: DatabaseManagerObserver.EventsObserver) {
        allEventsObserverList.remove(observer)
    }

    override fun notifyEventsObservers(events: List<Event>) {
        Log.d(Constants.TAG_COMMON, "notification to observers sent - allEvents")
        allEventsObserverList.forEach {
            it.notifyEventsObservers(events)
        }
    }

    override fun addEventDeleteObservable(observer: DatabaseManagerObserver.EventDeleteObserver) {
        eventDeleteObserverList.add(observer)
    }

    override fun removeEventDeleteObservable(observer: DatabaseManagerObserver.EventDeleteObserver) {
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

    override fun notifyUploadImageObservers(isSuccessful: Boolean, url: String) {
        if(isSuccessful) {
            event.image = url
            eventInsertForUserReference
                .child(event.id)
                .setValue(event)
                .addOnCompleteListener(eventInsertListener)
        } else {
            notifyEventInsertObservers(false)
        }
    }

    override fun notifyDeleteImageObservers(isSuccessful: Boolean) {
        if(isSuccessful) {
            eventDeleteForUserReference
                .child(event.id)
                .removeValue()
                .addOnCompleteListener(eventDeleteListener)
        } else {
            notifyEventInsertObservers(false)
        }
    }

}