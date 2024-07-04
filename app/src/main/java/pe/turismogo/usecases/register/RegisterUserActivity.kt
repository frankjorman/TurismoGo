package pe.turismogo.usecases.register

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import pe.turismogo.R
import pe.turismogo.assets.InputTextWatcher
import pe.turismogo.databinding.ActivityRegisterUserBinding
import pe.turismogo.factory.DatePickerFactory
import pe.turismogo.model.domain.User
import pe.turismogo.usecases.base.ActivityCreateAccount
import pe.turismogo.util.Navigation

class RegisterUserActivity : ActivityCreateAccount() {

    private lateinit var binding : ActivityRegisterUserBinding //ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //declaracion de view binding
        binding = ActivityRegisterUserBinding.inflate(activity.layoutInflater)
        setContentView(binding.root)

        //ordenar las dependencias
        setDependencies()
    }

    override fun setDependencies() {
        supportActionBar?.hide() //se oculta la barra por defecto de la app

        binding.header.title.visibility = View.GONE //se oculta el titulo del header creado por el momento
        binding.header.subtitle.visibility = View.GONE //se oculta el subtitulo del header creardo por el momento

        binding.btnCreateUserAccount.isEnabled = false

        binding.cbTermsAndConditions.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> binding.btnCreateUserAccount.isEnabled = true
                false -> binding.btnCreateUserAccount.isEnabled = false
            }
        }

        setClickEvents()
        setInputEvents()
    }

    override fun setClickEvents() {
        binding.header.back.setOnClickListener { finish() }  //se asigna la opcion de navegar atras al boton en el header

        //se muestra temporalmente un mensaje de creación de cuenta
        binding.btnCreateUserAccount.setOnClickListener {
            if(validateInputs()) {
                val user : User = User()
                user.name = binding.etNames.editableText.toString()
                user.lastname = binding.etLastNames.editableText.toString()
                user.documentId = binding.etDocumentId.editableText.toString()
                user.email = binding.etEmailRegister.editableText.toString()
                user.password = binding.etPasswordRegister.editableText.toString()
                binding.etPhone.editableText.let { user.phone = it.toString() }

                createUserAccount(user, binding.root)
            }
        }

        binding.tilDateOfBirth.setEndIconOnClickListener {
            val datePickerFactory = DatePickerFactory()
            val datePicker =
                datePickerFactory
                    .getPickerDialog(
                        context,
                        DatePickerFactory.TYPE_FWD_DATE,
                        onDateSetListener)
            datePicker.show()
        }
    }

    override fun setInputEvents() {
        binding.etNames.addTextChangedListener(InputTextWatcher(binding.tilNames))
        binding.etLastNames.addTextChangedListener(InputTextWatcher(binding.tilLastNames))
        binding.etDocumentId.addTextChangedListener(InputTextWatcher(binding.tilDocumentId))
        binding.etEmailRegister.addTextChangedListener(InputTextWatcher(binding.tilEmailRegister))
        binding.etPasswordRegister.addTextChangedListener(InputTextWatcher(binding.tilPasswordRegister))
    }

    override fun validateInputs() : Boolean {
        return if(!validateInput(
                binding.etNames,
                binding.tilNames,
                getString(R.string.error_name_required)))
            false
        else if (!validateInput(
                binding.etLastNames,
                binding.tilLastNames,
                getString(R.string.error_last_name_required)))
            false
        else if (!validateInput(
            binding.etDateOfBirth,
            binding.tilDateOfBirth,
            context.getString(R.string.error_date_birth_required)))
            false
        else if (!validateInput(
                binding.etPhone,
                binding.tilPhone,
                context.getString(R.string.error_phone_required)))
            false
        else if (!validateInput(
                binding.etDocumentId,
                binding.tilDocumentId,
                getString(R.string.error_document_id_required)))
            false
        else if (!validateInput(
                binding.etEmailRegister,
                binding.tilEmailRegister,
                getString(R.string.error_email_required)))
            false
        else if (!validateInput(
                binding.etPasswordRegister,
                binding.tilPasswordRegister,
                getString(R.string.error_password_required)))
            false
        else
            true
    }

    private val onDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val formattedDayOfMonth = String.format("%02d", dayOfMonth)
            val formattedMonth = String.format("%02d", month)
            val date = "$formattedDayOfMonth-$formattedMonth-$year"
            binding.etDateOfBirth.setText(date)
        }

    override fun navigateToAccountMainMenu(context: Context) {
        Navigation.toUserHomeMenu(context)
    }
}