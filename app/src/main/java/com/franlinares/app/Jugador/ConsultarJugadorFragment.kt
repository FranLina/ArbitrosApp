package com.franlinares.app.Jugador

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
import com.franlinares.app.Jugador.ListViewJugador.AdaptadorJugador
import com.franlinares.app.Jugador.ListViewJugador.Jugadores
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentConsultarEquipoBinding
import com.franlinares.app.databinding.FragmentConsultarJugadorBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ConsultarJugadorFragment : BaseFragment() {

    private var _binding: FragmentConsultarJugadorBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private val listaJugadores = mutableListOf<Jugadores>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConsultarJugadorBinding.inflate(inflater, container, false)

        llenarListView()

        binding.ListViewJugador.setOnItemLongClickListener(AdapterView.OnItemLongClickListener { arg0, arg1, pos, id ->

            val builder = AlertDialog.Builder(binding.root.context)
            val view = layoutInflater.inflate(R.layout.borrardialog, null)
            builder.setView(view)
            view.findViewById<TextView>(R.id.txtIdBorrar)
                .setText(listaJugadores[pos].nombre + " " + listaJugadores[pos].apellido1 + " " + listaJugadores[pos].apellido2)
            val dialog = builder.create()
            dialog.show()

            view.findViewById<Button>(R.id.btnSi).setOnClickListener {
                db.collection("Jugadores")
                    .document(listaJugadores[pos].dni).delete()
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
                db.collection("Equipos").document(listaJugadores[pos].equipo).get()
                    .addOnSuccessListener {
                        val listJugador = it.get("Jugadores") as ArrayList<String>
                        listJugador.remove(listaJugadores[pos].dni)
                        db.collection("Equipos").document(listaJugadores[pos].equipo).update(
                            hashMapOf(
                                "Jugadores" to listJugador
                            ) as HashMap<String?, Any>
                        ).addOnSuccessListener {
                            llenarListView()
                            dialog.hide()
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
        listaJugadores.clear()
        db.collection("Jugadores").get().addOnSuccessListener {
            for (jugadores in it) {
                val jugador = Jugadores(
                    jugadores.get("Nombre").toString(),
                    jugadores.get("Apellido1").toString(),
                    jugadores.get("Apellido2").toString(),
                    jugadores.get("Dorsal").toString(),
                    jugadores.get("Categoria").toString(),
                    jugadores.get("Sexo").toString(),
                    jugadores.get("Equipo").toString(),
                    jugadores.id,
                    jugadores.get("UrlFoto").toString()
                )
                listaJugadores.add(jugador)
            }

            val adapter = AdaptadorJugador(binding.root.context, listaJugadores)

            binding.ListViewJugador.adapter = adapter
        }
    }

}