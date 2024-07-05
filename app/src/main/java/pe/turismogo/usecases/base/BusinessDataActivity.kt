package pe.turismogo.usecases.base

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pe.turismogo.R
import pe.turismogo.assets.InputTextWatcher
import pe.turismogo.databinding.ActivityRegisterBusinessBinding
import pe.turismogo.factory.MessageFactory
import pe.turismogo.model.domain.User
import pe.turismogo.util.Constants
import pe.turismogo.util.Enums
import pe.turismogo.util.Navigation
import pe.turismogo.util.Utils

abstract class BusinessDataActivity : ActivityBase() {

    protected lateinit var binding : ActivityRegisterBusinessBinding //declaracion de view binding
    protected lateinit var dialog : androidx.appcompat.app.AlertDialog

    abstract fun isPasswordValid(password : String) : Boolean
    abstract fun actionButton()
    abstract fun setMenuConditions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //declaración de View Binding
        binding = ActivityRegisterBusinessBinding.inflate(activity.layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setDependencies() //ordenar las dependencias
    }

    override fun setDependencies() {
        supportActionBar?.hide() //se oculta la barra por defecto de la app

        binding.header.title.visibility = View.GONE //se oculta el titulo creado en el header por el mo
        binding.header.subtitle.visibility = View.GONE //se oculta el subtitulo creado en el header por el mo

        setMenuConditions()
        setClickEvents()
        setInputEvents()
    }

    override fun setClickEvents() {
        binding.header.back.setOnClickListener { finish() } //se asigna la opcion de navegar atras al boton en el header

        //se muestra temporalmente un mensaje de creación de cuenta
        binding.btnCreateBusinessAccount.setOnClickListener {
            Utils.showToast(context, context.getString(R.string.success_account_creation))
            Navigation.toLogin(context)
        }

        //se muestra temporalmente un mensaje de creación de cuenta
        binding.btnCreateBusinessAccount.setOnClickListener {
            if(validateInputs()) {
                actionButton()
            }
        }
    }

    override fun setInputEvents() {
        binding.etBusinessName.addTextChangedListener(InputTextWatcher(binding.tilBusinessName))
        binding.etLegalId.addTextChangedListener(InputTextWatcher(binding.tilLegalId))
        binding.etCity.addTextChangedListener(InputTextWatcher(binding.tilCity))
        binding.etEmailRegister.addTextChangedListener(InputTextWatcher(binding.tilEmailRegister))
        binding.etPasswordRegister.addTextChangedListener(InputTextWatcher(binding.tilPasswordRegister))
    }

    override fun validateInputs(): Boolean {
        return if(!isPasswordValid(binding.etPasswordRegister.editableText.toString())){
            binding.tilPasswordRegister.error = getString(R.string.error_password_too_short)
            false
        }
        else if(!validateInput(
                binding.etBusinessName,
                binding.tilBusinessName,
                getString(R.string.error_business_name_required)))
            false
        else if (!validateInput(
                binding.etLegalId,
                binding.tilLegalId,
                getString(R.string.error_legal_id_required)))
            false
        else if (!validateInput(
                binding.etCity,
                binding.tilCity,
                getString(R.string.error_city_required)))
            false
        else if (!validateInput(
                binding.etEmailRegister,
                binding.tilEmailRegister,
                getString(R.string.error_email_required)))
            false
        else
            true
    }

    protected fun showWaitDialog() {
        val dialogFactory = MessageFactory()
        dialog = dialogFactory.getDialog(context, MessageFactory.TYPE_WAIT).create()
        dialog.show()
    }

    protected fun hideWaitDialog() {
        try { dialog.dismiss() } catch (ignored : Exception) { /*NTD*/ }
    }
}