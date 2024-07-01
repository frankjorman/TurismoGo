package pe.turismogo.usecases.login

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pe.turismogo.R
import pe.turismogo.databinding.ActivityLoginBinding
import pe.turismogo.util.Constants
import pe.turismogo.util.Navigation

class LoginActivity : AppCompatActivity() {

    var context : Context = this
    var activity : Activity = this
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(activity.layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        binding.btnLoginContinue.setOnClickListener {
            val email = binding.etEmailLogin.editableText?.toString()
            val password = binding.etPasswordLogin.editableText?.toString()

            if(!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                if(validateTemporalAdmin(email, password))
                    Navigation.toAdminHomeMenu(context)
                else if (validateTemporalUser(email, password))
                    Navigation.toUserHomeMenu(context)
                else
                    Constants.showSnackBar(binding.root, "El usuario no Existe")
            } else
                Constants.showSnackBar(binding.root, "Debe escribir el usuario y pass")
        }

        binding.tvForgotAccount.setOnClickListener {
            Navigation.toRecoverPassword(context)
        }

        binding.tvRegisterAccount.setOnClickListener {
            Navigation.toRegisterSelection(context)
        }

    }


    fun validateTemporalUser(user : String, password : String) : Boolean {
        return user == Constants.USER_TEMPORAL_USER && password == Constants.USER_TEMPORAL_PASSWORD
    }

    fun validateTemporalAdmin(user : String, password : String) : Boolean {
        return user == Constants.ADMIN_TEMPORAL_USER && password == Constants.ADMIN_TEMPORAL_PASSWORD
    }
}