package pe.turismogo.usecases.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import pe.turismogo.R
import pe.turismogo.data.DatabaseManager
import pe.turismogo.databinding.FragmentProfileBinding
import pe.turismogo.factory.MessageFactory
import pe.turismogo.model.domain.User
import pe.turismogo.observable.auth.AuthObserver
import pe.turismogo.observable.rtdatabase.DatabaseManagerObserver
import pe.turismogo.model.session.UserSingleton
import pe.turismogo.util.Constants
import pe.turismogo.util.Navigation

abstract class FragmentProfile : FragmentBase(),
    AuthObserver.AuthSession,
    DatabaseManagerObserver.UserUpdateObserver {

    protected lateinit var binding : FragmentProfileBinding

    abstract fun updateUserData(user : User)


    override fun onResume() {
        super.onResume()
        UserSingleton.getInstance().getUser()?.let { user ->
            updateUserData(user)
        }
        DatabaseManager.getInstance().getAuth().addAuthSessionObserver(this)
        DatabaseManager.getInstance().addUserUpdateObservable(this)
    }

    override fun onPause() {
        super.onPause()
        DatabaseManager.getInstance().getAuth().removeAuthSessionObserver(this)
        DatabaseManager.getInstance().removeUserUpdateObservable(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        setDependencies()
        return binding.root
    }

    override fun setDependencies() {

        binding.content.option1.root.visibility = View.INVISIBLE
        binding.content.option2.root.visibility = View.INVISIBLE

        binding.content.option3.optionTitle.text = fragContext.getString(R.string.action_sign_out)
        binding.content.option3.optionDescription.text = fragContext.getString(R.string.prompt_logout)
        binding.content.option3.icon.background = AppCompatResources.getDrawable(fragContext, R.drawable.icon_power)

        setClickEvents()
    }

    override fun setClickEvents() {
        binding.content.option3.root.setOnClickListener {
            DatabaseManager.getInstance().getAuth().signOut()
        }

        binding.content.option3.root.setOnClickListener {
            val dialogFactory = MessageFactory()
            val dialog = dialogFactory.getDialog(fragContext, MessageFactory.TYPE_WAIT).create()
            dialog.show()

            DatabaseManager.getInstance().getAuth().signOut()
        }
    }

    private fun delay() {
        val runnable = Runnable {
            Constants.showToast(fragContext, fragContext.getString(R.string.success_account_sign_out))
            Navigation.toLogin(fragContext)
        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(runnable, 1500)
    }

    override fun setInputEvents() {
        TODO("Not yet implemented")
    }

    override fun notifyAuthSessionObservers(isLogged: Boolean, uid: String?, exception: Any?) {
        if(!isLogged) {
            delay()
        }
    }

    override fun notifyUserUpdateObservers(user: User) {
        updateUserData(user)
    }
}