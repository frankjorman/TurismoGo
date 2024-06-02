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
import pe.turismogo.util.Navigation

class LoginActivity : AppCompatActivity() {

    var context : Context = this
    var activity : Activity = this
    lateinit var binding : ActivityLoginBinding

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
            Navigation.toUserHomeMenu(context)
        }

        binding.tvForgotAccount.setOnClickListener {
            Navigation.toRecoverPassword(context)
        }

        binding.tvRegisterAccount.setOnClickListener {
            Navigation.toRegisterSelection(context)
        }

    }
}