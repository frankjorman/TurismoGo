package pe.turismogo.observable.auth

import pe.turismogo.model.domain.User

interface AuthObserver {

    @Deprecated("Por ahora se encuentra deprecado")
    interface AuthUser {
        fun notifyAuthUserObservers(user : User?)
    }

    interface AuthRecovering {
        fun notifyAuthRecoveringObservers(isSuccessfull: Boolean)
    }

    interface AuthCreation {
        fun notifyAuthCreationObservers(isSuccessful: Boolean, message: Any?)
    }

    interface AuthSession {
        fun notifyAuthSessionObservers(isLogged: Boolean, uid: String?, exception: Any?)
    }
}