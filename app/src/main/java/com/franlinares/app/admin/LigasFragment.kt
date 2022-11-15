package com.franlinares.app.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.franlinares.app.BaseFragment
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentLigasBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LigasFragment : BaseFragment() {

    private var _binding: FragmentLigasBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLigasBinding.inflate(inflater, container, false)

        binding.btnCrearLiga.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_ligasFragment_to_clearLigaFragment)
        }

        binding.btnConsultarLiga.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_ligasFragment_to_consultarLigaFragment)
        }

        binding.btnIniciarLiga.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_ligasFragment_to_iniciarLigaFragment)
        }

        return binding.root
    }


}