package pe.turismogo.observable.rtdatabase

import pe.turismogo.model.domain.Event
import pe.turismogo.model.domain.User

interface DatabaseManagerObservable {

    @Deprecated("Por ahora se encuentra deprecado")
    interface UserDataObservable {
        fun addUserDataObservable(observer : DatabaseManagerObserver.UserDataObserver)
        fun removeUserDataObservable(observer : DatabaseManagerObserver.UserDataObserver)
        fun notifyUserDataObservers(userList : ArrayList<User>)
    }

    interface UserUpdateObservable {
        fun addUserUpdateObservable(observer : DatabaseManagerObserver.UserUpdateObserver)
        fun removeUserUpdateObservable(observer : DatabaseManagerObserver.UserUpdateObserver)
        fun notifyUserUpdateObservers(user : User)
    }

    interface EventsObservable {
        fun addEventsObservable(observer : DatabaseManagerObserver.EventsObserver)
        fun removeEventsObservable(observer : DatabaseManagerObserver.EventsObserver)
        fun notifyEventsObservers(events : List<Event>)
    }

    interface EventInsertObservable {
        fun addEventInsertObservable(observer : DatabaseManagerObserver.EventInsertObserver)
        fun removeEventInsertObservable(observer : DatabaseManagerObserver.EventInsertObserver)
        fun notifyEventInsertObservers(isSuccessful : Boolean)
    }

    interface EventDeleteObservable {
        fun addEventDeleteObservable(observer : DatabaseManagerObserver.EventDeleteObserver)
        fun removeEventDeleteObservable(observer : DatabaseManagerObserver.EventDeleteObserver)
        fun notifyEventDeleteObservers(isSuccessful : Boolean)
    }

    interface EventUpdateObservable {
        fun addEventUpdateObservable(observer : DatabaseManagerObserver.EventUpdateObserver)
        fun removeEventUpdateObservable(observer : DatabaseManagerObserver.EventUpdateObserver)
        fun notifyEventUpdateObservers(events : List<Event>)
    }

}