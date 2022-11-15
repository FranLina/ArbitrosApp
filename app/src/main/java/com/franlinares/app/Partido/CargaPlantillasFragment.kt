package com.franlinares.app.Partido

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.franlinares.app.BaseFragment
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentCargaPlantillasBinding
import com.franlinares.app.databinding.FragmentDesignacionBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CargaPlantillasFragment : BaseFragment() {

    private var _binding: FragmentCargaPlantillasBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCargaPlantillasBinding.inflate(inflater, container, false)

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val idpartido = prefs.getString("idPartido", "")

        db.collection("Partidos").document(idpartido.toString()).get()
            .addOnSuccessListener {
                val editor = prefs.edit()
                editor.putString("EquipoLocal", it.getString("EquipoLocal"))
                editor.putString("EquipoVisitante", it.getString("EquipoVisitante"))
                editor.apply()
            }

        binding.btnEquipo.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_cargaPlantillasFragment_to_plantillaLocalFragment)
        }

        binding.btnSiguiente.setOnClickListener {
            db.collection("Estadisticas").document(idpartido.toString()).get()
                .addOnSuccessListener {
                    val editor = prefs.edit()
                    editor.putString("idPartido", idpartido.toString())
                    editor.apply()

                    if (it.exists())
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_cargaPlantillasFragment_to_datosPartidosFragment)
                    else
                        Toast.makeText(
                            binding.root.context,
                            "Carga las plantillas para poder seguir",
                            Toast.LENGTH_SHORT
                        ).show()
                }

        }

        return binding.root
    }

}