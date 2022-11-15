package com.franlinares.app.Liga

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
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentCrearLigaBinding
import com.franlinares.app.databinding.FragmentDatosPersonalesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class CrearLigaFragment : BaseFragment() {

    private var _binding: FragmentCrearLigaBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearLigaBinding.inflate(inflater, container, false)
        llenarSpinner(2021, 100)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCLGuardar.setOnClickListener {

            val nombreLiga = generarNombre(
                binding.txtNombreLiga.text.toString(),
                binding.spinnerCategoria.selectedItem.toString(),
                binding.spinnerSexo.selectedItem.toString(),
                binding.spinnerTemporada.selectedItem.toString()
            )

            if (binding.txtNombreLiga.text.toString() != "") {
                db.collection("Ligas")
                    .document(nombreLiga)
                    .get().addOnSuccessListener {
                        if (it.get("Nombre") != null) {
                            Toast.makeText(
                                binding.root.context,
                                "Ya existe un registro con este nombre",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {

                            db.collection("Ligas")
                                .document(nombreLiga).set(
                                    hashMapOf(
                                        "Nombre" to binding.txtNombreLiga.text.toString(),
                                        "Activa" to false,
                                        "Categoria" to binding.spinnerCategoria.selectedItem.toString(),
                                        "Sexo" to binding.spinnerSexo.selectedItem.toString(),
                                        "Temporada" to binding.spinnerTemporada.selectedItem.toString(),
                                        "Equipos" to listOf<String>()
                                    ) as Map<String, Any>
                                ).addOnSuccessListener {
                                    Toast.makeText(
                                        binding.root.context,
                                        "Guardado los datos con exito",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }.addOnFailureListener { exception ->
                                    Log.w(ContentValues.TAG, "Error setting documents.", exception)
                                }
                        }


                    }.addOnFailureListener { exception ->
                        Log.w(ContentValues.TAG, "Error getting documents.", exception)
                    }
            } else {
                Toast.makeText(
                    binding.root.context,
                    "Rellene los campos",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    fun generarNombre(nombre: String, Categoria: String, Sexo: String, Temporada: String): String {

        var nombreLiga = ""

        val anio = Temporada.split("/")

        nombreLiga =
            nombre + Categoria[0].toString().uppercase() + Sexo[0].toString().uppercase() + anio[0]

        return nombreLiga
    }

    fun llenarSpinner(anioI: Int, cantidad: Int) {

        //AñoTemporada
        val lista = mutableListOf<String>()
        for (i in anioI..(anioI + cantidad)) {
            lista.add("" + i + "/" + (i + 1))
        }
        val adaptador =
            ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_item)
        adaptador.addAll(lista)
        binding.spinnerTemporada.adapter = adaptador

        //Categoria
        val lista2 =
            mutableListOf<String>(
                "Senior",
                "Junior",
                "Cadete",
                "Infantil"
            )
        val adaptador2 =
            ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_item)
        adaptador2.addAll(lista2)
        binding.spinnerCategoria.adapter = adaptador2

        //Sexo
        val lista3 =
            mutableListOf<String>("Masculino", "Femenino")
        val adaptador3 =
            ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_item)
        adaptador3.addAll(lista3)
        binding.spinnerSexo.adapter = adaptador3
    }

}