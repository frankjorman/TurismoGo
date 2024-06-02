package pe.turismogo.usecases.home

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import pe.turismogo.R
import pe.turismogo.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    var context : Context = this //referencia al contexto de la app para evitar el uso de "this"
    var activity : Activity = this //se genera una referencia a la actividad de la app evitando el uso de "this"
    lateinit var binding : ActivityHomeBinding //declaracion de view binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(activity.layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide() //se oculta la barra por defecto de la app

        replaceFragment(UserDashboardFragment()) //se muestra la vista inicial

        //segun la seleccion de la barra de navegacion inferior se cambiaran los fragmentos respectivos
        binding.navHomeUser.setOnItemSelectedListener { item ->
            when(item.itemId) {
                //vista principal donde reside el dashboard
                R.id.menu_user_home -> replaceFragment(UserDashboardFragment())
                //vista de historico aun no definida
                R.id.menu_user_history -> replaceFragment(UserHistoryFragment())
                //visa de opciones, se piensa unir con el perfil de usuario
                R.id.menu_user_settings -> replaceFragment(UserSettingsFragment())
                //vista de perfil de usuario
                R.id.menu_user_profile -> replaceFragment(UserProfileFragment())
                else -> replaceFragment(UserDashboardFragment())
            }
        }



    }

    /**
     * Funcion que permite reemplazar un fragmento dentro del frame de la actividad
     */
    private fun replaceFragment(fragment : Fragment): Boolean {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.frame.id, fragment)
        fragmentTransaction.commit()
        return true
    }
}