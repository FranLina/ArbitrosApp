package com.franlinares.app.Liga

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.Navigation
import com.franlinares.app.BaseFragment
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentEditarPartidoJornadaBinding
import com.franlinares.app.databinding.FragmentGestionarJornadaLigaBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class EditarPartidoJornadaFragment : BaseFragment() {

    private var _binding: FragmentEditarPartidoJornadaBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditarPartidoJornadaBinding.inflate(inflater, container, false)

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val partido = prefs.getString("idPartido", "")
        val editor = prefs.edit()

        if (partido != null) {
            db.collection("Partidos").document(partido).get()
                .addOnSuccessListener { documentSnapshot ->
                    editor.putString("liga", documentSnapshot.get("Liga").toString())
                    editor.apply()
                    binding.txtEEJornada.setText(
                        "Jornada " + documentSnapshot.get("Jornada").toString()
                    )
                    binding.txtEEPolideportivo.setText(
                        documentSnapshot.get("Polideportivo").toString()
                    )
                    binding.txtEEFecha.setText(documentSnapshot.get("Fecha").toString())
                    binding.txtEEHora.setText(documentSnapshot.get("Hora").toString())
                    binding.txtEENombreLocal.setText(documentSnapshot.get("EquipoLocal").toString())
                    binding.txtEENombreVisitante.setText(
                        documentSnapshot.get("EquipoVisitante").toString()
                    )

                    db.collection("Equipos").document(binding.txtEENombreLocal.text.toString())
                        .get()
                        .addOnSuccessListener {
                            if (it.get("UrlFoto") != "") {
                                Picasso.get()
                                    .load(it.get("UrlFoto").toString())
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .error(R.drawable.ic_launcher_foreground)
                                    .into(binding.imageEELocal)
                            }
                        }
                    db.collection("Equipos").document(binding.txtEENombreVisitante.text.toString())
                        .get()
                        .addOnSuccessListener {
                            if (it.get("UrlFoto") != "") {
                                Picasso.get()
                                    .load(it.get("UrlFoto").toString())
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .error(R.drawable.ic_launcher_foreground)
                                    .into(binding.imageEEVisitante)
                            }
                        }
                }

            binding.btnEEModificar.setOnClickListener {
                db.collection("Partidos").document(partido).update(
                    hashMapOf(
                        "Polideportivo" to binding.txtEEPolideportivo.text.toString(),
                        "Fecha" to binding.txtEEFecha.text.toString(),
                        "Hora" to binding.txtEEHora.text.toString()
                    ) as Map<String, Any>
                ).addOnSuccessListener {
                    Toast.makeText(
                        binding.root.context,
                        "Se ha modificado con exito",
                        Toast.LENGTH_LONG
                    ).show()
                    editor.putString("idPartido", "")
                    editor.apply()
                }
            }

        }

        return binding.root
    }

}