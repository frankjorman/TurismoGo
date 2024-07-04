package pe.turismogo.model.session

import pe.turismogo.model.domain.User

class UserSingleton {

    companion object { //Singleton Pattern
        private var instance : UserSingleton? = null;

        fun getInstance() : UserSingleton {
            if(instance == null) {
                instance = UserSingleton()
            }
            return instance as UserSingleton
        }
    }

    private var user : User? = null

    fun setUser(user : User?) {
        this.user = user
    }

    fun getUser() : User? {
        return user
    }

}