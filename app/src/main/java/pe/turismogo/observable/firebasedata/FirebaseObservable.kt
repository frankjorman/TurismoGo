package pe.turismogo.observable.firebasedata

import pe.turismogo.model.domain.User
import pe.turismogo.observable.rtdatabase.DatabaseManagerObserver

interface FirebaseObservable {
    fun addObserver(observer : DatabaseManagerObserver)
    fun removeObserver(observer : DatabaseManagerObserver)
    fun notifyObservers(user : User)
}