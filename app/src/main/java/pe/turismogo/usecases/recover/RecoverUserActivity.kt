package pe.turismogo.usecases.recover

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pe.turismogo.R
import pe.turismogo.assets.InputTextWatcher
import pe.turismogo.data.DatabaseManager
import pe.turismogo.databinding.ActivityRecoverUserBinding
import pe.turismogo.factory.MessageFactory
import pe.turismogo.observable.auth.AuthObserver
import pe.turismogo.usecases.base.ActivityBase
import pe.turismogo.util.Constants
import pe.turismogo.util.Utils

class RecoverUserActivity
    : ActivityBase(),
    AuthObserver.AuthRecovering {

    private lateinit var binding : ActivityRecoverUserBinding
    private lateinit var dialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRecoverUserBinding.inflate(activity.layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        DatabaseManager.getInstance().getAuth().addAuthRecoveringObserver(this)

        setDependencies()
    }

    override fun onDestroy() {
        super.onDestroy()
        DatabaseManager.getInstance().getAuth().removeAuthRecoveringObserver(this)
    }

    override fun setDependencies() {
        supportActionBar?.hide()

        binding.header.title.visibility = View.GONE
        binding.header.subtitle.visibility = View.GONE

        setClickEvents()
        setInputEvents()
    }

    override fun setClickEvents() {
        binding.header.back.setOnClickListener { finish() }

        binding.btnRecoverAccount.setOnClickListener {
            if(validateInputs()) {
                showWaitDialog()
                DatabaseManager.getInstance().getAuth().recoverAccountUsingEmail(binding.etEmailRecover.text.toString())
            }
        }
    }

    override fun setInputEvents() {
        binding.etEmailRecover.addTextChangedListener(InputTextWatcher(binding.tilEmailRecover))
    }

    override fun validateInputs(): Boolean {
        return validateInput(binding.etEmailRecover, binding.tilEmailRecover, context.getString(R.string.error_email_required))
    }

    override fun notifyAuthRecoveringObservers(isSuccessfull: Boolean) {
        hideWaitDialog()
        if(isSuccessfull) {
            Utils.showToast(context, context.getString(R.string.success_email_recover_sent))
            finish()
        } else
            Utils.showSnackBar(binding.root, context.getString(R.string.error_email_recover))
    }

    private fun showWaitDialog() {
        val messageFactory = MessageFactory()
        dialog = messageFactory.getDialog(context, MessageFactory.TYPE_WAIT).create()
        dialog.show()
    }

    private fun hideWaitDialog() {
        try { dialog.dismiss() } catch (ignored : Exception) { /*NTD*/ }
    }

}