package pe.turismogo.usecases.user.dashboard

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pe.turismogo.R
import pe.turismogo.databinding.FragmentUserDashboardBinding
import pe.turismogo.util.Navigation

class UserDashboardFragment : Fragment() {

    private lateinit var context : Context
    private lateinit var activity : Activity
    private lateinit var binding : FragmentUserDashboardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = requireContext()
        activity = requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDashboardBinding.inflate(inflater, container, false)
        setDependencies()
        return binding.root
    }


    private fun setDependencies() {
        binding.content.cardReservation.setOnClickListener { Navigation.toEventReview(context) }
        binding.content.option1.root.setOnClickListener { Navigation.toEventDetails(context) }
    }
}