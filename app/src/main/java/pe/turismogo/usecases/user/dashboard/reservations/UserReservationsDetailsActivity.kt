package pe.turismogo.usecases.user.dashboard.reservations

import android.os.Bundle
import pe.turismogo.R
import pe.turismogo.usecases.user.base.EventDetailsActivity
import pe.turismogo.util.Navigation

class UserReservationsDetailsActivity : EventDetailsActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDependencies()
    }

    override fun setDependencies() {
        super.setDependencies()

        binding.header.title.text = context.getString(R.string.prompt_reservations)
        binding.header.subtitle.text = context.getString(R.string.desc_menu_reservations_details)
        binding.contentEventDetails.btnEventDetailAction.text = context.getString(R.string.action_leave_review)
    }


    override fun actionEventDetail() {
        Navigation.toEventReview(context)
    }

    override fun notifyEventInsertObservers(isSuccessful: Boolean) {

    }
}