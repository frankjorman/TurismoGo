package pe.turismogo.observable.rtdatabase

import pe.turismogo.model.domain.Event
import pe.turismogo.model.domain.User

interface DatabaseObservable {

    @Deprecated("Por ahora se encuentra deprecado")
    interface UserDataObservable {
        fun addUserDataObservable(observer : DatabaseObserver.UserDataObserver)
        fun removeUserDataObservable(observer : DatabaseObserver.UserDataObserver)
        fun notifyUserDataObservers(userList : ArrayList<User>)
    }

    interface UserUpdateObservable {
        fun addUserUpdateObservable(observer : DatabaseObserver.UserUpdateObserver)
        fun removeUserUpdateObservable(observer : DatabaseObserver.UserUpdateObserver)
        fun notifyUserUpdateObservers(user : User)
    }

    interface EventsObservable {
        fun addEventsObservable(observer : DatabaseObserver.EventsObserver)
        fun removeEventsObservable(observer : DatabaseObserver.EventsObserver)
        fun notifyEventsObservers(events : List<Event>)
    }

    interface EventInsertObservable {
        fun addEventInsertObservable(observer : DatabaseObserver.EventInsertObserver)
        fun removeEventInsertObservable(observer : DatabaseObserver.EventInsertObserver)
        fun notifyEventInsertObservers(isSuccessful : Boolean)
    }

    interface EventDeleteObservable {
        fun addEventDeleteObservable(observer : DatabaseObserver.EventDeleteObserver)
        fun removeEventDeleteObservable(observer : DatabaseObserver.EventDeleteObserver)
        fun notifyEventDeleteObservers(isSuccessful : Boolean)
    }

    interface EventUpdateObservable {
        fun addEventUpdateObservable(observer : DatabaseObserver.EventUpdateObserver)
        fun removeEventUpdateObservable(observer : DatabaseObserver.EventUpdateObserver)
        fun notifyEventUpdateObservers(events : List<Event>)
    }

}