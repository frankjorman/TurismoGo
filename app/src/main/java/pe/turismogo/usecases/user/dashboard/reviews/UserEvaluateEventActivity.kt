package pe.turismogo.usecases.user.dashboard.reviews

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import pe.turismogo.R
import pe.turismogo.assets.InputTextWatcher
import pe.turismogo.data.CacheManager
import pe.turismogo.data.DatabaseManager
import pe.turismogo.databinding.ActivityUserEvaluateEventBinding
import pe.turismogo.factory.MessageFactory
import pe.turismogo.model.domain.Review
import pe.turismogo.model.session.UserSingleton
import pe.turismogo.observable.rtdatabase.DatabaseManagerObserver
import pe.turismogo.usecases.base.ActivityBase
import pe.turismogo.util.Constants
import pe.turismogo.util.Navigation

class UserEvaluateEventActivity : ActivityBase(),
    DatabaseManagerObserver.EventInsertObserver {

    private lateinit var binding : ActivityUserEvaluateEventBinding

    private lateinit var btnStar1 : MaterialButton
    private lateinit var btnStar2 : MaterialButton
    private lateinit var btnStar3 : MaterialButton
    private lateinit var btnStar4 : MaterialButton
    private lateinit var btnStar5 : MaterialButton

    private var selectedStars = mutableSetOf<MaterialButton>()

    private lateinit var dialog : AlertDialog

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
        DatabaseManager.getInstance().addEventInsertObservable(this)
        setDependencies()
    }

    override fun onDestroy() {
        super.onDestroy()
        DatabaseManager.getInstance().removeEventInsertObservable(this)
    }

    override fun setDependencies() {
        supportActionBar?.hide()
        binding.header.title.text = context.getString(R.string.prompt_your_opinion)
        binding.header.subtitle.text = context.getString(R.string.desc_menu_review_publish)

        btnStar1 = binding.btnStar1
        btnStar2 = binding.btnStar2
        btnStar3 = binding.btnStar3
        btnStar4 = binding.btnStar4
        btnStar5 = binding.btnStar5

        binding.btnPublishReview.isEnabled = DatabaseManager.getInstance().isUserAvailableForReview(activity)

        setClickEvents()
        setInputEvents()
    }

    override fun setClickEvents() {
        binding.header.back.setOnClickListener { finish() }
        btnStar1.setOnClickListener { toggleStar(btnStar1) }
        btnStar2.setOnClickListener { toggleStar(btnStar2) }
        btnStar3.setOnClickListener { toggleStar(btnStar3) }
        btnStar4.setOnClickListener { toggleStar(btnStar4) }
        btnStar5.setOnClickListener { toggleStar(btnStar5) }
        binding.btnPublishReview.setOnClickListener {
            if(validateInputs()) {
                saveReview()
            }
        }
    }

    override fun setInputEvents() {
        binding.etReviewComment.addTextChangedListener(InputTextWatcher(binding.tilReviewComment))
    }

    override fun validateInputs(): Boolean {
        return validateInput(
                binding.etReviewComment,
                binding.tilReviewComment,
                context.getString(R.string.error_review_comment_required))
    }

    private fun saveReview() {
        UserSingleton.getInstance().getUser()?.let { user ->
            val event = CacheManager.getInstance().getEventCache(activity)


            val review = Review()
            review.rating = selectedStars.size
            review.comment = binding.etReviewComment.text.toString()

            val messageFactory = MessageFactory()
            dialog = messageFactory.getDialog(context, MessageFactory.TYPE_WAIT).create()
            dialog.show()
            DatabaseManager.getInstance().saveReview(event, review)

        }



    }

    private fun toggleStar(button : MaterialButton) {
        if(selectedStars.contains(button)) {
            // Si ya está seleccionada, desmarcar y deseleccionar las anteriores
            button.icon = AppCompatResources.getDrawable(context, R.drawable.icon_star_0)
            selectedStars.remove(button)
            deselectFollowingStars(button)
        }else {
            // Si no está seleccionada, marcar y seleccionar las anteriores
            button.icon = AppCompatResources.getDrawable(context, R.drawable.icon_star_2)
            selectedStars.add(button)
            selectPreviousStars(button)
        }
    }

    @Deprecated("no funciono como debia")
    private fun deselectPreviousStars(currentButton : MaterialButton) {
        //desmarcar las estrellas anteriores
        selectedStars.forEach { button ->
            if(button != currentButton) {
                button.icon = AppCompatResources.getDrawable(context, R.drawable.icon_star_0)
                selectedStars.remove(button)
            }
        }
    }

    private fun selectPreviousStars(currentButton: MaterialButton) {
        //marcar las estrellas anterior a la actual
        val buttonsToSelect = listOf(btnStar1, btnStar2, btnStar3, btnStar4, btnStar5)
            .takeWhile { it != currentButton }
        for (button in buttonsToSelect) {
            button.icon = AppCompatResources.getDrawable(context, R.drawable.icon_star_2)
            selectedStars.add(button)
        }
    }

    private fun deselectFollowingStars(currentButton: MaterialButton) {
        // Desmarcar las estrellas siguientes a la actual
        val buttonsToDeselect = listOf(btnStar1, btnStar2, btnStar3, btnStar4, btnStar5)
            .dropWhile { it != currentButton }
            .drop(1) // Excluir la estrella actual
        for (button in buttonsToDeselect) {
            button.icon = AppCompatResources.getDrawable(context, R.drawable.icon_star_0)
            selectedStars.remove(button)
        }
    }

    override fun notifyEventInsertObservers(isSuccessful: Boolean) {
        dialog.dismiss()
        if(isSuccessful) {
            Constants.showToast(context, context.getString(R.string.success_review_published))
            Navigation.toUserHomeMenu(context)
        } else
            Constants.showToast(context, context.getString(R.string.error_review_publish))
    }

}