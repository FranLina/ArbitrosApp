package com.franlinares.app.Liga

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.franlinares.app.BaseFragment
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentDesignacionPartidoBinding
import com.franlinares.app.databinding.FragmentDesignarArbitrosPartidoBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class DesignarArbitrosPartidoFragment : BaseFragment() {

    private var _binding: FragmentDesignarArbitrosPartidoBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDesignarArbitrosPartidoBinding.inflate(inflater, container, false)
        cargarDatos()
        llenarSpinner()

        binding.btnEPJDesignar.setOnClickListener {
            if (binding.spinnerArbitroP.selectedItem != binding.spinnerArbitroA.selectedItem &&
                binding.spinnerArbitroP.selectedItem != binding.spinnerAnotador.selectedItem &&
                binding.spinnerArbitroP.selectedItem != binding.spinnerCronometrador.selectedItem &&
                (binding.spinnerArbitroA.selectedItem != binding.spinnerCronometrador.selectedItem || (binding.spinnerArbitroA.selectedItem == "" && binding.spinnerCronometrador.selectedItem == "")) &&
                binding.spinnerArbitroA.selectedItem != binding.spinnerAnotador.selectedItem &&
                binding.spinnerCronometrador.selectedItem != binding.spinnerAnotador.selectedItem
            ) {
                val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                val partido = prefs.getString("idPartido", "")
                val arbitroP = binding.spinnerArbitroP.selectedItem.toString().split("-")
                val arbitroA = binding.spinnerArbitroA.selectedItem.toString().split("-")
                val anotador = binding.spinnerAnotador.selectedItem.toString().split("-")
                val cronometrador = binding.spinnerCronometrador.selectedItem.toString().split("-")
                if (partido != null) {
                    db.collection("designaciones").document(partido).set(
                        hashMapOf(
                            "ArbitroP" to arbitroP[0].trim(),
                            "ArbitroA" to arbitroA[0].trim(),
                            "Anotador" to anotador[0].trim(),
                            "Cronometrador" to cronometrador[0].trim()
                        ) as Map<String, Any>
                    ).addOnSuccessListener {

                        if (arbitroP[0].toString() != "")
                            db.collection("Mesa_oficial")
                                .document(arbitroP[0].trim().toString())
                                .get()
                                .addOnSuccessListener {
                                    val listPartidos =
                                        it.get("partidosAsignados") as ArrayList<String>
                                    listPartidos.add(partido)
                                    db.collection("Mesa_oficial")
                                        .document(arbitroP[0].trim().toString()).update(
                                            hashMapOf(
                                                "partidosAsignados" to listPartidos
                                            ) as Map<String, Any>
                                        )
                                }
                        if (arbitroA[0].toString() != "")
                            db.collection("Mesa_oficial")
                                .document(arbitroA[0].trim().toString())
                                .get()
                                .addOnSuccessListener {
                                    val listPartidos =
                                        it.get("partidosAsignados") as ArrayList<String>
                                    listPartidos.add(partido)
                                    db.collection("Mesa_oficial")
                                        .document(arbitroA[0].trim().toString()).update(
                                            hashMapOf(
                                                "partidosAsignados" to listPartidos
                                            ) as Map<String, Any>
                                        )
                                }
                        if (cronometrador[0].toString() != "")
                            db.collection("Mesa_oficial")
                                .document(cronometrador[0].trim().toString())
                                .get()
                                .addOnSuccessListener {
                                    val listPartidos =
                                        it.get("partidosAsignados") as ArrayList<String>
                                    listPartidos.add(partido)
                                    db.collection("Mesa_oficial")
                                        .document(cronometrador[0].trim().toString()).update(
                                            hashMapOf(
                                                "partidosAsignados" to listPartidos
                                            ) as Map<String, Any>
                                        )
                                }
                        if (anotador[0].toString() != "")
                            db.collection("Mesa_oficial")
                                .document(anotador[0].trim().toString())
                                .get()
                                .addOnSuccessListener {
                                    val listPartidos =
                                        it.get("partidosAsignados") as ArrayList<String>
                                    listPartidos.add(partido)
                                    db.collection("Mesa_oficial")
                                        .document(anotador[0].trim().toString()).update(
                                            hashMapOf(
                                                "partidosAsignados" to listPartidos
                                            ) as Map<String, Any>
                                        ).addOnSuccessListener {
                                            Toast.makeText(
                                                binding.root.context,
                                                "Designaciones añadidas con exito",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                }
                    }
                }

            } else {
                Toast.makeText(
                    binding.root.context,
                    "No puede repetirse la misma persona, para ambos puestos",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        return binding.root
    }

    fun cargarDatos() {

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val partido = prefs.getString("idPartido", "")
        val editor = prefs.edit()

        if (partido != null) {
            db.collection("Partidos").document(partido).get()
                .addOnSuccessListener { documentSnapshot ->
                    binding.txtEPJJornada.setText(
                        "Jornada " + documentSnapshot.get("Jornada").toString()
                    )
                    binding.txtEPJPolideportivo.setText(
                        documentSnapshot.get("Polideportivo").toString()
                    )
                    binding.txtEPJFecha.setText(documentSnapshot.get("Fecha").toString())
                    binding.txtEPJHora.setText(documentSnapshot.get("Hora").toString())
                    binding.txtEPJLocal.setText(documentSnapshot.get("EquipoLocal").toString())
                    binding.txtEPJVisitante.setText(
                        documentSnapshot.get("EquipoVisitante").toString()
                    )

                    db.collection("Equipos").document(binding.txtEPJLocal.text.toString())
                        .get()
                        .addOnSuccessListener {
                            if (it.get("UrlFoto") != "") {
                                Picasso.get()
                                    .load(it.get("UrlFoto").toString())
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .error(R.drawable.ic_launcher_foreground)
                                    .into(binding.imageEPJlocal)
                            }
                        }
                    db.collection("Equipos").document(binding.txtEPJVisitante.text.toString())
                        .get()
                        .addOnSuccessListener {
                            if (it.get("UrlFoto") != "") {
                                Picasso.get()
                                    .load(it.get("UrlFoto").toString())
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .error(R.drawable.ic_launcher_foreground)
                                    .into(binding.imageEPJVisitante)
                            }
                        }
                }
        }

    }

    fun llenarSpinner() {

        //SpinnerArbitrosP
        val listaArbitrosP = mutableListOf<String>()
        db.collection("Mesa_oficial").get().addOnSuccessListener {
            for (arbitros in it) {
                if (arbitros.get("admin") == false) {
                    listaArbitrosP.add(
                        arbitros.id + " - " + arbitros.get("nombre") + " " + arbitros.get(
                            "apellido1"
                        ) + " " + arbitros.get("apellido2")
                    )
                }
            }
            val adaptadorArbitro =
                ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_item)
            adaptadorArbitro.addAll(listaArbitrosP)
            binding.spinnerArbitroP.adapter = adaptadorArbitro
        }

        //SpinnerArbitrosA
        val listaArbitrosA = mutableListOf<String>()
        db.collection("Mesa_oficial").get().addOnSuccessListener {
            listaArbitrosA.add("")
            for (arbitros in it) {
                if (arbitros.get("admin") == false) {
                    listaArbitrosA.add(
                        arbitros.id + " - " + arbitros.get("nombre") + " " + arbitros.get(
                            "apellido1"
                        ) + " " + arbitros.get("apellido2")
                    )
                }
            }
            val adaptadorArbitroA =
                ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_item)
            adaptadorArbitroA.addAll(listaArbitrosA)
            binding.spinnerArbitroA.adapter = adaptadorArbitroA
        }

        //SpinnerAnotador
        val listaAnotador = mutableListOf<String>()
        db.collection("Mesa_oficial").get().addOnSuccessListener {
            for (arbitros in it) {
                if (arbitros.get("admin") == false) {
                    listaAnotador.add(
                        arbitros.id + " - " + arbitros.get("nombre") + " " + arbitros.get(
                            "apellido1"
                        ) + " " + arbitros.get("apellido2")
                    )
                }
            }
            val adaptadorAnotador =
                ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_item)
            adaptadorAnotador.addAll(listaAnotador)
            binding.spinnerAnotador.adapter = adaptadorAnotador
        }


        //SpinnerCronometrador
        val listaCronometrador = mutableListOf<String>()
        db.collection("Mesa_oficial").get().addOnSuccessListener {
            listaCronometrador.add("")
            for (arbitros in it) {
                if (arbitros.get("admin") == false) {
                    listaCronometrador.add(
                        arbitros.id + " - " + arbitros.get("nombre") + " " + arbitros.get(
                            "apellido1"
                        ) + " " + arbitros.get("apellido2")
                    )
                }
            }
            val adaptadorCronometrador =
                ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_item)
            adaptadorCronometrador.addAll(listaCronometrador)
            binding.spinnerCronometrador.adapter = adaptadorCronometrador
        }

    }

}