package pe.turismogo.usecases.user.events.details

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import pe.turismogo.R
import pe.turismogo.data.DatabaseManager
import pe.turismogo.factory.MessageFactory
import pe.turismogo.observable.rtdatabase.DatabaseObserver
import pe.turismogo.usecases.user.base.EventDetailsActivity
import pe.turismogo.util.Utils

class UserEventDetailsActivity : EventDetailsActivity(),
    DatabaseObserver.EventInsertObserver {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseManager.getInstance().addEventInsertObservable(this)
        setDependencies()
    }

    override fun onDestroy() {
        super.onDestroy()
        DatabaseManager.getInstance().removeEventInsertObservable(this)
    }

    override fun setDependencies() {
        super.setDependencies()
        binding.contentEventDetails.btnEventDetailAction.text = context.getString(R.string.action_reserve)
    }

    override fun actionEventDetail() {
        dialog = MessageFactory().getDialog(context, MessageFactory.TYPE_ACCEPT_CANCEL).create()
        val joinMessage = "${context.getString(R.string.join_action_begin)} \"${event.title} \" ${context.getString(R.string.join_action_end)} "

        dialog.setMessage(joinMessage)
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            dialog.dismiss()
            if(!DatabaseManager.getInstance().isEventCollision(event)) {
                dialog = MessageFactory().getDialog(context, MessageFactory.TYPE_LOAD).create()
                dialog.show()

                DatabaseManager.getInstance().joinEvent(event)

            } else {
                Utils.showToast(context, context.getString(R.string.error_event_collision))
            }
        }
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun notifyEventInsertObservers(isSuccessful: Boolean) {
        try { dialog.dismiss() } catch (ignored : Exception) { /*NTD*/ }
        if(isSuccessful) {
            Utils.showToast(context, context.getString(R.string.success_event_join))
            finish()
        } else {
            Utils.showToast(context, context.getString(R.string.error_event_join))
        }
    }
}