package com.franlinares.app.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.franlinares.app.BaseFragment
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentEquiposBinding
import com.franlinares.app.databinding.FragmentJugadoresBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class JugadoresFragment : BaseFragment() {

    private var _binding: FragmentJugadoresBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJugadoresBinding.inflate(inflater, container, false)

        binding.btnJCrearJugador.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_jugadoresFragment_to_crearJugadorFragment)
        }

        binding.btnJConsultarJugador.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_jugadoresFragment_to_consultarJugadorFragment)
        }

        binding.btnJAgregarEquipo.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_jugadoresFragment_to_agregarJugadorEquipoFragment)
        }
        return binding.root
    }

}