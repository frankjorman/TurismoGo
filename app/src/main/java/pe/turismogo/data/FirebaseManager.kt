package pe.turismogo.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import pe.turismogo.model.domain.User
import pe.turismogo.observable.auth.AuthObserver
import pe.turismogo.observable.rtdatabase.DatabaseManagerObservable
import pe.turismogo.observable.rtdatabase.DatabaseManagerObserver
import pe.turismogo.util.Constants

open class FirebaseManager : DatabaseManagerObservable.UserDataObservable, DatabaseManagerObservable.UserUpdateObservable, AuthObserver.AuthSession, AuthObserver.AuthUser {

    companion object {
        private var instance : FirebaseManager? = null

        fun getInstance(): FirebaseManager {
            if (instance == null) {
                instance = FirebaseManager()
            }
            return instance as FirebaseManager
        }

        fun destroyInstance() {
            instance = null
        }
    }

    private var user : User? = null

    private val userList : ArrayList<User> = arrayListOf()

    private val userReference = FirebaseDatabase.getInstance()//.getReference(Constants.DATABASE_USERS)
    private val userObserverList : ArrayList<DatabaseManagerObserver> = arrayListOf()

    private var userValueListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {

            Log.d(Constants.TAG_COMMON, "data fetched in firebase manager")

            if (snapshot.exists()) {
                val temporalUserList : ArrayList<User> = arrayListOf()
                for (snap in snapshot.children) {
                    val user = snap.getValue(User::class.java)
                    user?.let {
                        temporalUserList.add(it)
                    }
                }
                setUserList(temporalUserList)
            }

            isDataFetched = true
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(Constants.TAG_COMMON, "error fetching database user from Firebase Manager ${error.message}")
        }
    }

    open var isDataFetched = false


    init {
        Log.d(Constants.TAG_COMMON, "firebase manager initialized")
//        AuthManager.getInstance().addAuthSessionObserver(this)
    }

    @JvmName("setUserList")
    private fun setUserList(userList : ArrayList<User>) {
        this.userList.clear()
        this.userList.addAll(userList)
        notifyUserDataObservers(userList)
    }

    @JvmName("getUserList")
    fun getUserList() : ArrayList<User> {
       return userList
    }

//    @JvmName("getUser")
//    fun getUser() : User? {
//        return AuthManager.getInstance().getUserLogged()
//    }



    override fun notifyAuthSessionObservers(isLogged: Boolean, uid: String?, exception: Any?) {
//        if(isLogged) {
//            AuthManager.getInstance().setUserReference(uid!!)
//        }
    }

    override fun notifyAuthUserObservers(user: User?) {

    }

    override fun addUserDataObservable(observer: DatabaseManagerObserver.UserDataObserver) {
        TODO("Not yet implemented")
    }

    override fun removeUserDataObservable(observer: DatabaseManagerObserver.UserDataObserver) {
        TODO("Not yet implemented")
    }

    override fun notifyUserDataObservers(userList: ArrayList<User>) {
        TODO("Not yet implemented")
    }

    override fun addUserUpdateObservable(observer: DatabaseManagerObserver.UserUpdateObserver) {
        TODO("Not yet implemented")
    }

    override fun removeUserUpdateObservable(observer: DatabaseManagerObserver.UserUpdateObserver) {
        TODO("Not yet implemented")
    }

    override fun notifyUserUpdateObservers(user: User) {
        TODO("Not yet implemented")
    }


}