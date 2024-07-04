package pe.turismogo.usecases.user.dashboard

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import pe.turismogo.R
import pe.turismogo.data.CacheManager
import pe.turismogo.databinding.FragmentUserDashboardBinding
import pe.turismogo.interfaces.IOnEventClickListener
import pe.turismogo.model.domain.Event
import pe.turismogo.model.session.UserSingleton
import pe.turismogo.usecases.base.FragmentBase
import pe.turismogo.usecases.user.home.HomeActivity
import pe.turismogo.util.Navigation

class UserDashboardFragment : FragmentBase(),
    IOnEventClickListener {

    private lateinit var binding : FragmentUserDashboardBinding

    private val reservationViewModel : ReservationUserViewModel get() = (activity as HomeActivity).reservationViewModel
    private val reservationAdapter = ReservationUserAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserDashboardBinding.inflate(inflater, container, false)
        setDependencies()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reservationViewModel.getEventListLiveData().observe(viewLifecycleOwner, Observer {
            reservationAdapter.setData(it)
        })
    }

    override fun setDependencies() {
        val welcome = "${fragContext.getString(R.string.prompt_welcome_user)} ${UserSingleton.getInstance().getUser()?.name}!"

        binding.contentDashboard.dashboardHeader.title.text = welcome
        binding.contentDashboard.dashboardHeader.subtitle.text = fragContext.getString(R.string.desc_menu_dashboard)

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
        Navigation.toReservationDetails(fragContext)
    }

    private fun initRecyclerView() {
        binding.contentDashboard.rvReservations.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false )
        binding.contentDashboard.rvReservations.adapter = reservationAdapter
    }
}