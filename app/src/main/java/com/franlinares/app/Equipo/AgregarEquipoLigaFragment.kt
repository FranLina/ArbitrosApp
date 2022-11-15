package com.franlinares.app.Equipo

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.franlinares.app.BaseFragment
import com.franlinares.app.Equipo.ListViewEquipo.AdaptadorEquipo
import com.franlinares.app.Equipo.ListViewEquipo.Equipos
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentAgregarEquipoLigaBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import kotlin.math.log

class AgregarEquipoLigaFragment : BaseFragment() {


    private var _binding: FragmentAgregarEquipoLigaBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        llenarSpinner()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAgregarEquipoLigaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAEAgregar.setOnClickListener {
            db.collection("Equipos")
                .document(binding.spinnerAEEquipo.selectedItem.toString()).update(
                    hashMapOf(
                        "Liga" to binding.spinnerAELiga.selectedItem.toString()
                    ) as Map<String, Any>
                ).addOnSuccessListener {

                    db.collection("Ligas").document(binding.spinnerAELiga.selectedItem.toString())
                        .get()
                        .addOnSuccessListener {
                            val listEquipo = it.get("Equipos") as ArrayList<String>
                            listEquipo.add(binding.spinnerAEEquipo.selectedItem.toString())
                            db.collection("Ligas")
                                .document(binding.spinnerAELiga.selectedItem.toString()).update(
                                    hashMapOf(
                                        "Equipos" to listEquipo
                                    ) as Map<String, Any>
                                ).addOnSuccessListener {
                                    Toast.makeText(
                                        binding.root.context,
                                        "Equipo añadido con exito",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    llenarSpinner()
                                }
                        }
                }
        }

    }

    fun llenarSpinner() {

        //SpinnerEquipo
        val listaEquipos = mutableListOf<String>()
        db.collection("Equipos").get().addOnSuccessListener {
            for (equipos in it) {
                if (equipos.get("Liga") == "") {
                    listaEquipos.add(equipos.id)
                }
            }
            val adaptadorEquipo =
                ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_item)
            adaptadorEquipo.addAll(listaEquipos)
            binding.spinnerAEEquipo.adapter = adaptadorEquipo
        }


        //SpinnerLiga
        val listaLigas = mutableListOf<String>()
        db.collection("Ligas").get().addOnSuccessListener {
            for (ligas in it) {
                listaLigas.add(ligas.id)
            }
            val adaptadorLiga =
                ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_item)
            adaptadorLiga.addAll(listaLigas)
            binding.spinnerAELiga.adapter = adaptadorLiga
        }


    }

}