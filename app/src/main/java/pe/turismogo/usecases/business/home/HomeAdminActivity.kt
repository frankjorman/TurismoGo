package pe.turismogo.usecases.business.home

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import pe.turismogo.R
import pe.turismogo.databinding.ActivityHomeAdminBinding
import pe.turismogo.interfaces.ISetup
import pe.turismogo.usecases.business.control.AdminControlFragment
import pe.turismogo.usecases.business.management.AdminManagementFragment
import pe.turismogo.usecases.business.management.ManagementViewModel
import pe.turismogo.usecases.business.panel.AdminPanelFragment
import pe.turismogo.usecases.business.panel.PanelViewModel
import pe.turismogo.usecases.business.profile.AdminProfileFragment

class HomeAdminActivity : AppCompatActivity(), ISetup {

    private var context : Context = this
    private var activity : Activity = this
    private lateinit var binding : ActivityHomeAdminBinding

    val panelViewModel : PanelViewModel by viewModels()
    val managementViewModel : ManagementViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeAdminBinding.inflate(activity.layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        panelViewModel.init()
        managementViewModel.init()

        setDependencies()
    }

    override fun onDestroy() {
        super.onDestroy()
        panelViewModel.detach()
    }

    override fun setDependencies() {
        supportActionBar?.hide() //se oculta la barra de navegacion

        replaceFragment(AdminPanelFragment()) //se muestra el primer fragmento como inicio

        setClickEvents() //se establecen los click events
    }

    override fun setClickEvents() {
        binding.navAdminUser.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_admin_home -> replaceFragment(AdminPanelFragment())
                R.id.menu_admin_control -> replaceFragment(AdminControlFragment())
                R.id.menu_admin_profile -> replaceFragment(AdminProfileFragment())
                R.id.menu_admin_management -> replaceFragment(AdminManagementFragment())
                else -> replaceFragment(AdminPanelFragment())
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