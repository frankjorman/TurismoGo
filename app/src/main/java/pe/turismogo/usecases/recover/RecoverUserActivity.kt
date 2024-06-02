package pe.turismogo.usecases.recover

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pe.turismogo.R
import pe.turismogo.databinding.ActivityRecoverUserBinding
import pe.turismogo.util.Constants

class RecoverUserActivity : AppCompatActivity() {

    var context : Context = this
    var activity : Activity = this
    lateinit var binding : ActivityRecoverUserBinding

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

        supportActionBar?.hide()

        binding.header.title.visibility = View.GONE
        binding.header.subtitle.visibility = View.GONE

        binding.header.back.setOnClickListener { finish() }
        binding.btnRecoverAccount.setOnClickListener {
            Constants.showToast(context, "Email Sent")
            finish()
        }
    }
}