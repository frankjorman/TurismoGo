package pe.turismogo.usecases.user.dashboard.evaluate

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pe.turismogo.R
import pe.turismogo.databinding.ActivityUserEvaluateEventBinding

class UserEvaluateEventActivity : AppCompatActivity() {

    private val context : Context = this
    private val activity : Activity = this
    private lateinit var binding : ActivityUserEvaluateEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUserEvaluateEventBinding.inflate(activity.layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        binding.header.back.setOnClickListener { finish() }
    }
}