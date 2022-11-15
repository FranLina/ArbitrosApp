package com.franlinares.app.Partido

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.franlinares.app.BaseFragment
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentCargaPlantillasBinding
import com.franlinares.app.databinding.FragmentDatosPartidosBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DatosPartidosFragment : BaseFragment() {

    private var _binding: FragmentDatosPartidosBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDatosPartidosBinding.inflate(inflater, container, false)

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val idpartido = prefs.getString("idPartido", "")

        db.collection("Partidos").document(idpartido.toString()).get()
            .addOnSuccessListener {
                binding.txtDPCompeticion.text = it.get("Liga").toString()
                binding.txtDPJornada.text = it.get("Jornada").toString()
                binding.txtDPCampoJuego.text = it.get("Polideportivo").toString()
                binding.txtDPFecha.text = it.get("Fecha").toString()
                binding.txtDPHora.text = it.get("Hora").toString()
                binding.txtDPEquipoLocal.text = it.get("EquipoLocal").toString()
                binding.txtDPEquipoVisitante.text = it.get("EquipoVisitante").toString()
            }
        db.collection("designaciones").document(idpartido.toString()).get()
            .addOnSuccessListener {
                if (it.get("Cronometrador").toString().isNotEmpty())
                    db.collection("Mesa_oficial").document(it.get("Cronometrador").toString()).get()
                        .addOnSuccessListener { it1 ->
                            binding.txtDPCronometrador.text =
                                it1.get("apellido1").toString() + " " + it1.get("apellido2")
                                    .toString() + ", " + it1.get("nombre").toString()
                        }
                db.collection("Mesa_oficial").document(it.get("ArbitroP").toString()).get()
                    .addOnSuccessListener { it1 ->
                        binding.txtDPArbitroP.text =
                            it1.get("apellido1").toString() + " " + it1.get("apellido2")
                                .toString() + ", " + it1.get("nombre").toString()
                    }
                if (it.get("ArbitroA").toString().isNotEmpty())
                    db.collection("Mesa_oficial").document(it.get("ArbitroA").toString()).get()
                        .addOnSuccessListener { it1 ->
                            binding.txtDPArbitroA.text =
                                it1.get("apellido1").toString() + " " + it1.get("apellido2")
                                    .toString() + ", " + it1.get("nombre").toString()
                        }
                db.collection("Mesa_oficial").document(it.get("Anotador").toString()).get()
                    .addOnSuccessListener { it1 ->
                        binding.txtDPAnotador.text =
                            it1.get("apellido1").toString() + " " + it1.get("apellido2")
                                .toString() + ", " + it1.get("nombre").toString()
                    }
            }

        binding.btnComenzarPartido.setOnClickListener {
            db.collection("Partidos").document(idpartido.toString()).update(
                hashMapOf(
                    "Estado" to "En Directo",
                    "Resultado" to "0:0"
                ) as Map<String?,Any?>
            )
            Navigation.findNavController(binding.root).navigate(
                R.id.action_datosPartidosFragment_to_partidoFragment
            )
        }
        return binding.root
    }

}