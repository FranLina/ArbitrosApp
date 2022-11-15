package com.franlinares.app.admin

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.franlinares.app.BaseFragment
import com.franlinares.app.LoginActivity
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentHomeAdminBinding

class HomeFragmentAdmin : BaseFragment() {

    private var _binding: FragmentHomeAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeAdminBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnACerrar.setOnClickListener {
            val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
            val editor = prefs.edit()
            editor.putString("usuario", "")
            editor.putString("contraseña", "")
            editor.apply()
            val intent = Intent(getActivity(), LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnAArbitro.setOnClickListener {
            Navigation.findNavController(root)
                .navigate(R.id.action_homeFragmentAdmin_to_arbitrosFragment)
        }

        binding.btnAJugadores.setOnClickListener {
            Navigation.findNavController(root)
                .navigate(R.id.action_homeFragmentAdmin_to_jugadoresFragment)
        }

        binding.btnAEquipos.setOnClickListener {
            Navigation.findNavController(root)
                .navigate(R.id.action_homeFragmentAdmin_to_equiposFragment)
        }

        binding.btnALigas.setOnClickListener {
            Navigation.findNavController(root)
                .navigate(R.id.action_homeFragmentAdmin_to_ligasFragment)
        }

        binding.btnADesignaciones.setOnClickListener {
            Navigation.findNavController(root)
                .navigate(R.id.action_homeFragmentAdmin_to_designacionPartidoFragment)
        }

        binding.btnADatos.setOnClickListener {
            Navigation.findNavController(root)
                .navigate(R.id.action_homeFragmentAdmin_to_datosPersonalesFragment2)
        }

        binding.btnAConfiguracion.setOnClickListener {
            Navigation.findNavController(root)
                .navigate(R.id.action_homeFragmentAdmin_to_configuracionFragment2)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}