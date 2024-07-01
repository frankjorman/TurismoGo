package pe.turismogo.usecases.admin.panel

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pe.turismogo.R
import pe.turismogo.databinding.FragmentAdminPanelBinding
import pe.turismogo.util.Navigation


class AdminPanelFragment : Fragment() {

    private lateinit var context : Context
    private lateinit var activity : Activity
    private lateinit var binding : FragmentAdminPanelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = requireContext()
        activity = requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminPanelBinding.inflate(inflater, container, false)
        setDependencies()
        return binding.root
    }

    private fun setDependencies() {
        binding.btnAddEvent.setOnClickListener { Navigation.toEventAdd(context) }
    }
}