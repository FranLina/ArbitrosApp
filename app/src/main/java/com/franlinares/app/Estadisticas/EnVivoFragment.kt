package com.franlinares.app.Estadisticas

import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.franlinares.app.BaseFragment
import com.franlinares.app.Estadisticas.ListViewEstadisticas.AdaptadorMinuto
import com.franlinares.app.Estadisticas.ListViewEstadisticas.MinutoAMinuto
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentEnVivoBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class EnVivoFragment : BaseFragment() {

    private var _binding: FragmentEnVivoBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private var i = 0
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEnVivoBinding.inflate(inflater, container, false)

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val idpartido = prefs.getString("idPartido", "")

        db.collection("Partidos").document(idpartido.toString()).get()
            .addOnSuccessListener { documentSnapshot ->
                binding.txtNombreELocal.text = "  " + documentSnapshot.get("EquipoLocal").toString()
                binding.txtNombreEVisitante.text =
                    "  " + documentSnapshot.get("EquipoVisitante").toString()
                binding.txtPuntosLocalPartido.text =
                    (documentSnapshot.get("Resultado").toString().split(":"))[0]
                binding.txtPuntosVisitantePartido.text =
                    (documentSnapshot.get("Resultado").toString().split(":"))[1]

                db.collection("Equipos")
                    .document(documentSnapshot.get("EquipoLocal").toString())
                    .get()
                    .addOnSuccessListener {
                        if (it.get("UrlFoto") != "") {
                            Picasso.get()
                                .load(it.get("UrlFoto").toString())
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .error(R.drawable.ic_launcher_foreground)
                                .into(binding.imageLocalPartido)
                        }
                    }

                db.collection("Equipos")
                    .document(documentSnapshot.get("EquipoVisitante").toString())
                    .get()
                    .addOnSuccessListener {
                        if (it.get("UrlFoto") != "") {
                            Picasso.get()
                                .load(it.get("UrlFoto").toString())
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .error(R.drawable.ic_launcher_foreground)
                                .into(binding.imageVisitantePartido)
                        }
                    }

            }

        db.collection("MinutoaMinuto").document(idpartido.toString()).get().addOnSuccessListener {
            val listaminuto = mutableListOf<MinutoAMinuto>()
            val lista = it.get("registro") as ArrayList<Map<String?, Any?>>
            for (i in (lista.count() - 1) downTo 0) {
                val minutoMap = lista[i]
                val minuto = MinutoAMinuto(
                    minutoMap["cuarto"].toString(),
                    minutoMap["dorsal"].toString(),
                    minutoMap["equipo"].toString(),
                    minutoMap["frase"].toString(),
                    minutoMap["resultado"].toString(),
                    minutoMap["tiempo"].toString(),
                    minutoMap["tipoFrase"].toString()
                )
                listaminuto.add(minuto)
            }
            val adapter = AdaptadorMinuto(binding.root.context, listaminuto)
            binding.LVMinutoAMinuto.adapter = adapter
        }

        i = binding.progressBar.progress

        Thread(Runnable {
            while (i < 100) {
                i += 1
                handler.post(Runnable {
                    binding.progressBar.progress = i
                })
                try {
                    Thread.sleep(125)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                if (i == 100) {
                    i = 0
                    db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                        .addOnSuccessListener {
                            val listaminuto = mutableListOf<MinutoAMinuto>()
                            val lista = it.get("registro") as ArrayList<Map<String?, Any?>>
                            for (i in (lista.count() - 1) downTo 0) {
                                val minutoMap = lista[i]
                                val minuto = MinutoAMinuto(
                                    minutoMap["cuarto"].toString(),
                                    minutoMap["dorsal"].toString(),
                                    minutoMap["equipo"].toString(),
                                    minutoMap["frase"].toString(),
                                    minutoMap["resultado"].toString(),
                                    minutoMap["tiempo"].toString(),
                                    minutoMap["tipoFrase"].toString()
                                )
                                listaminuto.add(minuto)
                            }
                            val adapter = AdaptadorMinuto(binding.root.context, listaminuto)
                            binding.LVMinutoAMinuto.adapter = adapter
                        }

                    db.collection("Partidos").document(idpartido.toString()).get()
                        .addOnSuccessListener { documentSnapshot ->
                            binding.txtPuntosLocalPartido.text =
                                (documentSnapshot.get("Resultado").toString().split(":"))[0]
                            binding.txtPuntosVisitantePartido.text =
                                (documentSnapshot.get("Resultado").toString().split(":"))[1]
                        }

                }
            }
        }).start()

        return binding.root
    }

}