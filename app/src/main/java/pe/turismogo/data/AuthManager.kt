package pe.turismogo.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import pe.turismogo.observable.auth.AuthObservable
import pe.turismogo.observable.auth.AuthObserver
import pe.turismogo.util.Constants

open class AuthManager : AuthObservable.AuthSession {

    private var uid : String? = null
    private var email : String? = null
    private var isLogged : Boolean = false

    private var authSessionObserverList : ArrayList<AuthObserver.AuthSession> = arrayListOf()

    //listener para el estado de autenticacion
    private val authStateListener = FirebaseAuth.AuthStateListener { task ->
        Log.d(Constants.TAG_COMMON, "auth state changed")
        setAuthLogin(task.currentUser != null, task.currentUser?.uid, task.currentUser?.email)
    }

    init {
        FirebaseAuth
            .getInstance()
            .addAuthStateListener(authStateListener)
    }

    private fun removeUserReference() {
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
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
}