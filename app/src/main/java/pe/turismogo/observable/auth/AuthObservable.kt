package pe.turismogo.observable.auth

interface AuthObservable {

    @Deprecated("no se usara de momento")
    interface AuthUser {
        fun addAuthUserObserver(observer : AuthObserver.AuthUser)
        fun removeAuthUserObserver(observer : AuthObserver.AuthUser)
        fun notifyAuthUserObservers(boolean : Boolean)
    }

    interface AuthSession {
        fun addAuthSessionObserver(observer : AuthObserver.AuthSession)
        fun removeAuthSessionObserver(observer : AuthObserver.AuthSession)
        fun notifyAuthSessionObservers(isLogged: Boolean, uid: String?, exception: Any?)
    }
}