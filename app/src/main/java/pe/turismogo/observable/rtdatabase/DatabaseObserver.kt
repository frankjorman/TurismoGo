package pe.turismogo.observable.rtdatabase

import pe.turismogo.model.domain.Event
import pe.turismogo.model.domain.User

interface DatabaseObserver {

    @Deprecated("Por ahora se encuentra deprecado")
    interface UserDataObserver {
        fun notifyUserDataObservers(userList : ArrayList<User>)
    }

    interface UserUpdateObserver{
        fun notifyUserUpdateObservers(user : User)
    }

    interface EventsObserver {
        fun notifyEventsObservers(events : List<Event>)
    }

    interface EventInsertObserver {
        fun notifyEventInsertObservers(isSuccessful : Boolean)
    }

    interface EventDeleteObserver {
        fun notifyEventDeleteObservers(isSuccessful : Boolean)
    }

    interface EventUpdateObserver {
        fun notifyEventUpdateObservers(events : List<Event>)
    }
}