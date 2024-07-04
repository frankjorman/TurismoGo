package pe.turismogo.usecases.register

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import pe.turismogo.R
import pe.turismogo.assets.InputTextWatcher
import pe.turismogo.databinding.ActivityRegisterBusinessBinding
import pe.turismogo.model.domain.User
import pe.turismogo.usecases.base.ActivityCreateAccount
import pe.turismogo.util.Constants
import pe.turismogo.util.Enums
import pe.turismogo.util.Navigation

class RegisterBusinessActivity : ActivityCreateAccount() {

    private lateinit var binding : ActivityRegisterBusinessBinding //declaracion de view binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //declaración de View Binding
        binding = ActivityRegisterBusinessBinding.inflate(activity.layoutInflater)
        setContentView(binding.root)

        setDependencies() //ordenar las dependencias
    }

    override fun setDependencies() {
        supportActionBar?.hide() //se oculta la barra por defecto de la app

        binding.header.title.visibility = View.GONE //se oculta el titulo creado en el header por el mo
        binding.header.subtitle.visibility = View.GONE //se oculta el subtitulo creado en el header por el mo

        binding.btnCreateBusinessAccount.isEnabled = false //se deshabilita el boton de creacion de cuenta

        //se agrega un listener para revisar cuando el check box esta o no seleccionado
        binding.cbTermsAndConditions.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked) {
                true -> binding.btnCreateBusinessAccount.isEnabled = true
                false -> binding.btnCreateBusinessAccount.isEnabled = false
            }
        }

        setClickEvents() //se asignan los eventos de click
        setInputEvents() //se asignan los eventos de entradas de texto
    }

    override fun setClickEvents() {
        binding.header.back.setOnClickListener { finish() } //se asigna la opcion de navegar atras al boton en el header

        //se muestra temporalmente un mensaje de creación de cuenta
        binding.btnCreateBusinessAccount.setOnClickListener {
            Constants.showToast(context, context.getString(R.string.success_account_creation))
            Navigation.toLogin(context)
        }

        //se muestra temporalmente un mensaje de creación de cuenta
        binding.btnCreateBusinessAccount.setOnClickListener {
            if(validateInputs()) {
                val user = User()
                user.businessName = binding.etBusinessName.editableText.toString()
                user.legalId = binding.etLegalId.editableText.toString()
                user.city = binding.etCity.editableText.toString()
                user.email = binding.etEmailRegister.editableText.toString()
                user.password = binding.etPasswordRegister.editableText.toString()
                user.role = Enums.Role.BUSINESS.name

                binding.etNamesLastnames.editableText.let {
                    if(it.contains(Constants.SPACE)) {
                        val names = it.split(Constants.SPACE)
                        user.name = names[0]
                        user.lastname = names[1]
                    } else {
                        user.name = it.toString()
                    }
                }
                binding.etBusinessRole.editableText.let { user.businessRole = it.toString() }
                binding.etPhone.editableText.let { user.phone = it.toString() }

                createUserAccount(user, binding.root)
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

    override fun validateInputs() : Boolean {
        return if(!validateInput(
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
        else if (!validateInput(
                binding.etPasswordRegister,
                binding.tilPasswordRegister,
                getString(R.string.error_password_too_short)))
            false
        else
            true
    }

    override fun navigateToAccountMainMenu(context: Context) {
        Navigation.toAdminHomeMenu(context)
    }
}