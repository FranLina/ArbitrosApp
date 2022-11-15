package com.franlinares.app.Liga

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.Navigation
import com.franlinares.app.BaseFragment
import com.franlinares.app.Equipo.ListViewEquipo.AdaptadorEquipo
import com.franlinares.app.Equipo.ListViewEquipo.Equipos
import com.franlinares.app.Liga.ListViewLigas.AdaptadorJornadas
import com.franlinares.app.Liga.ListViewLigas.Jornadas
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentConsultarLigaBinding
import com.franlinares.app.databinding.FragmentGestionarJornadaLigaBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GestionarJornadaLigaFragment : BaseFragment() {

    private var _binding: FragmentGestionarJornadaLigaBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGestionarJornadaLigaBinding.inflate(inflater, container, false)

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val liga = prefs.getString("liga", "")

        db.collection("Partidos").orderBy("Jornada").get().addOnSuccessListener {
            val listaJornadas = mutableListOf<Jornadas>()
            for (jornadas in it) {
                if (jornadas.get("Liga") == liga) {

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

            val adapter = AdaptadorJornadas(binding.root.context, listaJornadas)

            binding.ListViewJornadas.adapter = adapter


            binding.ListViewJornadas.setOnItemClickListener { adapterView, view, i, l ->

                val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                val editor = prefs.edit()
                editor.putString("idPartido", listaJornadas[i].id)
                editor.apply()
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_gestionarJornadaLigaFragment_to_editarPartidoJornadaFragment)

            }
        }


        return binding.root
    }

}