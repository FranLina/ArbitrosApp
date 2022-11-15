package com.franlinares.app.Liga

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.franlinares.app.BaseFragment
import com.franlinares.app.Liga.ListViewLigas.AdaptadorJornadas
import com.franlinares.app.Liga.ListViewLigas.Jornadas
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentDesignacionPartidoBinding
import com.franlinares.app.databinding.FragmentGestionarJornadaLigaBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DesignacionPartidoFragment : BaseFragment() {

    private var _binding: FragmentDesignacionPartidoBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDesignacionPartidoBinding.inflate(inflater, container, false)

        db.collection("Partidos").orderBy("Jornada").get().addOnSuccessListener {
            val listaJornadas = mutableListOf<Jornadas>()
            for (jornadas in it) {
                val jornada = Jornadas(
                    jornadas.id,
                    jornadas.get("EquipoLocal").toString(),
                    jornadas.get("EquipoVisitante").toString(),
                    jornadas.get("Polideportivo").toString(),
                    jornadas.get("Jornada").toString(),
                    jornadas.get("Resultado").toString(),
                    jornadas.get("Hora").toString(),
                    jornadas.get("Fecha").toString(),
                    jornadas.get("Estado").toString()
                )
                listaJornadas.add(jornada)
            }

            val adapter = AdaptadorJornadas(binding.root.context, listaJornadas)

            binding.ListViewDesignacionPartido.adapter = adapter


            binding.ListViewDesignacionPartido.setOnItemClickListener { adapterView, view, i, l ->

                val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                val editor = prefs.edit()
                editor.putString("idPartido", listaJornadas[i].id)
                editor.apply()
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_designacionPartidoFragment_to_designarArbitrosPartidoFragment)

            }
        }

        return binding.root
    }

}