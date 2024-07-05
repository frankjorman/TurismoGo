package pe.turismogo.data

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import pe.turismogo.model.domain.User
import pe.turismogo.model.session.UserSingleton
import pe.turismogo.observable.auth.AuthObservable
import pe.turismogo.observable.auth.AuthObserver
import pe.turismogo.util.Constants

open class AuthManager :
    AuthObservable.AuthSession,
    AuthObservable.AuthCreation,
    AuthObservable.AuthRecovering {

    private var uid : String? = null
    private var email : String? = null
    private var isLogged : Boolean = false

    private var authSessionObserverList : ArrayList<AuthObserver.AuthSession> = arrayListOf()
    private var authCreationObserverList : ArrayList<AuthObserver.AuthCreation> = arrayListOf()
    private var authRecoveringObserverList : ArrayList<AuthObserver.AuthRecovering> = arrayListOf()

    //listener para el estado de autenticacion
    private val authStateListener = FirebaseAuth.AuthStateListener { task ->
        Log.d(Constants.TAG_COMMON, "auth state changed")
        setAuthLogin(task.currentUser != null, task.currentUser?.uid, task.currentUser?.email)
    }

    private val authCreationListener =
        OnCompleteListener<AuthResult> { p0 ->
            if(p0.isSuccessful) {
                UserSingleton.getInstance().getUser()?.id = p0.result?.user?.uid.toString()
            }
            notifyAuthCreationObservers(p0.isSuccessful, p0.exception)
        }

    private val authRecoveringListener = OnCompleteListener<Void> {
        notifyAuthRecoveringObservers(it.isSuccessful)
    }


    init {
        FirebaseAuth
            .getInstance()
            .addAuthStateListener(authStateListener)
    }

    private fun removeUserReference() {
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
    }

    @JvmName("createUserAccountWithEmailAndPassword")
    fun createUserAccount(user : User) {
        UserSingleton.getInstance().setUser(user)

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(authCreationListener)
    }

    @JvmName("logInWithEmailAndPassword")
    fun logInWithEmailAndPassword(email : String , password : String) {
        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful && it.result.user != null) {
                    val uid = it.result.user!!.uid
                    val emailInfo = it.result.user!!.email
                    setAuthLogin(true, uid, emailInfo)
                } else {
                    setAuthLogin(false, null, it.exception)
                }

            }
    }

    @JvmName("recoverAccountUsingEmail")
    fun recoverAccountUsingEmail(email : String) {
        FirebaseAuth.getInstance()
            .sendPasswordResetEmail(email)
            .addOnCompleteListener(authRecoveringListener)
    }

    private fun setAuthLogin(isLogged: Boolean, uid : String?, exception : Any?) {
        this.isLogged = isLogged
        this.uid = uid
        notifyAuthSessionObservers(isLogged, uid, exception)
    }

    @JvmName("getFirebaseUser")
    fun getFirebaseCurrentUser() : FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    @JvmName("isLoggedIn")
    fun isLoggedIn() : Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    @JvmName("signOut")
    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        this.uid = null
        this.email = null
        this.isLogged = false
    }

    override fun addAuthSessionObserver(observer: AuthObserver.AuthSession) {
        authSessionObserverList.add(observer)
    }

    override fun removeAuthSessionObserver(observer: AuthObserver.AuthSession) {
        authSessionObserverList.remove(observer)
    }

    override fun notifyAuthSessionObservers(isLogged : Boolean, uid : String?, exception : Any?) {
        authSessionObserverList.forEach {
            it.notifyAuthSessionObservers(isLogged, uid, exception)
        }
    }

    override fun addAuthCreationObserver(observer: AuthObserver.AuthCreation) {
        authCreationObserverList.add(observer)
    }

    override fun removeAuthCreationObserver(observer: AuthObserver.AuthCreation) {
        authCreationObserverList.remove(observer)
    }

    override fun notifyAuthCreationObservers(isSuccessfull: Boolean, message: Any?) {
        authCreationObserverList.forEach {
            it.notifyAuthCreationObservers(isSuccessfull, message)
        }
    }

    override fun addAuthRecoveringObserver(observer: AuthObserver.AuthRecovering) {
        authRecoveringObserverList.add(observer)
    }

    override fun removeAuthRecoveringObserver(observer: AuthObserver.AuthRecovering) {
        authRecoveringObserverList.remove(observer)
    }

    override fun notifyAuthRecoveringObservers(isSuccessfull: Boolean) {
        authRecoveringObserverList.forEach {
            it.notifyAuthRecoveringObservers(isSuccessfull)
        }
    }
}