package pe.turismogo.usecases.launch

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pe.turismogo.R
import pe.turismogo.util.Constants
import pe.turismogo.util.Navigation

@SuppressLint("CustomSplashScreen")
class LaunchActivity : AppCompatActivity() {

    var context : Context = this; //referencia al contexto de la app para evitar el uso de "this"
    var activity : Activity = this; //se genera una referencia a la actividad de la app evitando el uso de "this"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_launch)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide() //se oculta la barra por defecto de la app

        //codigo que permite usar el ciclo de vida de la app para esperar 3 segundos
        Handler(Looper.getMainLooper())
            .postDelayed({
                         Navigation.toLogin(context) //Navega a la actividad de Inicio de sesion
            }, Constants.ONE_SEC * 3)

    }
}