package pe.turismogo.usecases.register

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pe.turismogo.R
import pe.turismogo.databinding.ActivityRegisterBusinessBinding
import pe.turismogo.databinding.ActivityRegisterUserBinding
import pe.turismogo.util.Constants
import pe.turismogo.util.Navigation

class RegisterBusinessActivity : AppCompatActivity() {

    var context : Context = this //referencia al contexto de la app para evitar el uso de "this"
    var activity : Activity = this //se genera una referencia a la actividad de la app evitando el uso de "this"
    lateinit var binding : ActivityRegisterBusinessBinding //declaracion de view binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBusinessBinding.inflate(activity.layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide() //se oculta la barra por defecto de la app

        binding.header.title.visibility = View.GONE //se oculta el titulo creado en el header por el mo
        binding.header.subtitle.visibility = View.GONE

        binding.header.back.setOnClickListener { finish() } //se asigna la opcion de navegar atras al boton en el header

        //se muestra temporalmente un mensaje de creación de cuenta
        binding.btnCreateBusinessAccount.setOnClickListener {
            Constants.showToast(context, context.getString(R.string.success_account_creation))
            Navigation.toLogin(context)
        }
    }
}