package pe.turismogo.usecases.register

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pe.turismogo.R
import pe.turismogo.databinding.ActivityRegisterSelectionBinding
import pe.turismogo.util.Constants
import pe.turismogo.util.Navigation
import java.util.Arrays

class RegisterSelectionActivity : AppCompatActivity() {

    var context : Context = this
    var activity : Activity = this
    lateinit var binding : ActivityRegisterSelectionBinding
    lateinit var roleList : List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterSelectionBinding.inflate(activity.layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide() //se oculta la barra por defecto de la app

        binding.header.title.visibility = View.GONE //se oculta el titulo del header creado por el momento
        binding.header.subtitle.visibility = View.GONE //se oculta el subtitulo del header creardo por el momento

        binding.actRoleSelection.isEnabled = false //se bloquea la opcion de poder editar el campo de texto
        binding.actRoleSelection.setText(context.getString(R.string.select)) //se escribe un texto descriptivo de "call to action"

        roleList = listOf<String>(*context.resources.getStringArray(R.array.account_type)) //se llena una lista de String con textos de recursos
        //se llena un adaptador generico para permitir que se muestren los strings de los recursos .xml
        val roleAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            roleList
        )
        binding.actRoleSelection.setAdapter(roleAdapter) //se asigna el adaptador al dropdown

        binding.header.back.setOnClickListener { finish() } //se asigna la opcion de navegar atras al boton en el header

        //logica para permitir navegacion hacia los menus correspondiente segun la selecccion de usuario
        binding.btnRegisterOption.setOnClickListener {
            when(binding.actRoleSelection.text.toString()) {
                context.getString(R.string.business_account) -> Navigation.toRegisterBusiness(context)
                context.getString(R.string.user_account) -> Navigation.toRegisterUser(context)
                else -> Constants.showSnackBar(binding.root, context.getString(R.string.invalid_selection)) //si no selecciona nada se muestra un mensaje
            }
        }


    }
}