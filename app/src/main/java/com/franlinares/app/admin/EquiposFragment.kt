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
import com.franlinares.app.databinding.FragmentLigasBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EquiposFragment : BaseFragment() {

    private var _binding: FragmentEquiposBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEquiposBinding.inflate(inflater, container, false)

        binding.btnECrear.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_equiposFragment_to_crearEquipoFragment)
        }

        binding.btnEConsultar.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_equiposFragment_to_consultarEquipoFragment)
        }

        binding.btnEAgregarLiga.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_equiposFragment_to_agregarEquipoLigaFragment)
        }

        return binding.root
    }

}