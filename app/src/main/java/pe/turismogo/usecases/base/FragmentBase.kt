package pe.turismogo.usecases.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class FragmentBase : Fragment() {

    protected lateinit var fragContext : Context
    protected lateinit var fragActivity : Activity
    protected lateinit var dialog : androidx.appcompat.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragContext = requireContext()
        fragActivity = requireActivity()
    }

    abstract fun setDependencies()
    abstract fun setClickEvents()
    abstract fun setInputEvents()
}