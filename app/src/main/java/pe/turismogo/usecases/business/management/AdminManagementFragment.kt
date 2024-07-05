package pe.turismogo.usecases.business.management

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import pe.turismogo.R
import pe.turismogo.databinding.FragmentAdminManagementBinding
import pe.turismogo.usecases.base.FragmentBase
import pe.turismogo.usecases.business.home.HomeAdminActivity


class AdminManagementFragment : FragmentBase() {

    private lateinit var binding : FragmentAdminManagementBinding

    private val managementAdapter = ManagementAdapter()

    private val managementViewModel : ManagementViewModel get() = (activity as HomeAdminActivity).managementViewModel

    private val eventListNames = arrayListOf<String>()
    private lateinit var roleAdapter: ArrayAdapter<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAdminManagementBinding.inflate(inflater, container, false)
        setDependencies()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        managementViewModel.getEventListLiveData().observe(viewLifecycleOwner, Observer {
            binding.actSelectEvent.setAdapter(null)
            eventListNames.clear()
            it.forEach{ event ->  eventListNames.add(event.title) }
            binding.actSelectEvent.setAdapter(roleAdapter)
        })

        managementViewModel.getUserListLiveData().observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                binding.tvNoData.visibility = View.VISIBLE
            } else {
                binding.tvNoData.visibility = View.GONE
            }
            managementAdapter.setData(it)
        })
    }

    override fun setDependencies() {

        binding.actSelectEvent.setText(fragContext.getString(R.string.prompt_none))

        roleAdapter = ArrayAdapter<String>(
            fragContext,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            eventListNames
        )

        binding.actSelectEvent.isEnabled = false

        initRecyclerView()

        setClickEvents()
    }

    override fun setClickEvents() {

        binding.actSelectEvent.setOnItemClickListener { _, _, position, _ ->
            managementViewModel.loadUserFromEvent(position)
        }

    }

    override fun setInputEvents() {

    }




    private fun initRecyclerView() {
        binding.rvData.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false )
        binding.rvData.adapter = managementAdapter
    }
}
