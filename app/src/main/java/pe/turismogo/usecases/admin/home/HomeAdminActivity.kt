package pe.turismogo.usecases.admin.home

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import pe.turismogo.R
import pe.turismogo.databinding.ActivityHomeAdminBinding
import pe.turismogo.databinding.ActivityHomeBinding
import pe.turismogo.usecases.admin.control.AdminControlFragment
import pe.turismogo.usecases.admin.management.AdminManagementFragment
import pe.turismogo.usecases.admin.panel.AdminPanelFragment
import pe.turismogo.usecases.admin.profile.AdminProfileFragment

class HomeAdminActivity : AppCompatActivity() {

    var context : Context = this
    var activity : Activity = this
    private lateinit var binding : ActivityHomeAdminBinding

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

        supportActionBar?.hide()
        replaceFragment(AdminPanelFragment())

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