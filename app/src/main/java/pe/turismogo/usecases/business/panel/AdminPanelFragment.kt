package pe.turismogo.usecases.business.panel

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import pe.turismogo.R
import pe.turismogo.data.CacheManager
import pe.turismogo.data.DatabaseManager
import pe.turismogo.databinding.FragmentAdminPanelBinding
import pe.turismogo.factory.MenuFactory
import pe.turismogo.factory.MessageFactory
import pe.turismogo.interfaces.IOnEventPanelClickListener
import pe.turismogo.interfaces.ISetup
import pe.turismogo.model.domain.Event
import pe.turismogo.observable.rtdatabase.DatabaseManagerObserver
import pe.turismogo.usecases.business.home.HomeAdminActivity
import pe.turismogo.util.Constants
import pe.turismogo.util.Navigation


class AdminPanelFragment : Fragment(),
    ISetup,
    IOnEventPanelClickListener,
    DatabaseManagerObserver.EventDeleteObserver {

    private lateinit var context : Context
    private lateinit var activity : Activity
    private lateinit var binding : FragmentAdminPanelBinding
    private lateinit var dialog : androidx.appcompat.app.AlertDialog

    private val panelViewModel : PanelViewModel get() = (activity as HomeAdminActivity).panelViewModel
    private val eventBusinessAdapter = EventBusinessAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = requireContext()
        activity = requireActivity()
        DatabaseManager.getInstance().addEventDeleteObservable(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        DatabaseManager.getInstance().removeEventDeleteObservable(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAdminPanelBinding.inflate(inflater, container, false)
        setDependencies()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disableSearch()

        panelViewModel.getEventListLiveData().observe(viewLifecycleOwner, Observer {
            eventBusinessAdapter.setData(it)
        })
    }

    override fun setDependencies() {
        binding.contentPanel.headerPanel.title.text = getString(R.string.prompt_welcome)
        binding.contentPanel.headerPanel.subtitle.text = getString(R.string.desc_menu_panel)

        initRecyclerView()

        setClickEvents()
        setInputEvents()
    }

    override fun setClickEvents() {
        binding.btnAddEvent.setOnClickListener { Navigation.toEventAdd(context) }

        binding.contentPanel.options.setOnClickListener {
            val popupMenu = MenuFactory().getMenu(binding.contentPanel.options, MenuFactory.TYPE_PANEL_FILTER)

            popupMenu.setOnMenuItemClickListener { item ->

                when (item?.itemId) {
                    R.id.panel_filter_title -> {
                        binding.contentPanel.tilSearchPanel.isEnabled = true
                        panelViewModel.setFilterType(PanelViewModel.TYPE_OPTION_NAME)
                    }
                    R.id.panel_filter_date -> {
                        binding.contentPanel.tilSearchPanel.isEnabled = true
                        panelViewModel.setFilterType(PanelViewModel.TYPE_OPTION_DATE)
                    }
                    R.id.panel_filter_status -> {
                        binding.contentPanel.tilSearchPanel.isEnabled = true
                        panelViewModel.setFilterType(PanelViewModel.TYPE_OPTION_STATUS)
                    }
                    R.id.panel_filter_none -> {
                        disableSearch()
                    }
                    else ->  {
                        disableSearch()
                        panelViewModel.setFilterType(PanelViewModel.TYPE_OPTION_ALL)
                    }
                }
                true
            }

            popupMenu.show()
        }

    }

    override fun setInputEvents() {
        super.setInputEvents()

        binding.contentPanel.etSearchPanel.addTextChangedListener {
            val search = it.toString()
            panelViewModel.filterEventsLiveData(search)
        }

    }

    override fun onEditClick(event: Event) {
        CacheManager.getInstance().saveEventCache(activity, event)
        Navigation.toEventEdit(context, event)
    }

    override fun onDeleteClick(event: Event) {
        dialog = MessageFactory().getDialog(context, MessageFactory.TYPE_ACCEPT_CANCEL).create()
        val deleteMessage = "${context.getString(R.string.delete_action_begin)} \"${event.title}\" ${context.getString(R.string.delete_action_end)} "

        dialog.setMessage(deleteMessage)
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            dialog.dismiss()
            dialog = MessageFactory().getDialog(context, MessageFactory.TYPE_LOAD).create()
            dialog.show()

//            DatabaseManager.getInstance().deleteEvent(event)
            DatabaseManager.getInstance().softDeleteEvent(event)
        }
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun initRecyclerView() {
        binding.contentPanel.rvEvents.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false )
        binding.contentPanel.rvEvents.adapter = eventBusinessAdapter
    }

    private fun disableSearch() {
        binding.contentPanel.etSearchPanel.text?.clear()
        binding.contentPanel.etSearchPanel.clearFocus()
        binding.contentPanel.tilSearchPanel.isEnabled = false
        panelViewModel.setFilterType(PanelViewModel.TYPE_OPTION_ALL)
    }

    override fun notifyEventDeleteObservers(isSuccessful: Boolean) {
        dialog.dismiss()
        if(isSuccessful) {
            Constants.showToast(context, context.getString(R.string.success_event_delete))
        } else {
            Constants.showToast(context, context.getString(R.string.error_event_delete))
        }
    }
}