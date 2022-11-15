package com.franlinares.app.Jugador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.franlinares.app.BaseFragment
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentAgregarEquipoLigaBinding
import com.franlinares.app.databinding.FragmentAgregarJugadorEquipoBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AgregarJugadorEquipoFragment : BaseFragment() {

    private var _binding: FragmentAgregarJugadorEquipoBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAgregarJugadorEquipoBinding.inflate(inflater, container, false)
        llenarSpinner()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinnerAJJugador.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (binding.spinnerAJJugador.selectedItem.toString() != "")
                        llenarSpinnerEquipos()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}

            }

        binding.btnAJAgregar.setOnClickListener {
            if (binding.spinnerAJJugador.selectedItem.toString() != "")
                db.collection("Jugadores")
                    .document(binding.spinnerAJJugador.selectedItem.toString()).update(
                        hashMapOf(
                            "Equipo" to binding.spinnerAJEquipo.selectedItem.toString()
                        ) as Map<String, Any>
                    ).addOnSuccessListener {

                        db.collection("Equipos")
                            .document(binding.spinnerAJEquipo.selectedItem.toString())
                            .get()
                            .addOnSuccessListener {
                                val listJugador = it.get("Jugadores") as ArrayList<String>
                                listJugador.add(binding.spinnerAJJugador.selectedItem.toString())
                                db.collection("Equipos")
                                    .document(binding.spinnerAJEquipo.selectedItem.toString())
                                    .update(
                                        hashMapOf(
                                            "Jugadores" to listJugador
                                        ) as Map<String, Any>
                                    ).addOnSuccessListener {
                                        Toast.makeText(
                                            binding.root.context,
                                            "Jugador añadido con exito",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        llenarSpinner()
                                    }
                            }
                    }
        }

    }

    fun llenarSpinner() {

        //SpinnerJugador
        val listaJugadores = mutableListOf<String>()
        db.collection("Jugadores").get().addOnSuccessListener {
            for (jugadores in it) {
                if (jugadores.get("Equipo") == "") {
                    listaJugadores.add(jugadores.id)
                }
            }
            listaJugadores.add("")
            val adaptadorJugador =
                ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_item)
            adaptadorJugador.addAll(listaJugadores)
            binding.spinnerAJJugador.adapter = adaptadorJugador
            llenarSpinnerEquipos()
        }
    }

    fun llenarSpinnerEquipos() {

        //SpinnerEquipo

        val dni = binding.spinnerAJJugador.selectedItem.toString()
        if (dni != "") {
            db.collection("Jugadores").document(dni).get().addOnSuccessListener {
                val categoria = it.get("Categoria").toString()
                val sexo = it.get("Sexo").toString()

                val listaEquipo = mutableListOf<String>()
                db.collection("Equipos").get().addOnSuccessListener {
                    for (equipo in it) {
                        if (equipo.get("Categoria").toString() == categoria &&
                            equipo.get("Sexo").toString() == sexo
                        )
                            listaEquipo.add(equipo.id)
                    }
                    val adaptadorEquipo =
                        ArrayAdapter<String>(
                            binding.root.context,
                            android.R.layout.simple_spinner_item
                        )
                    adaptadorEquipo.addAll(listaEquipo)
                    binding.spinnerAJEquipo.adapter = adaptadorEquipo
                }
            }
        }
    }

}