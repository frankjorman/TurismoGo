package pe.turismogo.usecases.user.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import pe.turismogo.R
import pe.turismogo.data.CacheManager
import pe.turismogo.databinding.FragmentUserEventsBinding
import pe.turismogo.interfaces.IOnEventClickListener
import pe.turismogo.model.domain.Event
import pe.turismogo.usecases.base.FragmentBase
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
        binding.contentUserEvents.eventHeader.title.text = getString(R.string.prompt_welcome)
        binding.contentUserEvents.eventHeader.subtitle.text = getString(R.string.desc_menu_panel)


        initRecyclerView()
    }

    override fun setClickEvents() {
        TODO("Not yet implemented")
    }

    override fun setInputEvents() {
        TODO("Not yet implemented")
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
        binding.contentUserEvents.etSearchPanel.text?.clear()
        binding.contentUserEvents.etSearchPanel.clearFocus()
        binding.contentUserEvents.tilSearchPanel.isEnabled = false

        binding.contentUserEvents.tilSearchDateStart.isEnabled = false
        binding.contentUserEvents.tilSearchDateEnd.isEnabled = false
    }

    private fun showLoading() {
        binding.contentUserEvents.pbLaunchEvents.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.contentUserEvents.pbLaunchEvents.visibility = View.GONE
    }
}