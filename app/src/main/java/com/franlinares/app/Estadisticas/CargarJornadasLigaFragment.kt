package com.franlinares.app.Estadisticas

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginRight
import androidx.navigation.Navigation
import com.franlinares.app.BaseFragment
import com.franlinares.app.Liga.ListViewLigas.AdaptadorJornadas
import com.franlinares.app.Liga.ListViewLigas.Jornadas
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentCargarJornadasLigaBinding
import com.franlinares.app.databinding.FragmentElegirLigaBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class CargarJornadasLigaFragment : BaseFragment() {

    private var _binding: FragmentCargarJornadasLigaBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCargarJornadasLigaBinding.inflate(inflater, container, false)

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val idliga = prefs.getString("idliga", "")

        cargarJornada1()

        db.collection("Ligas").document(idliga.toString()).get().addOnSuccessListener {
            val numEquipos = (it.get("Equipos") as ArrayList<String>).count()
            val jornadas = (numEquipos * 2) - 2
            for (i in 1..jornadas) {
                val txt: TextView = TextView(binding.root.context)
                txt.text = "  Jornada " + i + "   "
                txt.setTextColor(Color.WHITE)
                txt.height = 140
                txt.gravity = Gravity.CENTER
                binding.ContenedorJornadas.addView(txt)

                txt.setOnClickListener {
                    val numJ = txt.text[txt.text.toString().trim().length + 1]
                    db.collection("Partidos").orderBy("Jornada").get().addOnSuccessListener {
                        val listaJornadas = mutableListOf<Jornadas>()
                        for (jornadas in it) {
                            if (jornadas.get("Liga")
                                    .toString() == idliga.toString() && jornadas.get("Jornada")
                                    .toString() == numJ.toString()
                            ) {
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

                        binding.ListViewJornadasLiga.adapter = adapter

                        binding.ListViewJornadasLiga.setOnItemClickListener { adapterView, view, i, l ->
                            if (listaJornadas[i].estado != "Sin Jugar") {
                                val prefs =
                                    PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                                val editor = prefs.edit()
                                editor.putString("idPartido", listaJornadas[i].id)
                                editor.apply()
                                Navigation.findNavController(binding.root)
                                    .navigate(R.id.action_cargarJornadasLigaFragment_to_cargaPartidoActivity)
                            }
                        }
                    }
                }


            }
        }


        return binding.root
    }

    fun cargarJornada1() {

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val idliga = prefs.getString("idliga", "")
        db.collection("Partidos").orderBy("Jornada").get().addOnSuccessListener {
            val listaJornadas = mutableListOf<Jornadas>()
            for (jornadas in it) {

                if (jornadas.get("Liga")
                        .toString() == idliga.toString() && jornadas.get("Jornada")
                        .toString() == "1"
                ) {
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

            binding.ListViewJornadasLiga.adapter = adapter

            binding.ListViewJornadasLiga.setOnItemClickListener { adapterView, view, i, l ->
                if (listaJornadas[i].estado != "Sin Jugar") {
                    val prefs =
                        PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                    val editor = prefs.edit()
                    editor.putString("idPartido", listaJornadas[i].id)
                    editor.apply()
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_cargarJornadasLigaFragment_to_cargaPartidoActivity)
                }
            }

        }
    }

}