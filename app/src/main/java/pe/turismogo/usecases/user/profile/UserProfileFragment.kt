package pe.turismogo.usecases.user.profile

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pe.turismogo.R
import pe.turismogo.databinding.FragmentUserProfileBinding


class UserProfileFragment : Fragment() {

    private lateinit var context : Context
    private lateinit var activity : Activity
    private lateinit var binding : FragmentUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = requireContext()
        activity = requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        setDependencies()
        return binding.root
    }

    private fun setDependencies() {

    }
}
