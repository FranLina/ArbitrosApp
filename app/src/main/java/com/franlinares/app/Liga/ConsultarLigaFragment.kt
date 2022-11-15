package com.franlinares.app.Liga

import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.franlinares.app.BaseFragment
import com.franlinares.app.Liga.ListViewLigas.AdaptadorLiga
import com.franlinares.app.Liga.ListViewLigas.Ligas
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentConsultarLigaBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ConsultarLigaFragment : BaseFragment() {

    private var _binding: FragmentConsultarLigaBinding? = null
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
        _binding = FragmentConsultarLigaBinding.inflate(inflater, container, false)
        llenarListView()

        binding.ListaLigas.setOnItemClickListener { adapterView, view, i, l ->
            if (listaLigas[i].activo == true) {
                val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                val editor = prefs.edit()
                editor.putString("liga", listaLigas[i].id)
                editor.apply()
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_consultarLigaFragment_to_gestionarJornadaLigaFragment)
            }
        }

        binding.ListaLigas.setOnItemLongClickListener(OnItemLongClickListener { arg0, arg1, pos, id ->

            val builder = AlertDialog.Builder(binding.root.context)
            val view = layoutInflater.inflate(R.layout.borrardialog, null)
            builder.setView(view)
            view.findViewById<TextView>(R.id.txtIdBorrar).setText(listaLigas[pos].id)
            val dialog = builder.create()
            dialog.show()

            view.findViewById<Button>(R.id.btnSi).setOnClickListener {
                db.collection("Ligas")
                    .document(listaLigas[pos].id).delete()
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
                db.collection("Equipos").get().addOnSuccessListener { it2 ->
                    for (equipo in it2) {
                        if (equipo.get("Liga") == listaLigas[pos].id) {
                            db.collection("Equipos").document(equipo.id).update(
                                hashMapOf(
                                    "Liga" to ""
                                ) as HashMap<String?, Any>
                            ).addOnSuccessListener {
                                llenarListView()
                                dialog.hide()
                            }
                        }
                    }
                }
            }

            view.findViewById<Button>(R.id.btnNo).setOnClickListener {
                dialog.hide()
            }

            true
        })

        return binding.root
    }

    fun llenarListView() {
        listaLigas.clear()
        db.collection("Ligas").get().addOnSuccessListener {
            for (ligas in it) {
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

            val adapter = AdaptadorLiga(binding.root.context, listaLigas)

            binding.ListaLigas.adapter = adapter
        }
    }

}