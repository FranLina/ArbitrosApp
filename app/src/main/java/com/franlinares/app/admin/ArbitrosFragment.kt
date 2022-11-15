package com.franlinares.app.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.franlinares.app.BaseFragment
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentArbitrosBinding
import com.franlinares.app.databinding.FragmentDesignarArbitrosPartidoBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ArbitrosFragment : BaseFragment() {

    private var _binding: FragmentArbitrosBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArbitrosBinding.inflate(inflater, container, false)
        binding.btnCArbitro.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_arbitrosFragment_to_crearArbitroFragment)
        }
        return binding.root
    }

}