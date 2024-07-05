package pe.turismogo.usecases.base

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import pe.turismogo.R
import pe.turismogo.assets.InputTextWatcher
import pe.turismogo.data.AuthManager
import pe.turismogo.databinding.ActivityRegisterUserBinding
import pe.turismogo.factory.DatePickerFactory
import pe.turismogo.factory.MessageFactory
import pe.turismogo.model.domain.User
import pe.turismogo.model.session.UserSingleton
import pe.turismogo.observable.auth.AuthObserver
import pe.turismogo.observable.rtdatabase.DatabaseObserver
import pe.turismogo.util.Constants
import pe.turismogo.util.Navigation

abstract class UserDataActivity : ActivityBase() {

    protected lateinit var binding : ActivityRegisterUserBinding //ViewBinding
    protected lateinit var dialog : androidx.appcompat.app.AlertDialog

    abstract fun actionButton()
    abstract fun setMenuConditions()
    abstract fun isPasswordValid(password : String) : Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterUserBinding.inflate(activity.layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setDependencies()
    }

    override fun setDependencies() {
        supportActionBar?.hide() //se oculta la barra por defecto de la app

        binding.header.title.visibility = View.GONE //se oculta el titulo del header creado por el momento
        binding.header.subtitle.visibility = View.GONE //se oculta el subtitulo del header creardo por el momento

        setMenuConditions()

        setClickEvents()
        setInputEvents()
    }

    override fun setClickEvents() {
        binding.header.back.setOnClickListener { finish() }  //se asigna la opcion de navegar atras al boton en el header

        binding.btnCreateUserAccount.setOnClickListener {
            if(validateInputs()) {
                actionButton()
            }
        }

        binding.tilDateOfBirth.setEndIconOnClickListener {
            val datePickerFactory = DatePickerFactory()
            val datePicker =
                datePickerFactory
                    .getPickerDialog(
                        context,
                        DatePickerFactory.TYPE_BWD_DATE,
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

    override fun validateInputs(): Boolean {
        return if(!isPasswordValid(binding.etPasswordRegister.editableText.toString())) {
            binding.tilPasswordRegister.error = getString(R.string.error_password_too_short)
            false
        } else if(!validateInput(
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

    protected val onDateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val formattedDayOfMonth = String.format("%02d", dayOfMonth)
            val formattedMonth = String.format("%02d", month)
            val date = "$formattedDayOfMonth-$formattedMonth-$year"
            binding.etDateOfBirth.setText(date)
        }

}