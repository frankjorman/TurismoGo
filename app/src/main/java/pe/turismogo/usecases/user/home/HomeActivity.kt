package pe.turismogo.usecases.user.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import pe.turismogo.R
import pe.turismogo.databinding.ActivityHomeBinding
import pe.turismogo.usecases.base.ActivityBase
import pe.turismogo.usecases.user.dashboard.ReservationUserViewModel
import pe.turismogo.usecases.user.dashboard.UserDashboardFragment
import pe.turismogo.usecases.user.events.EventViewModel
import pe.turismogo.usecases.user.events.UserEventsFragment
import pe.turismogo.usecases.user.history.HistoryUserViewModel
import pe.turismogo.usecases.user.history.UserHistoryFragment
import pe.turismogo.usecases.user.profile.UserProfileFragment

class HomeActivity : ActivityBase()  {

    private lateinit var binding : ActivityHomeBinding //declaracion de view binding

    val eventViewModel : EventViewModel by viewModels()
    val reservationViewModel : ReservationUserViewModel by viewModels()
    val historyViewModel : HistoryUserViewModel by viewModels()

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

        eventViewModel.init()
        reservationViewModel.init()
        historyViewModel.init()

        setDependencies()
    }

    override fun setDependencies() {
        supportActionBar?.hide() //se oculta la barra por defecto de la app

        replaceFragment(UserEventsFragment()) //se muestra la vista inicial

        setClickEvents()
    }

    override fun setClickEvents() {
        //segun la seleccion de la barra de navegacion inferior se cambiaran los fragmentos respectivos
        binding.navHomeUser.setOnItemSelectedListener { item ->
            when(item.itemId) {
                //vista principal donde reside el dashboard
                R.id.menu_user_home -> replaceFragment(UserEventsFragment())
                //vista de historico aun no definida
                R.id.menu_user_dashboard -> replaceFragment(UserDashboardFragment())
                //vista de historico aun no definida
                R.id.menu_user_history -> replaceFragment(UserHistoryFragment())
                //visa de opciones, se piensa unir con el perfil de usuario
                R.id.menu_user_profile -> replaceFragment(UserProfileFragment())
                else -> replaceFragment(UserEventsFragment())
            }
        }

    }

    override fun setInputEvents() { TODO("Not yet implemented")  }

    override fun validateInputs(): Boolean { TODO("Not yet implemented") }

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