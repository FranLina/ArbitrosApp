package com.franlinares.app.user

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.franlinares.app.BaseFragment
import com.franlinares.app.LoginActivity
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnCerrar.setOnClickListener {
            val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
            val editor = prefs.edit()
            editor.putString("usuario", "")
            editor.putString("contraseña", "")
            editor.apply()
            val intent = Intent(getActivity(), LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnDesignacion.setOnClickListener {
            Navigation.findNavController(root)
                .navigate(R.id.action_nav_home_to_designacionFragment)
        }

        binding.btnDatos.setOnClickListener {
            Navigation.findNavController(root)
                .navigate(R.id.action_nav_home_to_datosPersonalesFragment)
        }

        binding.btnConfiguracion.setOnClickListener {
            Navigation.findNavController(root)
                .navigate(R.id.action_nav_home_to_configuracionFragment)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}