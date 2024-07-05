package pe.turismogo.usecases.user.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import pe.turismogo.R
import pe.turismogo.data.CacheManager
import pe.turismogo.databinding.FragmentUserEventsBinding
import pe.turismogo.factory.MenuFactory
import pe.turismogo.interfaces.IOnEventClickListener
import pe.turismogo.model.domain.Event
import pe.turismogo.usecases.base.FragmentBase
import pe.turismogo.usecases.business.panel.PanelViewModel
import pe.turismogo.usecases.user.home.HomeActivity
import pe.turismogo.util.Navigation


class UserEventsFragment : FragmentBase(),
    IOnEventClickListener {

    private lateinit var binding : FragmentUserEventsBinding

    private val eventViewModel : EventViewModel get() = (activity as HomeActivity).eventViewModel
    private val eventUserAdapter = EventUserAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUserEventsBinding.inflate(inflater, container, false)
        setDependencies()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disableSearch()

        eventViewModel.getEventListLiveData().observe(viewLifecycleOwner, Observer {
            eventUserAdapter.setData(it)
            if(it.isNotEmpty()) {
                hideLoading()
            } else
                showLoading()
        })
    }

    override fun setDependencies() {

        try {dialog.dismiss() } catch (ignored : Exception) { /*NTD*/ }

        binding.contentUserEvents.eventHeader.title.text = getString(R.string.prompt_events_available)
        binding.contentUserEvents.eventHeader.subtitle.text = getString(R.string.desc_menu_panel)

        initRecyclerView()
        setClickEvents()
        setInputEvents()
    }

    override fun setClickEvents() {

        binding.contentUserEvents.options.setOnClickListener {
            val popupMenu = MenuFactory().getMenu(binding.contentUserEvents.options, MenuFactory.TYPE_EVENT_FILTER)

            popupMenu.setOnMenuItemClickListener { item ->

                when (item?.itemId) {
                    R.id.event_filter_departure -> {
                        binding.contentUserEvents.tilSearchDateStart.isEnabled = true
                        binding.contentUserEvents.tilSearchEvent.isEnabled = false
                        binding.contentUserEvents.etSearchEvent.text?.clear()
                        binding.contentUserEvents.tilSearchDateStart.hint = fragContext.getString(R.string.prompt_date_departure)
                        eventViewModel.setFilterType(EventViewModel.TYPE_OPTION_DEPARTURE)
                    }
                    R.id.event_filter_return -> {
                        binding.contentUserEvents.tilSearchDateStart.isEnabled = true
                        binding.contentUserEvents.tilSearchEvent.isEnabled = false
                        binding.contentUserEvents.etSearchEvent.text?.clear()
                        binding.contentUserEvents.tilSearchDateStart.hint = fragContext.getString(R.string.prompt_date_return)
                        eventViewModel.setFilterType(EventViewModel.TYPE_OPTION_RETURN)
                    }
                    R.id.event_filter_description -> {
                        binding.contentUserEvents.tilSearchDateStart.isEnabled = false
                        binding.contentUserEvents.tilSearchEvent.isEnabled = true
                        binding.contentUserEvents.tilSearchDateStart.hint = fragContext.getString(R.string.prompt_date_departure)
                        eventViewModel.setFilterType(EventViewModel.TYPE_OPTION_DESCRIPTION)
                    }R.id.panel_filter_none -> {
                        disableSearch()
                    }
                    else ->  {
                        disableSearch()
                        eventViewModel.setFilterType(EventViewModel.TYPE_OPTION_ALL)
                    }
                }
                true
            }

            popupMenu.show()
        }
    }

    override fun setInputEvents() {
        binding.contentUserEvents.etSearchEvent.addTextChangedListener {
            val search = it.toString()
            eventViewModel.filterEventsLiveData(search)
        }

        binding.contentUserEvents.etSearchDateStart.addTextChangedListener {
            val search = it.toString()
            eventViewModel.filterEventsLiveData(search)
        }

    }

    override fun onClickDetails(event: Event) {
        CacheManager.getInstance().saveEventCache(fragActivity, event)
        Navigation.toEventDetails(fragContext)
    }

    private fun initRecyclerView() {
        binding.contentUserEvents.rvEvents.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false )
        binding.contentUserEvents.rvEvents.adapter = eventUserAdapter
    }


    private fun disableSearch() {
        binding.contentUserEvents.etSearchEvent.text?.clear()
        binding.contentUserEvents.etSearchEvent.clearFocus()
        binding.contentUserEvents.tilSearchEvent.isEnabled = false

        binding.contentUserEvents.tilSearchDateStart.isEnabled = false
    }

    private fun showLoading() {
        binding.contentUserEvents.pbLaunchEvents.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.contentUserEvents.pbLaunchEvents.visibility = View.GONE
    }
}