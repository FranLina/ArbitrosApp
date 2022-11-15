package com.franlinares.app.user

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
import com.franlinares.app.databinding.FragmentDesignacionBinding
import com.franlinares.app.databinding.FragmentDesignarArbitrosPartidoBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DesignacionFragment : BaseFragment() {

    private var _binding: FragmentDesignacionBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDesignacionBinding.inflate(inflater, container, false)

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val usuario = prefs.getString("usuario", "")

        db.collection("Mesa_oficial").document(usuario.toString()).get()
            .addOnSuccessListener { documentSnapshot ->

                val listPartidos = documentSnapshot.get("partidosAsignados") as ArrayList<String>
                val listaJornadas = mutableListOf<Jornadas>()

                db.collection("Partidos").get().addOnSuccessListener {
                    for (jornadas in it) {
                        for (i in 0..listPartidos.count() - 1) {
                            if (jornadas.id == listPartidos[i]) {
                                if (jornadas.get("Estado") != "Finalizado"){
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
                            }
                        }
                    }

                    val adapter = AdaptadorJornadas(binding.root.context, listaJornadas)

                    binding.ListViewDesignacion.adapter = adapter

                    binding.ListViewDesignacion.setOnItemClickListener { adapterView, view, i, l ->

                        val prefs =
                            PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                        val editor = prefs.edit()
                        editor.putString("idPartido", listaJornadas[i].id)
                        editor.apply()
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_designacionFragment_to_cargaPlantillasFragment)

                    }
                }

            }

        return binding.root
    }
}