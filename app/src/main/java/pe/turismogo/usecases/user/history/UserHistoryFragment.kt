package pe.turismogo.usecases.user.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import pe.turismogo.R
import pe.turismogo.databinding.FragmentUserHistoryBinding
import pe.turismogo.usecases.base.FragmentBase
import pe.turismogo.usecases.user.home.HomeActivity


class UserHistoryFragment : FragmentBase() {

    private lateinit var binding : FragmentUserHistoryBinding
    private val historyViewModel : HistoryUserViewModel get() = (activity as HomeActivity).historyViewModel
    private val historyAdapter = HistoryUserAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUserHistoryBinding.inflate(inflater, container, false)
        setDependencies()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyViewModel.getEventListLiveData().observe(viewLifecycleOwner, Observer {
            historyAdapter.setData(it)
        })
    }

    override fun setDependencies() {
        try {dialog.dismiss() } catch (ignored : Exception) { /*NTD*/ }

        binding.contentHistory.historyHeader.title.text = getString(R.string.prompt_history)
        binding.contentHistory.historyHeader.subtitle.text = getString(R.string.desc_menu_history_events)

        initRecyclerView()
    }

    override fun setClickEvents() {
        TODO("Not yet implemented")
    }

    override fun setInputEvents() {
        TODO("Not yet implemented")
    }

    private fun initRecyclerView() {
        binding.contentHistory.rvHistory.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false )
        binding.contentHistory.rvHistory.adapter = historyAdapter
    }
}
