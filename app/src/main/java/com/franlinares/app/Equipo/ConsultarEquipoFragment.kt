package com.franlinares.app.Equipo

import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.franlinares.app.BaseFragment
import com.franlinares.app.Equipo.ListViewEquipo.AdaptadorEquipo
import com.franlinares.app.Equipo.ListViewEquipo.Equipos
import com.franlinares.app.Liga.ListViewLigas.AdaptadorLiga
import com.franlinares.app.Liga.ListViewLigas.Ligas
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentConsultarEquipoBinding
import com.franlinares.app.databinding.FragmentCrearEquipoBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ConsultarEquipoFragment : BaseFragment() {

    private var _binding: FragmentConsultarEquipoBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private val listaEquipos = mutableListOf<Equipos>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConsultarEquipoBinding.inflate(inflater, container, false)

        llenarListView()
        binding.ListViewEquipos.setOnItemLongClickListener(AdapterView.OnItemLongClickListener { arg0, arg1, pos, id ->

            val builder = AlertDialog.Builder(binding.root.context)
            val view = layoutInflater.inflate(R.layout.borrardialog, null)
            builder.setView(view)
            view.findViewById<TextView>(R.id.txtIdBorrar)
                .setText(listaEquipos[pos].nombreEquipo)
            val dialog = builder.create()
            dialog.show()

            view.findViewById<Button>(R.id.btnSi).setOnClickListener {
                db.collection("Equipos")
                    .document(listaEquipos[pos].id).delete()
                    .addOnSuccessListener {
                        Toast.makeText(
                            binding.root.context,
                            "Borrado con exito",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener { exception ->
                        Log.w(
                            ContentValues.TAG,
                            "Error deletting documents.",
                            exception
                        )
                    }
                /*db.collection("Equipos").document(listaEquipos[pos].id).get().addOnSuccessListener {
                    db.collection("Ligas").document(it.get("Liga").toString()).get()
                        .addOnSuccessListener { it2 ->
                            val listEquipo = it2.get("Equipos") as ArrayList<String>
                            listEquipo.remove(listaEquipos[pos].id)
                            db.collection("Equipos").document(it.get("Liga").toString()).update(
                                hashMapOf(
                                    "Equipos" to listEquipo
                                ) as HashMap<String?, Any>
                            )
                        }
                }
                db.collection("Jugadores").get().addOnSuccessListener { it2 ->
                    for (jugador in it2) {
                        if (jugador.get("Equipos") == listaEquipos[pos].id) {
                            db.collection("Jugadores").document(jugador.id).update(
                                hashMapOf(
                                    "Equipo" to ""
                                ) as HashMap<String?, Any>
                            ).addOnSuccessListener {
                                llenarListView()
                                dialog.hide()
                            }
                        }
                    }
                }*/
            }

            view.findViewById<Button>(R.id.btnNo).setOnClickListener {
                dialog.hide()
            }

            true
        })

        return binding.root
    }

    fun llenarListView() {
        listaEquipos.clear()
        db.collection("Equipos").get().addOnSuccessListener {
            for (equipos in it) {
                val equipo = Equipos(
                    equipos.id,
                    equipos.get("Nombre").toString(),
                    equipos.get("Categoria").toString(),
                    equipos.get("Sexo").toString(),
                    equipos.get("Temporada").toString(),
                    equipos.get("Localidad").toString(),
                    equipos.get("UrlFoto").toString()
                )
                listaEquipos.add(equipo)
            }

            val adapter = AdaptadorEquipo(binding.root.context, listaEquipos)

            binding.ListViewEquipos.adapter = adapter
        }
    }

}