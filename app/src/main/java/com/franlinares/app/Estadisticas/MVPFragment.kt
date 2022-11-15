package com.franlinares.app.Estadisticas

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.franlinares.app.BaseFragment
import com.franlinares.app.Estadisticas.ListViewEstadisticas.AdaptadorMinuto
import com.franlinares.app.Estadisticas.ListViewEstadisticas.MinutoAMinuto
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentEnVivoBinding
import com.franlinares.app.databinding.FragmentMVPBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class MVPFragment : BaseFragment() {

    private var _binding: FragmentMVPBinding? = null
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
        _binding = FragmentMVPBinding.inflate(inflater, container, false)

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
        val mvpList = mutableListOf<Int>(0, 0, 0)
        val colorList = mutableListOf<Int>(0, 0, 0)
        db.collection("Estadisticas").document(idpartido.toString()).get().addOnSuccessListener {
            val listJugador = it.get("ListadoJugadores") as ArrayList<String>
            for (j in 0..listJugador.count() - 1) {
                val jugador =
                    (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()
                if (jugador["val"].toString().toInt() >= mvpList[0]) {
                    mvpList[2] = mvpList[1]
                    colorList[2] = colorList[1]
                    binding.txtMVPDorsal3.text = binding.txtMVPDorsal2.text
                    binding.txtMVPValoracion3.text = binding.txtMVPValoracion2.text
                    binding.txtMVPPuntos3.text = binding.txtMVPPuntos2.text
                    binding.txtMVPRebotes3.text = binding.txtMVPRebotes2.text
                    binding.txtMVPAsistencias3.text = binding.txtMVPAsistencias2.text
                    binding.txtMVPNombre3.text = binding.txtMVPNombre2.text
                    binding.lineaSeparadora3.setBackgroundColor(colorList[2])

                    mvpList[1] = mvpList[0]
                    colorList[1] = colorList[0]
                    binding.txtMVPDorsal2.text = binding.txtMVPDorsal1.text
                    binding.txtMVPValoracion2.text = binding.txtMVPValoracion1.text
                    binding.txtMVPPuntos2.text = binding.txtMVPPuntos1.text
                    binding.txtMVPRebotes2.text = binding.txtMVPRebotes1.text
                    binding.txtMVPAsistencias2.text = binding.txtMVPAsistencias1.text
                    binding.txtMVPNombre2.text = binding.txtMVPNombre1.text
                    binding.lineaSeparadora2.setBackgroundColor(colorList[1])

                    mvpList[0] = jugador["val"].toString().toInt()
                    binding.txtMVPDorsal1.text = jugador["dorsal"].toString()
                    binding.txtMVPValoracion1.text = jugador["val"].toString()
                    binding.txtMVPPuntos1.text = jugador["puntos"].toString()
                    binding.txtMVPRebotes1.text =
                        (jugador["rebO"].toString().toInt() + jugador["rebD"].toString()
                            .toInt()).toString()
                    binding.txtMVPAsistencias1.text = jugador["asi"].toString()
                    binding.txtMVPNombre1.text = jugador["nombre"].toString()
                    if (jugador["equipo"] == "Local") {
                        binding.lineaSeparadora.setBackgroundColor(Color.parseColor("#FF3D00"))
                        colorList[0] = Color.parseColor("#FF3D00")
                    } else {
                        binding.lineaSeparadora.setBackgroundColor(Color.parseColor("#00B0FF"))
                        colorList[0] = Color.parseColor("#00B0FF")
                    }


                } else if (jugador["val"].toString().toInt() >= mvpList[1]) {

                    mvpList[2] = mvpList[1]
                    colorList[2] = colorList[1]
                    binding.txtMVPDorsal3.text = binding.txtMVPDorsal2.text
                    binding.txtMVPValoracion3.text = binding.txtMVPValoracion2.text
                    binding.txtMVPPuntos3.text = binding.txtMVPPuntos2.text
                    binding.txtMVPRebotes3.text = binding.txtMVPRebotes2.text
                    binding.txtMVPAsistencias3.text = binding.txtMVPAsistencias2.text
                    binding.txtMVPNombre3.text = binding.txtMVPNombre2.text
                    binding.lineaSeparadora3.setBackgroundColor(colorList[2])

                    mvpList[1] = jugador["val"].toString().toInt()
                    binding.txtMVPDorsal2.text = jugador["dorsal"].toString()
                    binding.txtMVPValoracion2.text = jugador["val"].toString()
                    binding.txtMVPPuntos2.text = jugador["puntos"].toString()
                    binding.txtMVPRebotes2.text =
                        (jugador["rebO"].toString().toInt() + jugador["rebD"].toString()
                            .toInt()).toString()
                    binding.txtMVPAsistencias2.text = jugador["asi"].toString()
                    binding.txtMVPNombre2.text = jugador["nombre"].toString()
                    if (jugador["equipo"] == "Local") {
                        binding.lineaSeparadora2.setBackgroundColor(Color.parseColor("#FF3D00"))
                        colorList[1] = Color.parseColor("#FF3D00")
                    } else {
                        binding.lineaSeparadora2.setBackgroundColor(Color.parseColor("#00B0FF"))
                        colorList[1] = Color.parseColor("#00B0FF")
                    }

                } else if (jugador["val"].toString().toInt() >= mvpList[2]) {
                    mvpList[2] = jugador["val"].toString().toInt()
                    binding.txtMVPDorsal3.text = jugador["dorsal"].toString()
                    binding.txtMVPValoracion3.text = jugador["val"].toString()
                    binding.txtMVPPuntos3.text = jugador["puntos"].toString()
                    binding.txtMVPRebotes3.text =
                        (jugador["rebO"].toString().toInt() + jugador["rebD"].toString()
                            .toInt()).toString()
                    binding.txtMVPAsistencias3.text = jugador["asi"].toString()
                    binding.txtMVPNombre3.text = jugador["nombre"].toString()
                    if (jugador["equipo"] == "Local") {
                        binding.lineaSeparadora3.setBackgroundColor(Color.parseColor("#FF3D00"))
                        colorList[2] = Color.parseColor("#FF3D00")
                    } else {
                        binding.lineaSeparadora3.setBackgroundColor(Color.parseColor("#00B0FF"))
                        colorList[2] = Color.parseColor("#00B0FF")
                    }
                }
            }
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
                    db.collection("Partidos").document(idpartido.toString()).get()
                        .addOnSuccessListener { documentSnapshot ->
                            binding.txtPuntosLocalPartido.text =
                                (documentSnapshot.get("Resultado").toString().split(":"))[0]
                            binding.txtPuntosVisitantePartido.text =
                                (documentSnapshot.get("Resultado").toString().split(":"))[1]
                        }
                    val mvpList = mutableListOf<Int>(0, 0, 0)
                    val colorList = mutableListOf<Int>(0, 0, 0)
                    db.collection("Estadisticas").document(idpartido.toString()).get().addOnSuccessListener {
                        val listJugador = it.get("ListadoJugadores") as ArrayList<String>
                        for (j in 0..listJugador.count() - 1) {
                            val jugador =
                                (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()
                            if (jugador["val"].toString().toInt() >= mvpList[0]) {
                                mvpList[2] = mvpList[1]
                                colorList[2] = colorList[1]
                                binding.txtMVPDorsal3.text = binding.txtMVPDorsal2.text
                                binding.txtMVPValoracion3.text = binding.txtMVPValoracion2.text
                                binding.txtMVPPuntos3.text = binding.txtMVPPuntos2.text
                                binding.txtMVPRebotes3.text = binding.txtMVPRebotes2.text
                                binding.txtMVPAsistencias3.text = binding.txtMVPAsistencias2.text
                                binding.txtMVPNombre3.text = binding.txtMVPNombre2.text
                                binding.lineaSeparadora3.setBackgroundColor(colorList[2])

                                mvpList[1] = mvpList[0]
                                colorList[1] = colorList[0]
                                binding.txtMVPDorsal2.text = binding.txtMVPDorsal1.text
                                binding.txtMVPValoracion2.text = binding.txtMVPValoracion1.text
                                binding.txtMVPPuntos2.text = binding.txtMVPPuntos1.text
                                binding.txtMVPRebotes2.text = binding.txtMVPRebotes1.text
                                binding.txtMVPAsistencias2.text = binding.txtMVPAsistencias1.text
                                binding.txtMVPNombre2.text = binding.txtMVPNombre1.text
                                binding.lineaSeparadora2.setBackgroundColor(colorList[1])

                                mvpList[0] = jugador["val"].toString().toInt()
                                binding.txtMVPDorsal1.text = jugador["dorsal"].toString()
                                binding.txtMVPValoracion1.text = jugador["val"].toString()
                                binding.txtMVPPuntos1.text = jugador["puntos"].toString()
                                binding.txtMVPRebotes1.text =
                                    (jugador["rebO"].toString().toInt() + jugador["rebD"].toString()
                                        .toInt()).toString()
                                binding.txtMVPAsistencias1.text = jugador["asi"].toString()
                                binding.txtMVPNombre1.text = jugador["nombre"].toString()
                                if (jugador["equipo"] == "Local") {
                                    binding.lineaSeparadora.setBackgroundColor(Color.parseColor("#FF3D00"))
                                    colorList[0] = Color.parseColor("#FF3D00")
                                } else {
                                    binding.lineaSeparadora.setBackgroundColor(Color.parseColor("#00B0FF"))
                                    colorList[0] = Color.parseColor("#00B0FF")
                                }


                            } else if (jugador["val"].toString().toInt() >= mvpList[1]) {

                                mvpList[2] = mvpList[1]
                                colorList[2] = colorList[1]
                                binding.txtMVPDorsal3.text = binding.txtMVPDorsal2.text
                                binding.txtMVPValoracion3.text = binding.txtMVPValoracion2.text
                                binding.txtMVPPuntos3.text = binding.txtMVPPuntos2.text
                                binding.txtMVPRebotes3.text = binding.txtMVPRebotes2.text
                                binding.txtMVPAsistencias3.text = binding.txtMVPAsistencias2.text
                                binding.txtMVPNombre3.text = binding.txtMVPNombre2.text
                                binding.lineaSeparadora3.setBackgroundColor(colorList[2])

                                mvpList[1] = jugador["val"].toString().toInt()
                                binding.txtMVPDorsal2.text = jugador["dorsal"].toString()
                                binding.txtMVPValoracion2.text = jugador["val"].toString()
                                binding.txtMVPPuntos2.text = jugador["puntos"].toString()
                                binding.txtMVPRebotes2.text =
                                    (jugador["rebO"].toString().toInt() + jugador["rebD"].toString()
                                        .toInt()).toString()
                                binding.txtMVPAsistencias2.text = jugador["asi"].toString()
                                binding.txtMVPNombre2.text = jugador["nombre"].toString()
                                if (jugador["equipo"] == "Local") {
                                    binding.lineaSeparadora2.setBackgroundColor(Color.parseColor("#FF3D00"))
                                    colorList[1] = Color.parseColor("#FF3D00")
                                } else {
                                    binding.lineaSeparadora2.setBackgroundColor(Color.parseColor("#00B0FF"))
                                    colorList[1] = Color.parseColor("#00B0FF")
                                }

                            } else if (jugador["val"].toString().toInt() >= mvpList[2]) {
                                mvpList[2] = jugador["val"].toString().toInt()
                                binding.txtMVPDorsal3.text = jugador["dorsal"].toString()
                                binding.txtMVPValoracion3.text = jugador["val"].toString()
                                binding.txtMVPPuntos3.text = jugador["puntos"].toString()
                                binding.txtMVPRebotes3.text =
                                    (jugador["rebO"].toString().toInt() + jugador["rebD"].toString()
                                        .toInt()).toString()
                                binding.txtMVPAsistencias3.text = jugador["asi"].toString()
                                binding.txtMVPNombre3.text = jugador["nombre"].toString()
                                if (jugador["equipo"] == "Local") {
                                    binding.lineaSeparadora3.setBackgroundColor(Color.parseColor("#FF3D00"))
                                    colorList[2] = Color.parseColor("#FF3D00")
                                } else {
                                    binding.lineaSeparadora3.setBackgroundColor(Color.parseColor("#00B0FF"))
                                    colorList[2] = Color.parseColor("#00B0FF")
                                }
                            }
                        }
                    }

                }
            }
        }).start()

        return binding.root
    }

}