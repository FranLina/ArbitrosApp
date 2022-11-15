package com.franlinares.app.Estadisticas

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.Navigation
import com.franlinares.app.BaseFragment
import com.franlinares.app.Liga.ListViewLigas.AdaptadorLiga
import com.franlinares.app.Liga.ListViewLigas.Ligas
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentCargaPlantillasBinding
import com.franlinares.app.databinding.FragmentElegirLigaBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ElegirLigaFragment : BaseFragment() {

    private var _binding: FragmentElegirLigaBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private var listaLigas = mutableListOf<Ligas>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentElegirLigaBinding.inflate(inflater, container, false)

        listaLigas.clear()
        db.collection("Ligas").get().addOnSuccessListener {
            for (ligas in it) {
                if (ligas.get("Activa") as Boolean) {
                    val liga = Ligas(
                        ligas.get("Nombre").toString(),
                        ligas.id.toString(),
                        ligas.get("Categoria").toString(),
                        ligas.get("Sexo").toString(),
                        ligas.get("Temporada").toString(),
                        ligas.get("Activa") as Boolean
                    )
                    listaLigas.add(liga)
                }
            }
            val adapter = AdaptadorLiga(binding.root.context, listaLigas)
            binding.ListLigasEmpezadas.adapter = adapter

        }

        binding.ListLigasEmpezadas.setOnItemClickListener { adapterView, view, i, l ->
            if (listaLigas[i].activo == true) {
                val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                val editor = prefs.edit()
                editor.putString("idliga", listaLigas[i].id)
                editor.apply()
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_elegirLigaFragment2_to_cargarJornadasLigaFragment)
            }
        }

        /*llenarSpinner()

        binding.btnVerLiga.setOnClickListener {
            val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
            val editor = prefs.edit()
            editor.putString("idLiga", binding.spinnerLigas.selectedItem.toString())
            editor.apply()

            Navigation.findNavController(binding.root).navigate(R.id.action_elegirLigaFragment2_to_cargarJornadasLigaFragment)
        }*/


        return binding.root
    }

    /*fun llenarSpinner() {
        val listaLigas = mutableListOf<String>()
        db.collection("Ligas").get().addOnSuccessListener {
            for (ligas in it) {
                if (ligas.get("Activa") == true)
                    listaLigas.add(ligas.id)
            }
            val adaptadorLiga =
                ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_item)
            adaptadorLiga.addAll(listaLigas)
            binding.spinnerLigas.adapter = adaptadorLiga
        }
    }*/
}