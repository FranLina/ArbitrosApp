package com.franlinares.app.Partido

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.Navigation
import com.franlinares.app.BaseFragment
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentPlantillaLocalBinding
import com.franlinares.app.databinding.FragmentPlantillaVisitanteBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PlantillaVisitanteFragment : BaseFragment() {

    private var _binding: FragmentPlantillaVisitanteBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlantillaVisitanteBinding.inflate(inflater, container, false)

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val EquipoVisitante = prefs.getString("EquipoVisitante", "")
        val idpartido = prefs.getString("idPartido", "")

        val contenedor: LinearLayout = binding.Contenedor2
        val listCheckBox: ArrayList<CheckBox> = java.util.ArrayList<CheckBox>()

        db.collection("Equipos").document(EquipoVisitante.toString()).get()
            .addOnSuccessListener { documentSnapshot ->
                val listJugadores = documentSnapshot.get("Jugadores") as ArrayList<String>
                for (i in 0..listJugadores.count() - 1) {
                    db.collection("Jugadores").document(listJugadores[i].toString()).get()
                        .addOnSuccessListener {
                            val checkbox: CheckBox = CheckBox(binding.root.context)
                            checkbox.text = it.id + ", " + it.get("Apellido2").toString() + " " +
                                    it.get("Apellido1") + " " + it.get("Nombre") + ", Nº " + it.get(
                                "Dorsal"
                            )
                            checkbox.id = i
                            contenedor.addView(checkbox)
                            listCheckBox.add(checkbox)
                        }
                }
            }

        binding.btnGuardarPlantillaVisitante.setOnClickListener {
            var listJugadores = ArrayList<String>()

            db.collection("Estadisticas").document(idpartido.toString()).get()
                .addOnSuccessListener {
                    listJugadores = it.get("ListadoJugadores") as ArrayList<String>

                    for (i in 0..listCheckBox.count() - 1) {
                        val checkbox: CheckBox = listCheckBox[i]
                        if (checkbox.isChecked) {
                            val idJugador = checkbox.text.split(",")
                            val id = idJugador[0]
                            listJugadores.add(id)
                            db.collection("Jugadores").document(id).get()
                                .addOnSuccessListener { documentSnapshot ->
                                    val jugador = hashMapOf(
                                        "dorsal" to documentSnapshot.get("Dorsal"),
                                        "nombre" to documentSnapshot.get("Apellido2")
                                            .toString() + " " + documentSnapshot.get("Apellido1") + ", " + documentSnapshot.get(
                                            "Nombre"
                                        ),
                                        "equipo" to "Visitante",
                                        "minutos" to 0,
                                        "asi" to 0,
                                        "fal" to 0,
                                        "per" to 0,
                                        "puntos" to 0,
                                        "rebD" to 0,
                                        "rebO" to 0,
                                        "recu" to 0,
                                        "taCom" to 0,
                                        "taRec" to 0,
                                        "tc2pA" to 0,
                                        "tc2pF" to 0,
                                        "tc3pA" to 0,
                                        "tc3pF" to 0,
                                        "tlA" to 0,
                                        "tlF" to 0,
                                        "val" to 0
                                    ) as Map<String, Any>

                                    db.collection("Estadisticas").document(idpartido.toString())
                                        .update(
                                            hashMapOf(
                                                id to jugador,
                                            ) as Map<String, Any>
                                        )

                                }
                        }
                    }

                    db.collection("Estadisticas").document(idpartido.toString())
                        .update(
                            hashMapOf(
                                "ListadoJugadores" to listJugadores,
                            ) as Map<String, Any>
                        )
                }
            Toast.makeText(
                binding.root.context,
                "Cargado el equipo visitante con exito",
                Toast.LENGTH_SHORT
            ).show()
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_plantillaVisitanteFragment_to_cargaPlantillasFragment)

        }

        return binding.root
    }
}