package com.franlinares.app.Estadisticas

import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.franlinares.app.BaseFragment
import com.franlinares.app.Estadisticas.ListViewEstadisticas.AdaptadorMinuto
import com.franlinares.app.Estadisticas.ListViewEstadisticas.MinutoAMinuto
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentEnVivoBinding
import com.franlinares.app.databinding.FragmentEstadisticasBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class EstadisticasFragment : BaseFragment() {

    private var _binding: FragmentEstadisticasBinding? = null
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
        _binding = FragmentEstadisticasBinding.inflate(inflater, container, false)

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val idpartido = prefs.getString("idPartido", "")

        db.collection("Partidos").document(idpartido.toString()).get()
            .addOnSuccessListener { documentSnapshot ->
                binding.txtNombreELocal.text = "  " + documentSnapshot.get("EquipoLocal").toString()
                binding.txtNombreEVisitante.text =
                    "  " + documentSnapshot.get("EquipoVisitante").toString()
                binding.txtNombreELocalEsta.text = documentSnapshot.get("EquipoLocal").toString()
                binding.txtNombreEVisitanteEsta.text =
                    documentSnapshot.get("EquipoVisitante").toString()
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
                            Picasso.get()
                                .load(it.get("UrlFoto").toString())
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .error(R.drawable.ic_launcher_foreground)
                                .into(binding.imageLocalPartidoEsta)
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
                            Picasso.get()
                                .load(it.get("UrlFoto").toString())
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .error(R.drawable.ic_launcher_foreground)
                                .into(binding.imageVisitantePartidoEsta)
                        }
                    }

            }
        val cabeceraL = LayoutInflater.from(binding.root.context).inflate(R.layout.row_cabecera, null, false)
        binding.TLLocal.addView(cabeceraL)
        val cabeceraV = LayoutInflater.from(binding.root.context).inflate(R.layout.row_cabecera, null, false)
        binding.TLVisitante.addView(cabeceraV)
        db.collection("Estadisticas").document(idpartido.toString()).get()
            .addOnSuccessListener {
                val listJugador = it.get("ListadoJugadores") as ArrayList<String>
                for (j in 0..listJugador.count() - 1) {
                    val jugador = (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()
                    if (jugador["equipo"] == "Local") {
                        val registro = LayoutInflater.from(binding.root.context)
                            .inflate(R.layout.row_estadistica, null, false)
                        registro.findViewById<TextView>(R.id.txtENombre).text =
                            jugador["nombre"].toString()
                        registro.findViewById<TextView>(R.id.txtEPuntos).text =
                            jugador["puntos"].toString()
                        registro.findViewById<TextView>(R.id.txtEDorsal).text =
                            jugador["dorsal"].toString()
                        registro.findViewById<TextView>(R.id.txtETC2P).text =
                            jugador["tc2pA"].toString() + "/" + (jugador["tc2pA"].toString()
                                .toInt() + jugador["tc2pF"].toString().toInt())
                        registro.findViewById<TextView>(R.id.txtETC3P).text =
                            jugador["tc3pA"].toString() + "/" + (jugador["tc3pA"].toString()
                                .toInt() + jugador["tc3pF"].toString().toInt())
                        registro.findViewById<TextView>(R.id.txtETL).text =
                            jugador["tlA"].toString() + "/" + (jugador["tlA"].toString()
                                .toInt() + jugador["tlF"].toString().toInt())
                        registro.findViewById<TextView>(R.id.txtERebO).text =
                            jugador["rebO"].toString()
                        registro.findViewById<TextView>(R.id.txtERebD).text =
                            jugador["rebD"].toString()
                        registro.findViewById<TextView>(R.id.txtEAsi).text =
                            jugador["asi"].toString()
                        registro.findViewById<TextView>(R.id.txtERec).text =
                            jugador["recu"].toString()
                        registro.findViewById<TextView>(R.id.txtEPer).text =
                            jugador["per"].toString()
                        registro.findViewById<TextView>(R.id.txtETapC).text =
                            jugador["taCom"].toString()
                        registro.findViewById<TextView>(R.id.txtETapR).text =
                            jugador["taRec"].toString()
                        registro.findViewById<TextView>(R.id.txtEFal).text =
                            jugador["fal"].toString()
                        registro.findViewById<TextView>(R.id.txtEVal).text =
                            jugador["val"].toString()
                        binding.TLLocal.addView(registro)
                    } else {
                        val registro = LayoutInflater.from(binding.root.context)
                            .inflate(R.layout.row_estadistica, null, false)
                        registro.findViewById<TextView>(R.id.txtENombre).text =
                            jugador["nombre"].toString()
                        registro.findViewById<TextView>(R.id.txtEPuntos).text =
                            jugador["puntos"].toString()
                        registro.findViewById<TextView>(R.id.txtEDorsal).text =
                            jugador["dorsal"].toString()
                        registro.findViewById<TextView>(R.id.txtETC2P).text =
                            jugador["tc2pA"].toString() + "/" + (jugador["tc2pA"].toString()
                                .toInt() + jugador["tc2pF"].toString().toInt())
                        registro.findViewById<TextView>(R.id.txtETC3P).text =
                            jugador["tc3pA"].toString() + "/" + (jugador["tc3pA"].toString()
                                .toInt() + jugador["tc3pF"].toString().toInt())
                        registro.findViewById<TextView>(R.id.txtETL).text =
                            jugador["tlA"].toString() + "/" + (jugador["tlA"].toString()
                                .toInt() + jugador["tlF"].toString().toInt())
                        registro.findViewById<TextView>(R.id.txtERebO).text =
                            jugador["rebO"].toString()
                        registro.findViewById<TextView>(R.id.txtERebD).text =
                            jugador["rebD"].toString()
                        registro.findViewById<TextView>(R.id.txtEAsi).text =
                            jugador["asi"].toString()
                        registro.findViewById<TextView>(R.id.txtERec).text =
                            jugador["recu"].toString()
                        registro.findViewById<TextView>(R.id.txtEPer).text =
                            jugador["per"].toString()
                        registro.findViewById<TextView>(R.id.txtETapC).text =
                            jugador["taCom"].toString()
                        registro.findViewById<TextView>(R.id.txtETapR).text =
                            jugador["taRec"].toString()
                        registro.findViewById<TextView>(R.id.txtEFal).text =
                            jugador["fal"].toString()
                        registro.findViewById<TextView>(R.id.txtEVal).text =
                            jugador["val"].toString()
                        binding.TLVisitante.addView(registro)
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
                    Thread.sleep(100)
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
                    binding.TLLocal.removeAllViewsInLayout()
                    binding.TLVisitante.removeAllViewsInLayout()

                    db.collection("Estadisticas").document(idpartido.toString()).get()
                        .addOnSuccessListener {
                            val listJugador = it.get("ListadoJugadores") as ArrayList<String>
                            val cabeceraL = LayoutInflater.from(binding.root.context)
                                .inflate(R.layout.row_cabecera, null, false)
                            val cabeceraV = LayoutInflater.from(binding.root.context)
                                .inflate(R.layout.row_cabecera, null, false)
                            binding.TLLocal.addView(cabeceraL)
                            binding.TLVisitante.addView(cabeceraV)
                            for (j in 0..listJugador.count() - 1) {
                                val jugador =
                                    (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()
                                if (jugador["equipo"] == "Local") {
                                    val registro = LayoutInflater.from(binding.root.context)
                                        .inflate(R.layout.row_estadistica, null, false)
                                    registro.findViewById<TextView>(R.id.txtENombre).text =
                                        jugador["nombre"].toString()
                                    registro.findViewById<TextView>(R.id.txtEPuntos).text =
                                        jugador["puntos"].toString()
                                    registro.findViewById<TextView>(R.id.txtEDorsal).text =
                                        jugador["dorsal"].toString()
                                    registro.findViewById<TextView>(R.id.txtETC2P).text =
                                        jugador["tc2pA"].toString() + "/" + (jugador["tc2pA"].toString()
                                            .toInt() + jugador["tc2pF"].toString().toInt())
                                    registro.findViewById<TextView>(R.id.txtETC3P).text =
                                        jugador["tc3pA"].toString() + "/" + (jugador["tc3pA"].toString()
                                            .toInt() + jugador["tc3pF"].toString().toInt())
                                    registro.findViewById<TextView>(R.id.txtETL).text =
                                        jugador["tlA"].toString() + "/" + (jugador["tlA"].toString()
                                            .toInt() + jugador["tlF"].toString().toInt())
                                    registro.findViewById<TextView>(R.id.txtERebO).text =
                                        jugador["rebO"].toString()
                                    registro.findViewById<TextView>(R.id.txtERebD).text =
                                        jugador["rebD"].toString()
                                    registro.findViewById<TextView>(R.id.txtEAsi).text =
                                        jugador["asi"].toString()
                                    registro.findViewById<TextView>(R.id.txtERec).text =
                                        jugador["recu"].toString()
                                    registro.findViewById<TextView>(R.id.txtEPer).text =
                                        jugador["per"].toString()
                                    registro.findViewById<TextView>(R.id.txtETapC).text =
                                        jugador["taCom"].toString()
                                    registro.findViewById<TextView>(R.id.txtETapR).text =
                                        jugador["taRec"].toString()
                                    registro.findViewById<TextView>(R.id.txtEFal).text =
                                        jugador["fal"].toString()
                                    registro.findViewById<TextView>(R.id.txtEVal).text =
                                        jugador["val"].toString()
                                    binding.TLLocal.addView(registro)
                                } else {
                                    val registro = LayoutInflater.from(binding.root.context)
                                        .inflate(R.layout.row_estadistica, null, false)
                                    registro.findViewById<TextView>(R.id.txtENombre).text =
                                        jugador["nombre"].toString()
                                    registro.findViewById<TextView>(R.id.txtEPuntos).text =
                                        jugador["puntos"].toString()
                                    registro.findViewById<TextView>(R.id.txtEDorsal).text =
                                        jugador["dorsal"].toString()
                                    registro.findViewById<TextView>(R.id.txtETC2P).text =
                                        jugador["tc2pA"].toString() + "/" + (jugador["tc2pA"].toString()
                                            .toInt() + jugador["tc2pF"].toString().toInt())
                                    registro.findViewById<TextView>(R.id.txtETC3P).text =
                                        jugador["tc3pA"].toString() + "/" + (jugador["tc3pA"].toString()
                                            .toInt() + jugador["tc3pF"].toString().toInt())
                                    registro.findViewById<TextView>(R.id.txtETL).text =
                                        jugador["tlA"].toString() + "/" + (jugador["tlA"].toString()
                                            .toInt() + jugador["tlF"].toString().toInt())
                                    registro.findViewById<TextView>(R.id.txtERebO).text =
                                        jugador["rebO"].toString()
                                    registro.findViewById<TextView>(R.id.txtERebD).text =
                                        jugador["rebD"].toString()
                                    registro.findViewById<TextView>(R.id.txtEAsi).text =
                                        jugador["asi"].toString()
                                    registro.findViewById<TextView>(R.id.txtERec).text =
                                        jugador["recu"].toString()
                                    registro.findViewById<TextView>(R.id.txtEPer).text =
                                        jugador["per"].toString()
                                    registro.findViewById<TextView>(R.id.txtETapC).text =
                                        jugador["taCom"].toString()
                                    registro.findViewById<TextView>(R.id.txtETapR).text =
                                        jugador["taRec"].toString()
                                    registro.findViewById<TextView>(R.id.txtEFal).text =
                                        jugador["fal"].toString()
                                    registro.findViewById<TextView>(R.id.txtEVal).text =
                                        jugador["val"].toString()
                                    binding.TLVisitante.addView(registro)
                                }
                            }
                        }
                }
            }
        }).start()


        return binding.root
    }
}