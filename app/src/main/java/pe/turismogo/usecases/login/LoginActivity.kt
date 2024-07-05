package pe.turismogo.usecases.login

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

import pe.turismogo.R
import pe.turismogo.assets.InputTextWatcher
import pe.turismogo.data.DatabaseManager
import pe.turismogo.databinding.ActivityLoginBinding
import pe.turismogo.factory.MessageFactory
import pe.turismogo.model.domain.User
import pe.turismogo.observable.auth.AuthObserver
import pe.turismogo.observable.rtdatabase.DatabaseObserver
import pe.turismogo.usecases.base.ActivityBase
import pe.turismogo.util.Constants
import pe.turismogo.util.Enums
import pe.turismogo.util.Navigation
import pe.turismogo.util.Utils

class LoginActivity :
    ActivityBase(),
    DatabaseObserver.UserUpdateObserver,
    AuthObserver.AuthSession {

    private lateinit var binding : ActivityLoginBinding
    private var shouldKeepScreenCondition = true

    private lateinit var dialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        //muestra el nuevo estilo de splash screen estableciendo la condicion de mantenerlo
        installSplashScreen().setKeepOnScreenCondition { shouldKeepScreenCondition }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(activity.layoutInflater) //view binding
        setContentView(binding.root)

        setDependencies() //se establecen las dependencias
    }

    override fun onResume() {
        super.onResume()
        //agregamos el listener para el estado del usuario
        DatabaseManager.getInstance().addUserUpdateObservable(this)
        DatabaseManager.getInstance().getAuth().addAuthSessionObserver(this)
    }

    override fun onPause() {
        super.onPause()
        //quitamos el listener para el estado del usuario
        DatabaseManager.getInstance().removeUserUpdateObservable(this)
        DatabaseManager.getInstance().getAuth().removeAuthSessionObserver(this)
    }

    override fun setDependencies() {

        supportActionBar?.hide() //se oculta la barra de navegacion si esta presente

        if(!DatabaseManager.getInstance().getAuth().isLoggedIn()) {
            shouldKeepScreenCondition = false
            setClickEvents()
            setInputEvents()
        }
    }

    override fun setClickEvents() {
        binding.btnLoginContinue.setOnClickListener {
            if(validateInputs()) {
                val dialogFactory = MessageFactory()
                dialog = dialogFactory.getDialog(context, MessageFactory.TYPE_WAIT).create()
                dialog.show()

                val email = binding.etEmailLogin.editableText.toString()
                val password = binding.etPasswordLogin.editableText.toString()

                DatabaseManager
                    .getInstance()
                    .getAuth().logInWithEmailAndPassword(email, password)
            }
        }

        binding.tvForgotAccount.setOnClickListener {
            Navigation.toRecoverPassword(context)
        }

        binding.tvRegisterAccount.setOnClickListener {
            Navigation.toRegisterSelection(context)
        }
    }

    override fun setInputEvents() {

        binding.etEmailLogin.addTextChangedListener(InputTextWatcher(binding.tilEmailLogin))
        binding.etPasswordLogin.addTextChangedListener(InputTextWatcher(binding.tilPasswordLogin))
    }

    override fun validateInputs(): Boolean {
        return if(!validateInput(
                binding.etEmailLogin,
                binding.tilEmailLogin,
                context.getString(R.string.error_email_required)))
            false
        else if (!validateInput(
                binding.etPasswordLogin,
                binding.tilPasswordLogin,
                context.getString(R.string.error_password_required)))
            false
        else
            true
    }

    override fun notifyUserUpdateObservers(user: User) {
        Log.d(Constants.TAG_COMMON, "user is not null")
        Log.d(Constants.TAG_COMMON, "user id: ${user.id} user role: ${user.role}")
        Log.d(Constants.TAG_COMMON, "user role to compare: ${Enums.Role.BUSINESS.name}")
        when(user.role) {
            Enums.Role.USER.name -> {
                Utils.showToast(context, context.getString(R.string.prompt_welcome))
                Navigation.toUserHomeMenu(context)
                return
            }
            Enums.Role.BUSINESS.name ->  {
                Utils.showToast(context, context.getString(R.string.prompt_welcome))
                Navigation.toAdminHomeMenu(context)
                return
            }
            else -> {
                shouldKeepScreenCondition = false

                DatabaseManager.getInstance().getAuth().signOut()
                Utils.showSnackBar(binding.root, context.getString(R.string.error_fetching_user_data))
            }
        }
    }

    override fun notifyAuthSessionObservers(isLogged: Boolean, uid: String?, exception: Any?) {
        if(!isLogged) {
            try {dialog.dismiss() } catch (ignored : Exception) { /*NTD*/ }
            when(exception) {
                is FirebaseAuthInvalidCredentialsException -> {
                    Utils.showSnackBar(binding.root, context.getString(R.string.error_invalid_credentials))
                }
                is FirebaseAuthEmailException -> {
                    Utils.showSnackBar(binding.root, context.getString(R.string.error_email_not_found))
                }
                is FirebaseAuthInvalidUserException -> {
                    Utils.showSnackBar(binding.root, context.getString(R.string.error_user_not_found))
                }
                else -> {
                    Utils.showSnackBar(binding.root, context.getString(R.string.error_fetching_user_data))
                }
            }
        }
    }
}