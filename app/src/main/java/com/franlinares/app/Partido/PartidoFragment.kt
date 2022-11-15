package com.franlinares.app.Partido

import android.app.AlertDialog
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.franlinares.app.BaseFragment
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentPartidoBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PartidoFragment : BaseFragment() {

    private var _binding: FragmentPartidoBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    var cuarto = 1
    var isPlay = false
    var pauseOffSet: Long = 10 * 60 * 1000


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPartidoBinding.inflate(inflater, container, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.TiempoCuarto.isCountDown = true
        }
        binding.TiempoCuarto.base = SystemClock.elapsedRealtime() + 10 * 60 * 1000
        quintetoLocal()

        binding.TiempoCuarto.setOnChronometerTickListener {
            if (it.text.toString().equals("00:00")) {
                if (cuarto == 4) {
                    val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                    val idpartido = prefs.getString("idPartido", "")
                    db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                        .addOnSuccessListener { it2 ->
                            val listRegistros =
                                it2.get("registro") as ArrayList<Map<String?, Any?>>
                            val registro = hashMapOf(
                                "cuarto" to cuarto,
                                "dorsal" to "",
                                "frase" to "FIN DEL PERIODO",
                                "resultado" to "",
                                "tiempo" to "",
                                "equipo" to "",
                                "tipoFrase" to "4"
                            ) as Map<String?, Any?>
                            listRegistros.add(registro)
                            val registro2 = hashMapOf(
                                "cuarto" to "",
                                "dorsal" to "",
                                "frase" to "FIN DEL PARTIDO",
                                "resultado" to "",
                                "tiempo" to "",
                                "equipo" to "",
                                "tipoFrase" to "4"
                            ) as Map<String?, Any?>
                            listRegistros.add(registro2)
                            db.collection("MinutoaMinuto")
                                .document(idpartido.toString())
                                .update(
                                    hashMapOf(
                                        "registro" to listRegistros,
                                    ) as Map<String?, Any?>
                                )
                        }
                    db.collection("Partidos").document(idpartido.toString()).update(
                        hashMapOf(
                            "Estado" to "Finalizado"
                        ) as Map<String?, Any?>
                    ).addOnSuccessListener {

                    }
                } else {
                    val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                    val idpartido = prefs.getString("idPartido", "")
                    binding.txtFaltasLocal.text = "Faltas: 0"
                    binding.txtFaltasVisitante.text = "Faltas: 0"
                    if (cuarto == 2) {
                        binding.txtTiemposMLocal.text = "Tiempos: 3"
                        binding.txtTiemposMVisitante.text = "Tiempos: 3"
                    }
                    db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                        .addOnSuccessListener { it2 ->
                            val listRegistros =
                                it2.get("registro") as ArrayList<Map<String?, Any?>>
                            val registro = hashMapOf(
                                "cuarto" to cuarto,
                                "dorsal" to "",
                                "frase" to "FIN DEL PERIODO",
                                "resultado" to "",
                                "tiempo" to "",
                                "equipo" to "",
                                "tipoFrase" to "4"
                            ) as Map<String?, Any?>
                            listRegistros.add(registro)
                            val registro2 = hashMapOf(
                                "cuarto" to cuarto + 1,
                                "dorsal" to "",
                                "frase" to "INICIO DEL PERIODO",
                                "resultado" to "",
                                "tiempo" to "",
                                "equipo" to "",
                                "tipoFrase" to "2"
                            ) as Map<String?, Any?>
                            listRegistros.add(registro2)
                            db.collection("MinutoaMinuto")
                                .document(idpartido.toString())
                                .update(
                                    hashMapOf(
                                        "registro" to listRegistros,
                                    ) as Map<String?, Any?>
                                ).addOnSuccessListener {
                                    binding.TiempoCuarto.stop()
                                    cuarto++
                                    binding.txtCuartoPartido.text = "Cuarto " + cuarto
                                    binding.TiempoCuarto.text = "10:00"
                                    isPlay = false
                                    pauseOffSet = 10 * 60 * 1000
                                }
                        }
                }
            }
        }

        //Cambios Local
        binding.TBLocal1.setOnLongClickListener(View.OnLongClickListener {
            hacerCambiosLocal(llenarListToggleLocal(), binding.TBLocal1)
            return@OnLongClickListener true
        })
        binding.TBLocal2.setOnLongClickListener(View.OnLongClickListener {
            hacerCambiosLocal(llenarListToggleLocal(), binding.TBLocal2)
            return@OnLongClickListener true
        })
        binding.TBLocal3.setOnLongClickListener(View.OnLongClickListener {
            hacerCambiosLocal(llenarListToggleLocal(), binding.TBLocal3)
            return@OnLongClickListener true
        })
        binding.TBLocal4.setOnLongClickListener(View.OnLongClickListener {
            hacerCambiosLocal(llenarListToggleLocal(), binding.TBLocal4)
            return@OnLongClickListener true
        })
        binding.TBLocal5.setOnLongClickListener(View.OnLongClickListener {
            hacerCambiosLocal(llenarListToggleLocal(), binding.TBLocal5)
            return@OnLongClickListener true
        })

        //Cambios Visitante
        binding.TBVisitante1.setOnLongClickListener(View.OnLongClickListener {
            hacerCambiosVisitante(llenarListToggleVisitante(), binding.TBVisitante1)
            return@OnLongClickListener true
        })
        binding.TBVisitante2.setOnLongClickListener(View.OnLongClickListener {
            hacerCambiosVisitante(llenarListToggleVisitante(), binding.TBVisitante2)
            return@OnLongClickListener true
        })
        binding.TBVisitante3.setOnLongClickListener(View.OnLongClickListener {
            hacerCambiosVisitante(llenarListToggleVisitante(), binding.TBVisitante3)
            return@OnLongClickListener true
        })
        binding.TBVisitante4.setOnLongClickListener(View.OnLongClickListener {
            hacerCambiosVisitante(llenarListToggleVisitante(), binding.TBVisitante4)
            return@OnLongClickListener true
        })
        binding.TBVisitante5.setOnLongClickListener(View.OnLongClickListener {
            hacerCambiosVisitante(llenarListToggleVisitante(), binding.TBVisitante5)
            return@OnLongClickListener true
        })

        //Pulsar boton Local
        binding.TBLocal1.setOnCheckedChangeListener { compoundButton, b ->
            if (binding.TBLocal1.isChecked) {
                vaciarToggle(llenarListToggle())
                binding.TBLocal1.isChecked = true
                binding.TBLocal1.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonlocalactivado))
            } else {
                binding.TBLocal1.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonlocaldesactivado))
            }
        }
        binding.TBLocal2.setOnCheckedChangeListener { compoundButton, b ->
            if (binding.TBLocal2.isChecked) {
                vaciarToggle(llenarListToggle())
                binding.TBLocal2.isChecked = true
                binding.TBLocal2.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonlocalactivado))
            } else {
                binding.TBLocal2.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonlocaldesactivado))
            }
        }
        binding.TBLocal3.setOnCheckedChangeListener { compoundButton, b ->
            if (binding.TBLocal3.isChecked) {
                vaciarToggle(llenarListToggle())
                binding.TBLocal3.isChecked = true
                binding.TBLocal3.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonlocalactivado))
            } else {
                binding.TBLocal3.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonlocaldesactivado))
            }
        }
        binding.TBLocal4.setOnCheckedChangeListener { compoundButton, b ->
            if (binding.TBLocal4.isChecked) {
                vaciarToggle(llenarListToggle())
                binding.TBLocal4.isChecked = true
                binding.TBLocal4.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonlocalactivado))
            } else {
                binding.TBLocal4.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonlocaldesactivado))
            }
        }
        binding.TBLocal5.setOnCheckedChangeListener { compoundButton, b ->
            if (binding.TBLocal5.isChecked) {
                vaciarToggle(llenarListToggle())
                binding.TBLocal5.isChecked = true
                binding.TBLocal5.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonlocalactivado))
            } else {
                binding.TBLocal5.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonlocaldesactivado))
            }
        }

        //Pulsar boton Visitante
        binding.TBVisitante1.setOnCheckedChangeListener { compoundButton, b ->
            if (binding.TBVisitante1.isChecked) {
                vaciarToggle(llenarListToggle())
                binding.TBVisitante1.isChecked = true
                binding.TBVisitante1.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonvisitanteactivo))
            } else {
                binding.TBVisitante1.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonvisitantedesactivado))
            }
        }
        binding.TBVisitante2.setOnCheckedChangeListener { compoundButton, b ->
            if (binding.TBVisitante2.isChecked) {
                vaciarToggle(llenarListToggle())
                binding.TBVisitante2.isChecked = true
                binding.TBVisitante2.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonvisitanteactivo))
            } else {
                binding.TBVisitante2.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonvisitantedesactivado))
            }
        }
        binding.TBVisitante3.setOnCheckedChangeListener { compoundButton, b ->
            if (binding.TBVisitante3.isChecked) {
                vaciarToggle(llenarListToggle())
                binding.TBVisitante3.isChecked = true
                binding.TBVisitante3.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonvisitanteactivo))
            } else {
                binding.TBVisitante3.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonvisitantedesactivado))
            }
        }
        binding.TBVisitante4.setOnCheckedChangeListener { compoundButton, b ->
            if (binding.TBVisitante4.isChecked) {
                vaciarToggle(llenarListToggle())
                binding.TBVisitante4.isChecked = true
                binding.TBVisitante4.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonvisitanteactivo))
            } else {
                binding.TBVisitante4.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonvisitantedesactivado))
            }
        }
        binding.TBVisitante5.setOnCheckedChangeListener { compoundButton, b ->
            if (binding.TBVisitante5.isChecked) {
                vaciarToggle(llenarListToggle())
                binding.TBVisitante5.isChecked = true
                binding.TBVisitante5.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonvisitanteactivo))
            } else {
                binding.TBVisitante5.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonvisitantedesactivado))
            }
        }

        //acciones
        binding.txtTiemposMLocal.setOnClickListener {

            val TiempoEquipoL = binding.txtTiemposMLocal.text.split(":")
            val TiempoL = TiempoEquipoL[1].trim()
            if (TiempoL.toInt() > 0) {
                val builder = AlertDialog.Builder(binding.root.context)
                val view = layoutInflater.inflate(R.layout.cronometro_tiempo_muerto, null)
                builder.setView(view)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.findViewById<Chronometer>(R.id.CronometroTM).isCountDown = true
                }

                view.findViewById<Chronometer>(R.id.CronometroTM).base =
                    SystemClock.elapsedRealtime() + 10 * 60 * 100
                view.findViewById<Chronometer>(R.id.CronometroTM)
                    .setBackgroundResource(R.drawable.bg_round)
                view.findViewById<Chronometer>(R.id.CronometroTM).start()

                val dialog = builder.create()
                dialog.show()

                view.findViewById<Chronometer>(R.id.CronometroTM).setOnChronometerTickListener {
                    if (it.text.toString().equals("00:00")) {
                        dialog.hide()
                    }
                }
                binding.txtTiemposMLocal.text = "Tiempos: " + (TiempoL.toInt() - 1).toString()
            }


        }
        binding.txtTiemposMVisitante.setOnClickListener {

            val TiempoEquipoV = binding.txtTiemposMVisitante.text.split(":")
            val TiempoV = TiempoEquipoV[1].trim()

            if (TiempoV.toInt() > 0) {
                val builder = AlertDialog.Builder(binding.root.context)
                val view = layoutInflater.inflate(R.layout.cronometro_tiempo_muerto, null)
                builder.setView(view)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.findViewById<Chronometer>(R.id.CronometroTM).isCountDown = true
                }

                view.findViewById<Chronometer>(R.id.CronometroTM).base =
                    SystemClock.elapsedRealtime() + 10 * 60 * 100
                view.findViewById<Chronometer>(R.id.CronometroTM)
                    .setBackgroundResource(R.drawable.bg_round_visitante)
                view.findViewById<Chronometer>(R.id.CronometroTM).start()

                val dialog = builder.create()
                dialog.show()

                view.findViewById<Chronometer>(R.id.CronometroTM).setOnChronometerTickListener {
                    if (it.text.toString().equals("00:00")) {
                        dialog.hide()
                    }
                }
                binding.txtTiemposMVisitante.text = "Tiempos: " + (TiempoV.toInt() - 1).toString()
            }

        }

        binding.btnTL.setOnClickListener {
            val builder = AlertDialog.Builder(binding.root.context)
            val view = layoutInflater.inflate(R.layout.accion_tl, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()

            view.findViewById<Button>(R.id.btnADGuardarTL).setOnClickListener {

                val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                val idpartido = prefs.getString("idPartido", "")
                val lista = llenarListToggle()
                for (i in 0..lista.count() - 1) {
                    if (lista[i].isChecked) {
                        if (lista[i].id == R.id.TBLocal1 || lista[i].id == R.id.TBLocal2 || lista[i].id == R.id.TBLocal3 || lista[i].id == R.id.TBLocal4 || lista[i].id == R.id.TBLocal5) {
                            db.collection("Estadisticas").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    var countTL = 0
                                    val listJugador =
                                        it.get("ListadoJugadores") as ArrayList<String>
                                    for (j in 0..listJugador.count() - 1) {
                                        val jugador =
                                            (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()
                                        if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Local") {

                                            if (view.findViewById<RadioButton>(R.id.rb1tlA).isChecked || view.findViewById<RadioButton>(
                                                    R.id.rb1tlF
                                                ).isChecked
                                            )
                                                countTL++
                                            if (view.findViewById<RadioButton>(R.id.rb2tlA).isChecked || view.findViewById<RadioButton>(
                                                    R.id.rb2tlF
                                                ).isChecked
                                            )
                                                countTL++
                                            if (view.findViewById<RadioButton>(R.id.rb3tlA).isChecked || view.findViewById<RadioButton>(
                                                    R.id.rb3tlF
                                                ).isChecked
                                            )
                                                countTL++

                                            db.collection("MinutoaMinuto")
                                                .document(idpartido.toString()).get()
                                                .addOnSuccessListener { it2 ->
                                                    val listRegistros =
                                                        it2.get("registro") as ArrayList<Map<String?, Any?>>
                                                    val registro = hashMapOf(
                                                        "cuarto" to cuarto,
                                                        "dorsal" to lista[i].text,
                                                        "frase" to countTL.toString() + " TIROS LIBRES PARA EL ",
                                                        "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                                        "tiempo" to binding.TiempoCuarto.text.toString(),
                                                        "equipo" to "Local",
                                                        "tipoFrase" to "3"
                                                    ) as Map<String?, Any?>
                                                    listRegistros.add(registro)

                                                    if (view.findViewById<RadioButton>(R.id.rb1tlA).isChecked) {
                                                        jugador["tlA"] =
                                                            jugador["tlA"].toString().toInt() + 1
                                                        jugador["puntos"] =
                                                            jugador["puntos"].toString().toInt() + 1
                                                        binding.txtPuntosLocal.text =
                                                            (binding.txtPuntosLocal.text.toString()
                                                                .toInt() + 1).toString()
                                                        db.collection("Partidos")
                                                            .document(idpartido.toString())
                                                            .update(
                                                                hashMapOf(
                                                                    "Resultado" to binding.txtPuntosLocal.text.toString() + ":" + binding.txtPuntosVisitante.text.toString()
                                                                ) as Map<String, Any>
                                                            )
                                                        Toast.makeText(
                                                            binding.root.context,
                                                            "Tiro Libre del jugador " + lista[i].textOn + " anotado",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        val registro = hashMapOf(
                                                            "cuarto" to cuarto,
                                                            "dorsal" to lista[i].text,
                                                            "frase" to "TIRO LIBRE ANOTADO",
                                                            "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                                            "equipo" to "Local",
                                                            "tipoFrase" to "1"
                                                        ) as Map<String?, Any?>
                                                        listRegistros.add(registro)

                                                    } else if (view.findViewById<RadioButton>(R.id.rb1tlF).isChecked) {

                                                        jugador["tlF"] =
                                                            jugador["tlF"].toString().toInt() + 1
                                                        Toast.makeText(
                                                            binding.root.context,
                                                            "Tiro Libre del jugador " + lista[i].textOn + " fallado",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        val registro = hashMapOf(
                                                            "cuarto" to cuarto,
                                                            "dorsal" to lista[i].text,
                                                            "frase" to "TIRO LIBRE FALLADO",
                                                            "resultado" to "",
                                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                                            "equipo" to "Local",
                                                            "tipoFrase" to "1"
                                                        ) as Map<String?, Any?>
                                                        listRegistros.add(registro)

                                                    }

                                                    if (view.findViewById<RadioButton>(R.id.rb2tlA).isChecked) {

                                                        jugador["tlA"] =
                                                            jugador["tlA"].toString().toInt() + 1
                                                        jugador["puntos"] =
                                                            jugador["puntos"].toString().toInt() + 1
                                                        binding.txtPuntosLocal.text =
                                                            (binding.txtPuntosLocal.text.toString()
                                                                .toInt() + 1).toString()
                                                        db.collection("Partidos")
                                                            .document(idpartido.toString())
                                                            .update(
                                                                hashMapOf(
                                                                    "Resultado" to binding.txtPuntosLocal.text.toString() + ":" + binding.txtPuntosVisitante.text.toString()
                                                                ) as Map<String, Any>
                                                            )
                                                        Toast.makeText(
                                                            binding.root.context,
                                                            "Tiro Libre del jugador " + lista[i].textOn + " anotado",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        val registro = hashMapOf(
                                                            "cuarto" to cuarto,
                                                            "dorsal" to lista[i].text,
                                                            "frase" to "TIRO LIBRE ANOTADO",
                                                            "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                                            "equipo" to "Local",
                                                            "tipoFrase" to "1"
                                                        ) as Map<String?, Any?>
                                                        listRegistros.add(registro)

                                                    } else if (view.findViewById<RadioButton>(R.id.rb2tlF).isChecked) {

                                                        jugador["tlF"] =
                                                            jugador["tlF"].toString().toInt() + 1
                                                        Toast.makeText(
                                                            binding.root.context,
                                                            "Tiro Libre del jugador " + lista[i].textOn + " fallado",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        val registro = hashMapOf(
                                                            "cuarto" to cuarto,
                                                            "dorsal" to lista[i].text,
                                                            "frase" to "TIRO LIBRE FALLADO",
                                                            "resultado" to "",
                                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                                            "equipo" to "Local",
                                                            "tipoFrase" to "1"
                                                        ) as Map<String?, Any?>
                                                        listRegistros.add(registro)

                                                    }

                                                    if (view.findViewById<RadioButton>(R.id.rb3tlA).isChecked) {

                                                        jugador["tlA"] =
                                                            jugador["tlA"].toString().toInt() + 1
                                                        jugador["puntos"] =
                                                            jugador["puntos"].toString().toInt() + 1
                                                        binding.txtPuntosLocal.text =
                                                            (binding.txtPuntosLocal.text.toString()
                                                                .toInt() + 1).toString()
                                                        db.collection("Partidos")
                                                            .document(idpartido.toString())
                                                            .update(
                                                                hashMapOf(
                                                                    "Resultado" to binding.txtPuntosLocal.text.toString() + ":" + binding.txtPuntosVisitante.text.toString()
                                                                ) as Map<String, Any>
                                                            )
                                                        Toast.makeText(
                                                            binding.root.context,
                                                            "Tiro Libre del jugador " + lista[i].textOn + " anotado",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        val registro = hashMapOf(
                                                            "cuarto" to cuarto,
                                                            "dorsal" to lista[i].text,
                                                            "frase" to "TIRO LIBRE ANOTADO",
                                                            "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                                            "equipo" to "Local",
                                                            "tipoFrase" to "1"
                                                        ) as Map<String?, Any?>
                                                        listRegistros.add(registro)

                                                    } else if (view.findViewById<RadioButton>(R.id.rb3tlF).isChecked) {

                                                        jugador["tlF"] =
                                                            jugador["tlF"].toString().toInt() + 1
                                                        Toast.makeText(
                                                            binding.root.context,
                                                            "Tiro Libre del jugador " + lista[i].textOn + " fallado",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        val registro = hashMapOf(
                                                            "cuarto" to cuarto,
                                                            "dorsal" to lista[i].text,
                                                            "frase" to "TIRO LIBRE FALLADO",
                                                            "resultado" to "",
                                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                                            "equipo" to "Local",
                                                            "tipoFrase" to "1"
                                                        ) as Map<String?, Any?>
                                                        listRegistros.add(registro)

                                                    }

                                                    db.collection("Estadisticas")
                                                        .document(idpartido.toString())
                                                        .update(
                                                            hashMapOf(
                                                                listJugador[j] to jugador
                                                            ) as Map<String, Any>
                                                        ).addOnSuccessListener {
                                                            calcularVal(listJugador[j], jugador)
                                                        }
                                                    db.collection("MinutoaMinuto")
                                                        .document(idpartido.toString())
                                                        .update(
                                                            hashMapOf(
                                                                "registro" to listRegistros,
                                                            ) as Map<String?, Any?>
                                                        )
                                                }

                                        }
                                    }
                                }
                        } else {

                            db.collection("Estadisticas").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    var countTL = 0
                                    val listJugador =
                                        it.get("ListadoJugadores") as ArrayList<String>
                                    for (j in 0..listJugador.count() - 1) {
                                        val jugador =
                                            (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()
                                        if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Visitante") {

                                            if (view.findViewById<RadioButton>(R.id.rb1tlA).isChecked || view.findViewById<RadioButton>(
                                                    R.id.rb1tlF
                                                ).isChecked
                                            )
                                                countTL++
                                            if (view.findViewById<RadioButton>(R.id.rb2tlA).isChecked || view.findViewById<RadioButton>(
                                                    R.id.rb2tlF
                                                ).isChecked
                                            )
                                                countTL++
                                            if (view.findViewById<RadioButton>(R.id.rb3tlA).isChecked || view.findViewById<RadioButton>(
                                                    R.id.rb3tlF
                                                ).isChecked
                                            )
                                                countTL++

                                            db.collection("MinutoaMinuto")
                                                .document(idpartido.toString()).get()
                                                .addOnSuccessListener { it2 ->
                                                    val listRegistros =
                                                        it2.get("registro") as ArrayList<Map<String?, Any?>>
                                                    val registro = hashMapOf(
                                                        "cuarto" to cuarto,
                                                        "dorsal" to lista[i].text,
                                                        "frase" to countTL.toString() + " TIROS LIBRES PARA EL ",
                                                        "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                                        "tiempo" to binding.TiempoCuarto.text.toString(),
                                                        "equipo" to "Visitante",
                                                        "tipoFrase" to "3"
                                                    ) as Map<String?, Any?>
                                                    listRegistros.add(registro)

                                                    if (view.findViewById<RadioButton>(R.id.rb1tlA).isChecked) {
                                                        jugador["tlA"] =
                                                            jugador["tlA"].toString().toInt() + 1
                                                        jugador["puntos"] =
                                                            jugador["puntos"].toString().toInt() + 1
                                                        binding.txtPuntosVisitante.text =
                                                            (binding.txtPuntosVisitante.text.toString()
                                                                .toInt() + 1).toString()
                                                        db.collection("Partidos")
                                                            .document(idpartido.toString())
                                                            .update(
                                                                hashMapOf(
                                                                    "Resultado" to binding.txtPuntosLocal.text.toString() + ":" + binding.txtPuntosVisitante.text.toString()
                                                                ) as Map<String, Any>
                                                            )
                                                        Toast.makeText(
                                                            binding.root.context,
                                                            "Tiro Libre del jugador " + lista[i].textOn + " anotado",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        val registro = hashMapOf(
                                                            "cuarto" to cuarto,
                                                            "dorsal" to lista[i].text,
                                                            "frase" to "TIRO LIBRE ANOTADO",
                                                            "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                                            "equipo" to "Visitante",
                                                            "tipoFrase" to "1"
                                                        ) as Map<String?, Any?>
                                                        listRegistros.add(registro)

                                                    } else if (view.findViewById<RadioButton>(R.id.rb1tlF).isChecked) {

                                                        jugador["tlF"] =
                                                            jugador["tlF"].toString().toInt() + 1
                                                        Toast.makeText(
                                                            binding.root.context,
                                                            "Tiro Libre del jugador " + lista[i].textOn + " fallado",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        val registro = hashMapOf(
                                                            "cuarto" to cuarto,
                                                            "dorsal" to lista[i].text,
                                                            "frase" to "TIRO LIBRE FALLADO",
                                                            "resultado" to "",
                                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                                            "equipo" to "Visitante",
                                                            "tipoFrase" to "1"
                                                        ) as Map<String?, Any?>
                                                        listRegistros.add(registro)

                                                    }

                                                    if (view.findViewById<RadioButton>(R.id.rb2tlA).isChecked) {

                                                        jugador["tlA"] =
                                                            jugador["tlA"].toString().toInt() + 1
                                                        jugador["puntos"] =
                                                            jugador["puntos"].toString().toInt() + 1
                                                        binding.txtPuntosVisitante.text =
                                                            (binding.txtPuntosVisitante.text.toString()
                                                                .toInt() + 1).toString()
                                                        db.collection("Partidos")
                                                            .document(idpartido.toString())
                                                            .update(
                                                                hashMapOf(
                                                                    "Resultado" to binding.txtPuntosLocal.text.toString() + ":" + binding.txtPuntosVisitante.text.toString()
                                                                ) as Map<String, Any>
                                                            )
                                                        Toast.makeText(
                                                            binding.root.context,
                                                            "Tiro Libre del jugador " + lista[i].textOn + " anotado",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        val registro = hashMapOf(
                                                            "cuarto" to cuarto,
                                                            "dorsal" to lista[i].text,
                                                            "frase" to "TIRO LIBRE ANOTADO",
                                                            "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                                            "equipo" to "Visitante",
                                                            "tipoFrase" to "1"
                                                        ) as Map<String?, Any?>
                                                        listRegistros.add(registro)

                                                    } else if (view.findViewById<RadioButton>(R.id.rb2tlF).isChecked) {

                                                        jugador["tlF"] =
                                                            jugador["tlF"].toString().toInt() + 1
                                                        Toast.makeText(
                                                            binding.root.context,
                                                            "Tiro Libre del jugador " + lista[i].textOn + " fallado",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        val registro = hashMapOf(
                                                            "cuarto" to cuarto,
                                                            "dorsal" to lista[i].text,
                                                            "frase" to "TIRO LIBRE FALLADO",
                                                            "resultado" to "",
                                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                                            "equipo" to "Visitante",
                                                            "tipoFrase" to "1"
                                                        ) as Map<String?, Any?>
                                                        listRegistros.add(registro)

                                                    }

                                                    if (view.findViewById<RadioButton>(R.id.rb3tlA).isChecked) {

                                                        jugador["tlA"] =
                                                            jugador["tlA"].toString().toInt() + 1
                                                        jugador["puntos"] =
                                                            jugador["puntos"].toString().toInt() + 1
                                                        binding.txtPuntosVisitante.text =
                                                            (binding.txtPuntosVisitante.text.toString()
                                                                .toInt() + 1).toString()
                                                        db.collection("Partidos")
                                                            .document(idpartido.toString())
                                                            .update(
                                                                hashMapOf(
                                                                    "Resultado" to binding.txtPuntosLocal.text.toString() + ":" + binding.txtPuntosVisitante.text.toString()
                                                                ) as Map<String, Any>
                                                            )
                                                        Toast.makeText(
                                                            binding.root.context,
                                                            "Tiro Libre del jugador " + lista[i].textOn + " anotado",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        val registro = hashMapOf(
                                                            "cuarto" to cuarto,
                                                            "dorsal" to lista[i].text,
                                                            "frase" to "TIRO LIBRE ANOTADO",
                                                            "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                                            "equipo" to "Visitante",
                                                            "tipoFrase" to "1"
                                                        ) as Map<String?, Any?>
                                                        listRegistros.add(registro)

                                                    } else if (view.findViewById<RadioButton>(R.id.rb3tlF).isChecked) {

                                                        jugador["tlF"] =
                                                            jugador["tlF"].toString().toInt() + 1
                                                        Toast.makeText(
                                                            binding.root.context,
                                                            "Tiro Libre del jugador " + lista[i].textOn + " fallado",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        val registro = hashMapOf(
                                                            "cuarto" to cuarto,
                                                            "dorsal" to lista[i].text,
                                                            "frase" to "TIRO LIBRE FALLADO",
                                                            "resultado" to "",
                                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                                            "equipo" to "Visitante",
                                                            "tipoFrase" to "1"
                                                        ) as Map<String?, Any?>
                                                        listRegistros.add(registro)

                                                    }

                                                    db.collection("Estadisticas")
                                                        .document(idpartido.toString())
                                                        .update(
                                                            hashMapOf(
                                                                listJugador[j] to jugador
                                                            ) as Map<String, Any>
                                                        ).addOnSuccessListener {
                                                            calcularVal(listJugador[j], jugador)
                                                        }
                                                    db.collection("MinutoaMinuto")
                                                        .document(idpartido.toString())
                                                        .update(
                                                            hashMapOf(
                                                                "registro" to listRegistros,
                                                            ) as Map<String?, Any?>
                                                        )
                                                }

                                        }
                                    }
                                }

                        }
                    }
                }
                dialog.hide()
                vaciarToggle(lista)
            }

        }
        binding.btnT2p.setOnClickListener {

            val builder = AlertDialog.Builder(binding.root.context)
            val view = layoutInflater.inflate(R.layout.accion_partido, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()

            view.findViewById<Button>(R.id.btnAnotar).setOnClickListener {

                val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                val idpartido = prefs.getString("idPartido", "")
                val lista = llenarListToggle()
                for (i in 0..lista.count() - 1) {
                    if (lista[i].isChecked) {
                        if (lista[i].id == R.id.TBLocal1 || lista[i].id == R.id.TBLocal2 || lista[i].id == R.id.TBLocal3 || lista[i].id == R.id.TBLocal4 || lista[i].id == R.id.TBLocal5) {
                            db.collection("Estadisticas").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listJugador =
                                        it.get("ListadoJugadores") as ArrayList<String>
                                    for (j in 0..listJugador.count() - 1) {
                                        val jugador =
                                            (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                        if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Local") {
                                            jugador["tc2pA"] =
                                                jugador["tc2pA"].toString().toInt() + 1
                                            jugador["puntos"] =
                                                jugador["puntos"].toString().toInt() + 2
                                            binding.txtPuntosLocal.text =
                                                (binding.txtPuntosLocal.text.toString()
                                                    .toInt() + 2).toString()

                                            db.collection("Partidos")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        "Resultado" to binding.txtPuntosLocal.text.toString() + ":" + binding.txtPuntosVisitante.text.toString()
                                                    ) as Map<String, Any>
                                                )

                                            db.collection("Estadisticas")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        listJugador[j] to jugador
                                                    ) as Map<String, Any>
                                                ).addOnSuccessListener {
                                                    Toast.makeText(
                                                        binding.root.context,
                                                        "Canasta del jugador " + lista[i].textOn + " de 2p",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    calcularVal(listJugador[j], jugador)
                                                    db.collection("MinutoaMinuto")
                                                        .document(idpartido.toString()).get()
                                                        .addOnSuccessListener { it2 ->
                                                            val listRegistros =
                                                                it2.get("registro") as ArrayList<Map<String?, Any?>>
                                                            val registro = hashMapOf(
                                                                "cuarto" to cuarto,
                                                                "dorsal" to lista[i].text,
                                                                "frase" to "CANASTA DE 2 PUNTOS",
                                                                "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                                                "tiempo" to binding.TiempoCuarto.text.toString(),
                                                                "equipo" to "Local",
                                                                "tipoFrase" to "1"
                                                            ) as Map<String?, Any?>
                                                            listRegistros.add(registro)
                                                            db.collection("MinutoaMinuto")
                                                                .document(idpartido.toString())
                                                                .update(
                                                                    hashMapOf(
                                                                        "registro" to listRegistros,
                                                                    ) as Map<String?, Any?>
                                                                )
                                                        }
                                                }
                                        }
                                    }

                                }


                        } else {
                            db.collection("Estadisticas").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listJugador =
                                        it.get("ListadoJugadores") as ArrayList<String>
                                    for (j in 0..listJugador.count() - 1) {
                                        val jugador =
                                            (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                        if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Visitante") {
                                            jugador["tc2pA"] =
                                                jugador["tc2pA"].toString().toInt() + 1
                                            jugador["puntos"] =
                                                jugador["puntos"].toString().toInt() + 2
                                            binding.txtPuntosVisitante.text =
                                                (binding.txtPuntosVisitante.text.toString()
                                                    .toInt() + 2).toString()

                                            db.collection("Partidos")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        "Resultado" to binding.txtPuntosLocal.text.toString() + ":" + binding.txtPuntosVisitante.text.toString()
                                                    ) as Map<String, Any>
                                                )

                                            db.collection("Estadisticas")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        listJugador[j] to jugador
                                                    ) as Map<String, Any>
                                                ).addOnSuccessListener {
                                                    Toast.makeText(
                                                        binding.root.context,
                                                        "Canasta del jugador " + lista[i].textOn + " de 2p",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    calcularVal(listJugador[j], jugador)
                                                    db.collection("MinutoaMinuto")
                                                        .document(idpartido.toString()).get()
                                                        .addOnSuccessListener { it2 ->
                                                            val listRegistros =
                                                                it2.get("registro") as ArrayList<Map<String?, Any?>>
                                                            val registro = hashMapOf(
                                                                "cuarto" to cuarto,
                                                                "dorsal" to lista[i].text,
                                                                "frase" to "CANASTA DE 2 PUNTOS",
                                                                "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                                                "tiempo" to binding.TiempoCuarto.text.toString(),
                                                                "equipo" to "Visitante",
                                                                "tipoFrase" to "1"
                                                            ) as Map<String?, Any?>
                                                            listRegistros.add(registro)
                                                            db.collection("MinutoaMinuto")
                                                                .document(idpartido.toString())
                                                                .update(
                                                                    hashMapOf(
                                                                        "registro" to listRegistros,
                                                                    ) as Map<String?, Any?>
                                                                )
                                                        }
                                                }
                                        }
                                    }

                                }

                        }
                    }
                }
                vaciarToggle(lista)
                dialog.hide()
            }

            view.findViewById<Button>(R.id.btnFallar).setOnClickListener {

                val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                val idpartido = prefs.getString("idPartido", "")
                val lista = llenarListToggle()
                for (i in 0..lista.count() - 1) {
                    if (lista[i].isChecked) {
                        if (lista[i].id == R.id.TBLocal1 || lista[i].id == R.id.TBLocal2 || lista[i].id == R.id.TBLocal3 || lista[i].id == R.id.TBLocal4 || lista[i].id == R.id.TBLocal5) {
                            db.collection("Estadisticas").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listJugador =
                                        it.get("ListadoJugadores") as ArrayList<String>
                                    for (j in 0..listJugador.count() - 1) {
                                        val jugador =
                                            (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                        if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Local") {
                                            jugador["tc2pF"] =
                                                jugador["tc2pF"].toString().toInt() + 1
                                            db.collection("Estadisticas")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        listJugador[j] to jugador
                                                    ) as Map<String, Any>
                                                ).addOnSuccessListener {
                                                    Toast.makeText(
                                                        binding.root.context,
                                                        "Canasta fallada del jugador " + lista[i].textOn + " de 2p",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    calcularVal(listJugador[j], jugador)
                                                    db.collection("MinutoaMinuto")
                                                        .document(idpartido.toString()).get()
                                                        .addOnSuccessListener { it2 ->
                                                            val listRegistros =
                                                                it2.get("registro") as ArrayList<Map<String?, Any?>>
                                                            val registro = hashMapOf(
                                                                "cuarto" to cuarto,
                                                                "dorsal" to lista[i].text,
                                                                "frase" to "CANASTA DE 2 PUNTOS FALLADA",
                                                                "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                                                "tiempo" to binding.TiempoCuarto.text.toString(),
                                                                "equipo" to "Local",
                                                                "tipoFrase" to "1"
                                                            ) as Map<String?, Any?>
                                                            listRegistros.add(registro)
                                                            db.collection("MinutoaMinuto")
                                                                .document(idpartido.toString())
                                                                .update(
                                                                    hashMapOf(
                                                                        "registro" to listRegistros,
                                                                    ) as Map<String?, Any?>
                                                                )
                                                        }
                                                }
                                        }
                                    }

                                }


                        } else {
                            db.collection("Estadisticas").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listJugador =
                                        it.get("ListadoJugadores") as ArrayList<String>
                                    for (j in 0..listJugador.count() - 1) {
                                        val jugador =
                                            (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                        if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Visitante") {
                                            jugador["tc2pF"] =
                                                jugador["tc2pF"].toString().toInt() + 1
                                            db.collection("Estadisticas")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        listJugador[j] to jugador
                                                    ) as Map<String, Any>
                                                ).addOnSuccessListener {
                                                    Toast.makeText(
                                                        binding.root.context,
                                                        "Canasta fallada del jugador " + lista[i].textOn + " de 2p",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    calcularVal(listJugador[j], jugador)
                                                    db.collection("MinutoaMinuto")
                                                        .document(idpartido.toString()).get()
                                                        .addOnSuccessListener { it2 ->
                                                            val listRegistros =
                                                                it2.get("registro") as ArrayList<Map<String?, Any?>>
                                                            val registro = hashMapOf(
                                                                "cuarto" to cuarto,
                                                                "dorsal" to lista[i].text,
                                                                "frase" to "CANASTA DE 2 PUNTOS FALLADA",
                                                                "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                                                "tiempo" to binding.TiempoCuarto.text.toString(),
                                                                "equipo" to "Visitante",
                                                                "tipoFrase" to "1"
                                                            ) as Map<String?, Any?>
                                                            listRegistros.add(registro)
                                                            db.collection("MinutoaMinuto")
                                                                .document(idpartido.toString())
                                                                .update(
                                                                    hashMapOf(
                                                                        "registro" to listRegistros,
                                                                    ) as Map<String?, Any?>
                                                                )
                                                        }
                                                }
                                        }
                                    }

                                }

                        }
                    }
                }
                vaciarToggle(lista)
                dialog.hide()
            }
        }
        binding.btnT3p.setOnClickListener {

            val builder = AlertDialog.Builder(binding.root.context)
            val view = layoutInflater.inflate(R.layout.accion_partido, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()

            view.findViewById<Button>(R.id.btnAnotar).setOnClickListener {

                val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                val idpartido = prefs.getString("idPartido", "")
                val lista = llenarListToggle()
                for (i in 0..lista.count() - 1) {
                    if (lista[i].isChecked) {
                        if (lista[i].id == R.id.TBLocal1 || lista[i].id == R.id.TBLocal2 || lista[i].id == R.id.TBLocal3 || lista[i].id == R.id.TBLocal4 || lista[i].id == R.id.TBLocal5) {
                            db.collection("Estadisticas").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listJugador =
                                        it.get("ListadoJugadores") as ArrayList<String>
                                    for (j in 0..listJugador.count() - 1) {
                                        val jugador =
                                            (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                        if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Local") {
                                            jugador["tc3pA"] =
                                                jugador["tc3pA"].toString().toInt() + 1
                                            jugador["puntos"] =
                                                jugador["puntos"].toString().toInt() + 3
                                            binding.txtPuntosLocal.text =
                                                (binding.txtPuntosLocal.text.toString()
                                                    .toInt() + 3).toString()

                                            db.collection("Partidos")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        "Resultado" to binding.txtPuntosLocal.text.toString() + ":" + binding.txtPuntosVisitante.text.toString()
                                                    ) as Map<String, Any>
                                                )

                                            db.collection("Estadisticas")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        listJugador[j] to jugador
                                                    ) as Map<String, Any>
                                                ).addOnSuccessListener {
                                                    Toast.makeText(
                                                        binding.root.context,
                                                        "Canasta del jugador " + lista[i].textOn + " de 3p",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    calcularVal(listJugador[j], jugador)
                                                    db.collection("MinutoaMinuto")
                                                        .document(idpartido.toString()).get()
                                                        .addOnSuccessListener { it2 ->
                                                            val listRegistros =
                                                                it2.get("registro") as ArrayList<Map<String?, Any?>>
                                                            val registro = hashMapOf(
                                                                "cuarto" to cuarto,
                                                                "dorsal" to lista[i].text,
                                                                "frase" to "TRIPLE",
                                                                "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                                                "tiempo" to binding.TiempoCuarto.text.toString(),
                                                                "equipo" to "Local",
                                                                "tipoFrase" to "1"
                                                            ) as Map<String?, Any?>
                                                            listRegistros.add(registro)
                                                            db.collection("MinutoaMinuto")
                                                                .document(idpartido.toString())
                                                                .update(
                                                                    hashMapOf(
                                                                        "registro" to listRegistros,
                                                                    ) as Map<String?, Any?>
                                                                )
                                                        }
                                                }
                                        }
                                    }

                                }


                        } else {
                            db.collection("Estadisticas").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listJugador =
                                        it.get("ListadoJugadores") as ArrayList<String>
                                    for (j in 0..listJugador.count() - 1) {
                                        val jugador =
                                            (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                        if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Visitante") {
                                            jugador["tc3pA"] =
                                                jugador["tc3pA"].toString().toInt() + 1
                                            jugador["puntos"] =
                                                jugador["puntos"].toString().toInt() + 3
                                            binding.txtPuntosVisitante.text =
                                                (binding.txtPuntosVisitante.text.toString()
                                                    .toInt() + 3).toString()

                                            db.collection("Partidos")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        "Resultado" to binding.txtPuntosLocal.text.toString() + ":" + binding.txtPuntosVisitante.text.toString()
                                                    ) as Map<String, Any>
                                                )

                                            db.collection("Estadisticas")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        listJugador[j] to jugador
                                                    ) as Map<String, Any>
                                                ).addOnSuccessListener {
                                                    Toast.makeText(
                                                        binding.root.context,
                                                        "Canasta del jugador " + lista[i].textOn + " de 3p",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    calcularVal(listJugador[j], jugador)
                                                    db.collection("MinutoaMinuto")
                                                        .document(idpartido.toString()).get()
                                                        .addOnSuccessListener { it2 ->
                                                            val listRegistros =
                                                                it2.get("registro") as ArrayList<Map<String?, Any?>>
                                                            val registro = hashMapOf(
                                                                "cuarto" to cuarto,
                                                                "dorsal" to lista[i].text,
                                                                "frase" to "TRIPLE",
                                                                "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                                                "tiempo" to binding.TiempoCuarto.text.toString(),
                                                                "equipo" to "Visitante",
                                                                "tipoFrase" to "1"
                                                            ) as Map<String?, Any?>
                                                            listRegistros.add(registro)
                                                            db.collection("MinutoaMinuto")
                                                                .document(idpartido.toString())
                                                                .update(
                                                                    hashMapOf(
                                                                        "registro" to listRegistros,
                                                                    ) as Map<String?, Any?>
                                                                )
                                                        }
                                                }
                                        }
                                    }

                                }

                        }
                    }
                }
                vaciarToggle(lista)
                dialog.hide()
            }

            view.findViewById<Button>(R.id.btnFallar).setOnClickListener {

                val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                val idpartido = prefs.getString("idPartido", "")
                val lista = llenarListToggle()
                for (i in 0..lista.count() - 1) {
                    if (lista[i].isChecked) {
                        if (lista[i].id == R.id.TBLocal1 || lista[i].id == R.id.TBLocal2 || lista[i].id == R.id.TBLocal3 || lista[i].id == R.id.TBLocal4 || lista[i].id == R.id.TBLocal5) {
                            db.collection("Estadisticas").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listJugador =
                                        it.get("ListadoJugadores") as ArrayList<String>
                                    for (j in 0..listJugador.count() - 1) {
                                        val jugador =
                                            (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                        if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Local") {
                                            jugador["tc3pF"] =
                                                jugador["tc3pF"].toString().toInt() + 1
                                            db.collection("Estadisticas")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        listJugador[j] to jugador
                                                    ) as Map<String, Any>
                                                ).addOnSuccessListener {
                                                    Toast.makeText(
                                                        binding.root.context,
                                                        "Canasta fallada del jugador " + lista[i].textOn + " de 3p",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    calcularVal(listJugador[j], jugador)
                                                }
                                        }
                                    }

                                }

                            db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listRegistros =
                                        it.get("registro") as ArrayList<Map<String?, Any?>>
                                    val registro = hashMapOf(
                                        "cuarto" to cuarto,
                                        "dorsal" to lista[i].text,
                                        "frase" to "TRIPLE FALLADO",
                                        "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                        "tiempo" to binding.TiempoCuarto.text.toString(),
                                        "equipo" to "Local",
                                        "tipoFrase" to "1"
                                    ) as Map<String?, Any?>
                                    listRegistros.add(registro)
                                    db.collection("MinutoaMinuto")
                                        .document(idpartido.toString())
                                        .update(
                                            hashMapOf(
                                                "registro" to listRegistros,
                                            ) as Map<String?, Any?>
                                        )
                                }
                        } else {
                            db.collection("Estadisticas").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listJugador =
                                        it.get("ListadoJugadores") as ArrayList<String>
                                    for (j in 0..listJugador.count() - 1) {
                                        val jugador =
                                            (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                        if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Visitante") {
                                            jugador["tc3pF"] =
                                                jugador["tc3pF"].toString().toInt() + 1
                                            db.collection("Estadisticas")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        listJugador[j] to jugador
                                                    ) as Map<String, Any>
                                                ).addOnSuccessListener {
                                                    Toast.makeText(
                                                        binding.root.context,
                                                        "Canasta fallada del jugador " + lista[i].textOn + " de 3p",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    calcularVal(listJugador[j], jugador)
                                                }
                                        }
                                    }

                                }
                            db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listRegistros =
                                        it.get("registro") as ArrayList<Map<String?, Any?>>
                                    val registro = hashMapOf(
                                        "cuarto" to cuarto,
                                        "dorsal" to lista[i].text,
                                        "frase" to "TRIPLE FALLADO",
                                        "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                        "tiempo" to binding.TiempoCuarto.text.toString(),
                                        "equipo" to "Visitante",
                                        "tipoFrase" to "1"
                                    ) as Map<String?, Any?>
                                    listRegistros.add(registro)
                                    db.collection("MinutoaMinuto")
                                        .document(idpartido.toString())
                                        .update(
                                            hashMapOf(
                                                "registro" to listRegistros,
                                            ) as Map<String?, Any?>
                                        )
                                }
                        }
                    }
                }
                vaciarToggle(lista)
                dialog.hide()
            }
        }
        binding.btnPerdida.setOnClickListener {

            val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
            val idpartido = prefs.getString("idPartido", "")
            val lista = llenarListToggle()
            for (i in 0..lista.count() - 1) {
                if (lista[i].isChecked) {
                    if (lista[i].id == R.id.TBLocal1 || lista[i].id == R.id.TBLocal2 || lista[i].id == R.id.TBLocal3 || lista[i].id == R.id.TBLocal4 || lista[i].id == R.id.TBLocal5) {
                        db.collection("Estadisticas").document(idpartido.toString()).get()
                            .addOnSuccessListener {
                                val listJugador =
                                    it.get("ListadoJugadores") as ArrayList<String>
                                for (j in 0..listJugador.count() - 1) {
                                    val jugador =
                                        (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                    if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Local") {
                                        jugador["per"] = jugador["per"].toString().toInt() + 1
                                        db.collection("Estadisticas")
                                            .document(idpartido.toString())
                                            .update(
                                                hashMapOf(
                                                    listJugador[j] to jugador
                                                ) as Map<String, Any>
                                            ).addOnSuccessListener {
                                                Toast.makeText(
                                                    binding.root.context,
                                                    "Pérdida del jugador " + lista[i].textOn,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                calcularVal(listJugador[j], jugador)
                                            }
                                    }
                                }

                            }

                        db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                            .addOnSuccessListener {
                                val listRegistros =
                                    it.get("registro") as ArrayList<Map<String?, Any?>>
                                val registro = hashMapOf(
                                    "cuarto" to cuarto,
                                    "dorsal" to lista[i].text,
                                    "frase" to "PÉRDIDA",
                                    "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                    "tiempo" to binding.TiempoCuarto.text.toString(),
                                    "equipo" to "Local",
                                    "tipoFrase" to "1"
                                ) as Map<String?, Any?>
                                listRegistros.add(registro)
                                db.collection("MinutoaMinuto").document(idpartido.toString())
                                    .update(
                                        hashMapOf(
                                            "registro" to listRegistros,
                                        ) as Map<String?, Any?>
                                    )
                            }
                    } else {
                        db.collection("Estadisticas").document(idpartido.toString()).get()
                            .addOnSuccessListener {
                                val listJugador =
                                    it.get("ListadoJugadores") as ArrayList<String>
                                for (j in 0..listJugador.count() - 1) {
                                    val jugador =
                                        (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                    if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Visitante") {
                                        jugador["per"] = jugador["per"].toString().toInt() + 1
                                        db.collection("Estadisticas")
                                            .document(idpartido.toString())
                                            .update(
                                                hashMapOf(
                                                    listJugador[j] to jugador
                                                ) as Map<String, Any>
                                            ).addOnSuccessListener {
                                                Toast.makeText(
                                                    binding.root.context,
                                                    "Pérdida del jugador " + lista[i].textOn,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                calcularVal(listJugador[j], jugador)
                                            }
                                    }
                                }

                            }
                        db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                            .addOnSuccessListener {
                                val listRegistros =
                                    it.get("registro") as ArrayList<Map<String?, Any?>>
                                val registro = hashMapOf(
                                    "cuarto" to cuarto,
                                    "dorsal" to lista[i].text,
                                    "frase" to "PÉRDIDA",
                                    "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                    "tiempo" to binding.TiempoCuarto.text.toString(),
                                    "equipo" to "Visitante",
                                    "tipoFrase" to "1"
                                ) as Map<String?, Any?>
                                listRegistros.add(registro)
                                db.collection("MinutoaMinuto").document(idpartido.toString())
                                    .update(
                                        hashMapOf(
                                            "registro" to listRegistros,
                                        ) as Map<String?, Any?>
                                    )
                            }
                    }
                }
            }
            vaciarToggle(lista)
        }
        binding.btnRecuperacion.setOnClickListener {

            val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
            val idpartido = prefs.getString("idPartido", "")
            val lista = llenarListToggle()
            for (i in 0..lista.count() - 1) {
                if (lista[i].isChecked) {
                    if (lista[i].id == R.id.TBLocal1 || lista[i].id == R.id.TBLocal2 || lista[i].id == R.id.TBLocal3 || lista[i].id == R.id.TBLocal4 || lista[i].id == R.id.TBLocal5) {
                        db.collection("Estadisticas").document(idpartido.toString()).get()
                            .addOnSuccessListener {
                                val listJugador =
                                    it.get("ListadoJugadores") as ArrayList<String>
                                for (j in 0..listJugador.count() - 1) {
                                    val jugador =
                                        (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                    if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Local") {
                                        jugador["recu"] = jugador["recu"].toString().toInt() + 1
                                        db.collection("Estadisticas")
                                            .document(idpartido.toString())
                                            .update(
                                                hashMapOf(
                                                    listJugador[j] to jugador
                                                ) as Map<String, Any>
                                            ).addOnSuccessListener {
                                                Toast.makeText(
                                                    binding.root.context,
                                                    "Recuperación del jugador " + lista[i].textOn,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                calcularVal(listJugador[j], jugador)
                                            }
                                    }
                                }

                            }
                        db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                            .addOnSuccessListener {
                                val listRegistros =
                                    it.get("registro") as ArrayList<Map<String?, Any?>>
                                val registro = hashMapOf(
                                    "cuarto" to cuarto,
                                    "dorsal" to lista[i].text,
                                    "frase" to "RECUPERACIÓN",
                                    "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                    "tiempo" to binding.TiempoCuarto.text.toString(),
                                    "equipo" to "Local",
                                    "tipoFrase" to "1"
                                ) as Map<String?, Any?>
                                listRegistros.add(registro)
                                db.collection("MinutoaMinuto").document(idpartido.toString())
                                    .update(
                                        hashMapOf(
                                            "registro" to listRegistros,
                                        ) as Map<String?, Any?>
                                    )
                            }
                    } else {
                        db.collection("Estadisticas").document(idpartido.toString()).get()
                            .addOnSuccessListener {
                                val listJugador =
                                    it.get("ListadoJugadores") as ArrayList<String>
                                for (j in 0..listJugador.count() - 1) {
                                    val jugador =
                                        (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                    if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Visitante") {
                                        jugador["recu"] = jugador["recu"].toString().toInt() + 1
                                        db.collection("Estadisticas")
                                            .document(idpartido.toString())
                                            .update(
                                                hashMapOf(
                                                    listJugador[j] to jugador
                                                ) as Map<String, Any>
                                            ).addOnSuccessListener {
                                                Toast.makeText(
                                                    binding.root.context,
                                                    "Recuperación del jugador " + lista[i].textOn,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                calcularVal(listJugador[j], jugador)
                                            }
                                    }
                                }

                            }
                        db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                            .addOnSuccessListener {
                                val listRegistros =
                                    it.get("registro") as ArrayList<Map<String?, Any?>>
                                val registro = hashMapOf(
                                    "cuarto" to cuarto,
                                    "dorsal" to lista[i].text,
                                    "frase" to "RECUPERACIÓN",
                                    "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                    "tiempo" to binding.TiempoCuarto.text.toString(),
                                    "equipo" to "Visitante",
                                    "tipoFrase" to "1"
                                ) as Map<String?, Any?>
                                listRegistros.add(registro)
                                db.collection("MinutoaMinuto").document(idpartido.toString())
                                    .update(
                                        hashMapOf(
                                            "registro" to listRegistros,
                                        ) as Map<String?, Any?>
                                    )
                            }
                    }
                }
            }
            vaciarToggle(lista)
        }
        binding.btnAsistencia.setOnClickListener {

            val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
            val idpartido = prefs.getString("idPartido", "")
            val lista = llenarListToggle()
            for (i in 0..lista.count() - 1) {
                if (lista[i].isChecked) {
                    if (lista[i].id == R.id.TBLocal1 || lista[i].id == R.id.TBLocal2 || lista[i].id == R.id.TBLocal3 || lista[i].id == R.id.TBLocal4 || lista[i].id == R.id.TBLocal5) {
                        db.collection("Estadisticas").document(idpartido.toString()).get()
                            .addOnSuccessListener {
                                val listJugador =
                                    it.get("ListadoJugadores") as ArrayList<String>
                                for (j in 0..listJugador.count() - 1) {
                                    val jugador =
                                        (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                    if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Local") {
                                        jugador["asi"] = jugador["asi"].toString().toInt() + 1
                                        db.collection("Estadisticas")
                                            .document(idpartido.toString())
                                            .update(
                                                hashMapOf(
                                                    listJugador[j] to jugador
                                                ) as Map<String, Any>
                                            ).addOnSuccessListener {
                                                Toast.makeText(
                                                    binding.root.context,
                                                    "Asistencia del jugador " + lista[i].textOn,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                calcularVal(listJugador[j], jugador)
                                            }
                                    }
                                }

                            }
                        db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                            .addOnSuccessListener {
                                val listRegistros =
                                    it.get("registro") as ArrayList<Map<String?, Any?>>
                                val registro = hashMapOf(
                                    "cuarto" to cuarto,
                                    "dorsal" to lista[i].text,
                                    "frase" to "ASISTENCIA",
                                    "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                    "tiempo" to binding.TiempoCuarto.text.toString(),
                                    "equipo" to "Local",
                                    "tipoFrase" to "1"
                                ) as Map<String?, Any?>
                                listRegistros.add(registro)
                                db.collection("MinutoaMinuto").document(idpartido.toString())
                                    .update(
                                        hashMapOf(
                                            "registro" to listRegistros,
                                        ) as Map<String?, Any?>
                                    )
                            }
                    } else {
                        db.collection("Estadisticas").document(idpartido.toString()).get()
                            .addOnSuccessListener {
                                val listJugador =
                                    it.get("ListadoJugadores") as ArrayList<String>
                                for (j in 0..listJugador.count() - 1) {
                                    val jugador =
                                        (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                    if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Visitante") {
                                        jugador["asi"] = jugador["asi"].toString().toInt() + 1
                                        db.collection("Estadisticas")
                                            .document(idpartido.toString())
                                            .update(
                                                hashMapOf(
                                                    listJugador[j] to jugador
                                                ) as Map<String, Any>
                                            ).addOnSuccessListener {
                                                Toast.makeText(
                                                    binding.root.context,
                                                    "Asistencia del jugador " + lista[i].textOn,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                calcularVal(listJugador[j], jugador)
                                            }
                                    }
                                }

                            }
                        db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                            .addOnSuccessListener {
                                val listRegistros =
                                    it.get("registro") as ArrayList<Map<String?, Any?>>
                                val registro = hashMapOf(
                                    "cuarto" to cuarto,
                                    "dorsal" to lista[i].text,
                                    "frase" to "ASISTENCIA",
                                    "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                    "tiempo" to binding.TiempoCuarto.text.toString(),
                                    "equipo" to "Visitante",
                                    "tipoFrase" to "1"
                                ) as Map<String?, Any?>
                                listRegistros.add(registro)
                                db.collection("MinutoaMinuto").document(idpartido.toString())
                                    .update(
                                        hashMapOf(
                                            "registro" to listRegistros,
                                        ) as Map<String?, Any?>
                                    )
                            }
                    }
                }
            }
            vaciarToggle(lista)
        }
        binding.btnFalta.setOnClickListener {

            val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
            val idpartido = prefs.getString("idPartido", "")
            val lista = llenarListToggle()

            val faltasEquipoL = binding.txtFaltasLocal.text.split(":")
            val faltaL = faltasEquipoL[1].trim()
            val faltasEquipoV = binding.txtFaltasVisitante.text.split(":")
            val faltaV = faltasEquipoV[1].trim()
            for (i in 0..lista.count() - 1) {
                if (lista[i].isChecked) {
                    if (lista[i].id == R.id.TBLocal1 || lista[i].id == R.id.TBLocal2 || lista[i].id == R.id.TBLocal3 || lista[i].id == R.id.TBLocal4 || lista[i].id == R.id.TBLocal5) {
                        db.collection("Estadisticas").document(idpartido.toString()).get()
                            .addOnSuccessListener {
                                val listJugador =
                                    it.get("ListadoJugadores") as ArrayList<String>
                                for (j in 0..listJugador.count() - 1) {
                                    val jugador =
                                        (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                    if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Local") {
                                        jugador["fal"] = jugador["fal"].toString().toInt() + 1
                                        binding.txtFaltasLocal.text = "Faltas: " +
                                                (faltaL.toInt() + 1).toString()
                                        db.collection("Estadisticas")
                                            .document(idpartido.toString())
                                            .update(
                                                hashMapOf(
                                                    listJugador[j] to jugador
                                                ) as Map<String, Any>
                                            ).addOnSuccessListener {
                                                Toast.makeText(
                                                    binding.root.context,
                                                    "Falta del jugador " + lista[i].textOn + ", lleva " + jugador["fal"] + " faltas.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                calcularVal(listJugador[j], jugador)

                                                db.collection("MinutoaMinuto")
                                                    .document(idpartido.toString()).get()
                                                    .addOnSuccessListener {
                                                        val listRegistros =
                                                            it.get("registro") as ArrayList<Map<String?, Any?>>
                                                        val registro = hashMapOf(
                                                            "cuarto" to cuarto,
                                                            "dorsal" to lista[i].text,
                                                            "frase" to "FALTA COMETIDA",
                                                            "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                                            "equipo" to "Local",
                                                            "tipoFrase" to "1"
                                                        ) as Map<String?, Any?>
                                                        listRegistros.add(registro)
                                                        db.collection("MinutoaMinuto")
                                                            .document(idpartido.toString())
                                                            .update(
                                                                hashMapOf(
                                                                    "registro" to listRegistros,
                                                                ) as Map<String?, Any?>
                                                            ).addOnSuccessListener {
                                                                if (jugador["fal"].toString() == "5")
                                                                    Faltas5Local(
                                                                        llenarListToggleLocal(),
                                                                        lista[i]
                                                                    )
                                                            }
                                                    }
                                            }
                                    }
                                }

                            }

                    } else {
                        db.collection("Estadisticas").document(idpartido.toString()).get()
                            .addOnSuccessListener {
                                val listJugador =
                                    it.get("ListadoJugadores") as ArrayList<String>
                                for (j in 0..listJugador.count() - 1) {
                                    val jugador =
                                        (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                    if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Visitante") {
                                        jugador["fal"] = jugador["fal"].toString().toInt() + 1
                                        binding.txtFaltasVisitante.text = "Faltas: " +
                                                (faltaV.toInt() + 1).toString()
                                        db.collection("Estadisticas")
                                            .document(idpartido.toString())
                                            .update(
                                                hashMapOf(
                                                    listJugador[j] to jugador
                                                ) as Map<String, Any>
                                            ).addOnSuccessListener {
                                                Toast.makeText(
                                                    binding.root.context,
                                                    "Falta del jugador " + lista[i].textOn + ", lleva " + jugador["fal"] + " faltas.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                calcularVal(listJugador[j], jugador)

                                                db.collection("MinutoaMinuto")
                                                    .document(idpartido.toString()).get()
                                                    .addOnSuccessListener {
                                                        val listRegistros =
                                                            it.get("registro") as ArrayList<Map<String?, Any?>>
                                                        val registro = hashMapOf(
                                                            "cuarto" to cuarto,
                                                            "dorsal" to lista[i].text,
                                                            "frase" to "FALTADA COMETIDA",
                                                            "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                                            "equipo" to "Visitante",
                                                            "tipoFrase" to "1"
                                                        ) as Map<String?, Any?>
                                                        listRegistros.add(registro)
                                                        db.collection("MinutoaMinuto")
                                                            .document(idpartido.toString())
                                                            .update(
                                                                hashMapOf(
                                                                    "registro" to listRegistros,
                                                                ) as Map<String?, Any?>
                                                            ).addOnSuccessListener {
                                                                if (jugador["fal"].toString() == "5")
                                                                    Faltas5Visitante(
                                                                        llenarListToggleVisitante(),
                                                                        lista[i]
                                                                    )

                                                            }
                                                    }
                                            }
                                    }
                                }

                            }
                    }
                }
            }
            vaciarToggle(lista)
        }
        binding.btnTapon.setOnClickListener {

            val builder = AlertDialog.Builder(binding.root.context)
            val view = layoutInflater.inflate(R.layout.accion_partido, null)
            builder.setView(view)
            view.findViewById<Button>(R.id.btnAnotar).text = "Recibido"
            view.findViewById<Button>(R.id.btnFallar).text = "Cometido"
            val dialog = builder.create()
            dialog.show()

            view.findViewById<Button>(R.id.btnAnotar).setOnClickListener {

                val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                val idpartido = prefs.getString("idPartido", "")
                val lista = llenarListToggle()
                for (i in 0..lista.count() - 1) {
                    if (lista[i].isChecked) {
                        if (lista[i].id == R.id.TBLocal1 || lista[i].id == R.id.TBLocal2 || lista[i].id == R.id.TBLocal3 || lista[i].id == R.id.TBLocal4 || lista[i].id == R.id.TBLocal5) {
                            db.collection("Estadisticas").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listJugador =
                                        it.get("ListadoJugadores") as ArrayList<String>
                                    for (j in 0..listJugador.count() - 1) {
                                        val jugador =
                                            (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                        if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Local") {
                                            jugador["taRec"] =
                                                jugador["taRec"].toString().toInt() + 1
                                            db.collection("Estadisticas")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        listJugador[j] to jugador
                                                    ) as Map<String, Any>
                                                ).addOnSuccessListener {
                                                    Toast.makeText(
                                                        binding.root.context,
                                                        "Tapón Recibido del jugador " + lista[i].textOn,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    calcularVal(listJugador[j], jugador)
                                                }
                                        }
                                    }

                                }
                            db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listRegistros =
                                        it.get("registro") as ArrayList<Map<String?, Any?>>
                                    val registro = hashMapOf(
                                        "cuarto" to cuarto,
                                        "dorsal" to lista[i].text,
                                        "frase" to "TAPÓN RECIBIDO",
                                        "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                        "tiempo" to binding.TiempoCuarto.text.toString(),
                                        "equipo" to "Local",
                                        "tipoFrase" to "1"
                                    ) as Map<String?, Any?>
                                    listRegistros.add(registro)
                                    db.collection("MinutoaMinuto")
                                        .document(idpartido.toString())
                                        .update(
                                            hashMapOf(
                                                "registro" to listRegistros,
                                            ) as Map<String?, Any?>
                                        )
                                }
                        } else {
                            db.collection("Estadisticas").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listJugador =
                                        it.get("ListadoJugadores") as ArrayList<String>
                                    for (j in 0..listJugador.count() - 1) {
                                        val jugador =
                                            (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                        if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Visitante") {
                                            jugador["taRec"] =
                                                jugador["taRec"].toString().toInt() + 1
                                            db.collection("Estadisticas")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        listJugador[j] to jugador
                                                    ) as Map<String, Any>
                                                ).addOnSuccessListener {
                                                    Toast.makeText(
                                                        binding.root.context,
                                                        "Tapón Recibido del jugador " + lista[i].textOn,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    calcularVal(listJugador[j], jugador)
                                                }
                                        }
                                    }

                                }
                            db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listRegistros =
                                        it.get("registro") as ArrayList<Map<String?, Any?>>
                                    val registro = hashMapOf(
                                        "cuarto" to cuarto,
                                        "dorsal" to lista[i].text,
                                        "frase" to "TAPÓN RECIBIDO",
                                        "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                        "tiempo" to binding.TiempoCuarto.text.toString(),
                                        "equipo" to "Visitante",
                                        "tipoFrase" to "1"
                                    ) as Map<String?, Any?>
                                    listRegistros.add(registro)
                                    db.collection("MinutoaMinuto")
                                        .document(idpartido.toString())
                                        .update(
                                            hashMapOf(
                                                "registro" to listRegistros,
                                            ) as Map<String?, Any?>
                                        )
                                }
                        }
                    }
                }
                vaciarToggle(lista)
                dialog.hide()
            }

            view.findViewById<Button>(R.id.btnFallar).setOnClickListener {

                val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                val idpartido = prefs.getString("idPartido", "")
                val lista = llenarListToggle()
                for (i in 0..lista.count() - 1) {
                    if (lista[i].isChecked) {
                        if (lista[i].id == R.id.TBLocal1 || lista[i].id == R.id.TBLocal2 || lista[i].id == R.id.TBLocal3 || lista[i].id == R.id.TBLocal4 || lista[i].id == R.id.TBLocal5) {
                            db.collection("Estadisticas").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listJugador =
                                        it.get("ListadoJugadores") as ArrayList<String>
                                    for (j in 0..listJugador.count() - 1) {
                                        val jugador =
                                            (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                        if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Local") {
                                            jugador["taCom"] =
                                                jugador["taCom"].toString().toInt() + 1
                                            db.collection("Estadisticas")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        listJugador[j] to jugador
                                                    ) as Map<String, Any>
                                                ).addOnSuccessListener {
                                                    Toast.makeText(
                                                        binding.root.context,
                                                        "Tapón Cometido del jugador " + lista[i].textOn,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    calcularVal(listJugador[j], jugador)
                                                }
                                        }
                                    }

                                }
                            db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listRegistros =
                                        it.get("registro") as ArrayList<Map<String?, Any?>>
                                    val registro = hashMapOf(
                                        "cuarto" to cuarto,
                                        "dorsal" to lista[i].text,
                                        "frase" to "TAPÓN COMETIDO",
                                        "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                        "tiempo" to binding.TiempoCuarto.text.toString(),
                                        "equipo" to "Local",
                                        "tipoFrase" to "1"
                                    ) as Map<String?, Any?>
                                    listRegistros.add(registro)
                                    db.collection("MinutoaMinuto")
                                        .document(idpartido.toString())
                                        .update(
                                            hashMapOf(
                                                "registro" to listRegistros,
                                            ) as Map<String?, Any?>
                                        )
                                }
                        } else {
                            db.collection("Estadisticas").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listJugador =
                                        it.get("ListadoJugadores") as ArrayList<String>
                                    for (j in 0..listJugador.count() - 1) {
                                        val jugador =
                                            (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                        if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Visitante") {
                                            jugador["taCom"] =
                                                jugador["taCom"].toString().toInt() + 1
                                            db.collection("Estadisticas")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        listJugador[j] to jugador
                                                    ) as Map<String, Any>
                                                ).addOnSuccessListener {
                                                    Toast.makeText(
                                                        binding.root.context,
                                                        "Tapón Cometido del jugador " + lista[i].textOn,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    calcularVal(listJugador[j], jugador)
                                                }
                                        }
                                    }

                                }
                            db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listRegistros =
                                        it.get("registro") as ArrayList<Map<String?, Any?>>
                                    val registro = hashMapOf(
                                        "cuarto" to cuarto,
                                        "dorsal" to lista[i].text,
                                        "frase" to "TAPÓN COMETIDO",
                                        "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                        "tiempo" to binding.TiempoCuarto.text.toString(),
                                        "equipo" to "Visitante",
                                        "tipoFrase" to "1"
                                    ) as Map<String?, Any?>
                                    listRegistros.add(registro)
                                    db.collection("MinutoaMinuto")
                                        .document(idpartido.toString())
                                        .update(
                                            hashMapOf(
                                                "registro" to listRegistros,
                                            ) as Map<String?, Any?>
                                        )
                                }
                        }
                    }
                }
                vaciarToggle(lista)
                dialog.hide()
            }
        }
        binding.btnRebote.setOnClickListener {

            val builder = AlertDialog.Builder(binding.root.context)
            val view = layoutInflater.inflate(R.layout.accion_partido, null)
            builder.setView(view)
            view.findViewById<Button>(R.id.btnAnotar).text = "Ofensivo"
            view.findViewById<Button>(R.id.btnFallar).text = "Defensivo"
            val dialog = builder.create()
            dialog.show()

            view.findViewById<Button>(R.id.btnAnotar).setOnClickListener {

                val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                val idpartido = prefs.getString("idPartido", "")
                val lista = llenarListToggle()
                for (i in 0..lista.count() - 1) {
                    if (lista[i].isChecked) {
                        if (lista[i].id == R.id.TBLocal1 || lista[i].id == R.id.TBLocal2 || lista[i].id == R.id.TBLocal3 || lista[i].id == R.id.TBLocal4 || lista[i].id == R.id.TBLocal5) {
                            db.collection("Estadisticas").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listJugador =
                                        it.get("ListadoJugadores") as ArrayList<String>
                                    for (j in 0..listJugador.count() - 1) {
                                        val jugador =
                                            (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                        if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Local") {
                                            jugador["rebO"] =
                                                jugador["rebO"].toString().toInt() + 1
                                            db.collection("Estadisticas")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        listJugador[j] to jugador
                                                    ) as Map<String, Any>
                                                ).addOnSuccessListener {
                                                    Toast.makeText(
                                                        binding.root.context,
                                                        "Rebote Ofensivo del jugador " + lista[i].textOn,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    calcularVal(listJugador[j], jugador)
                                                }
                                        }
                                    }

                                }
                            db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listRegistros =
                                        it.get("registro") as ArrayList<Map<String?, Any?>>
                                    val registro = hashMapOf(
                                        "cuarto" to cuarto,
                                        "dorsal" to lista[i].text,
                                        "frase" to "REBOTE OFENSIVO",
                                        "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                        "tiempo" to binding.TiempoCuarto.text.toString(),
                                        "equipo" to "Local",
                                        "tipoFrase" to "1"
                                    ) as Map<String?, Any?>
                                    listRegistros.add(registro)
                                    db.collection("MinutoaMinuto")
                                        .document(idpartido.toString())
                                        .update(
                                            hashMapOf(
                                                "registro" to listRegistros,
                                            ) as Map<String?, Any?>
                                        )
                                }
                        } else {
                            db.collection("Estadisticas").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listJugador =
                                        it.get("ListadoJugadores") as ArrayList<String>
                                    for (j in 0..listJugador.count() - 1) {
                                        val jugador =
                                            (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                        if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Visitante") {
                                            jugador["rebO"] =
                                                jugador["rebO"].toString().toInt() + 1
                                            db.collection("Estadisticas")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        listJugador[j] to jugador
                                                    ) as Map<String, Any>
                                                ).addOnSuccessListener {
                                                    Toast.makeText(
                                                        binding.root.context,
                                                        "Rebote Ofensivo del jugador " + lista[i].textOn,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    calcularVal(listJugador[j], jugador)
                                                }
                                        }
                                    }

                                }
                            db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listRegistros =
                                        it.get("registro") as ArrayList<Map<String?, Any?>>
                                    val registro = hashMapOf(
                                        "cuarto" to cuarto,
                                        "dorsal" to lista[i].text,
                                        "frase" to "REBOTE OFENSIVO",
                                        "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                        "tiempo" to binding.TiempoCuarto.text.toString(),
                                        "equipo" to "Visitante",
                                        "tipoFrase" to "1"
                                    ) as Map<String?, Any?>
                                    listRegistros.add(registro)
                                    db.collection("MinutoaMinuto")
                                        .document(idpartido.toString())
                                        .update(
                                            hashMapOf(
                                                "registro" to listRegistros,
                                            ) as Map<String?, Any?>
                                        )
                                }
                        }
                    }
                }
                vaciarToggle(lista)
                dialog.hide()
            }

            view.findViewById<Button>(R.id.btnFallar).setOnClickListener {

                val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                val idpartido = prefs.getString("idPartido", "")
                val lista = llenarListToggle()
                for (i in 0..lista.count() - 1) {
                    if (lista[i].isChecked) {
                        if (lista[i].id == R.id.TBLocal1 || lista[i].id == R.id.TBLocal2 || lista[i].id == R.id.TBLocal3 || lista[i].id == R.id.TBLocal4 || lista[i].id == R.id.TBLocal5) {
                            db.collection("Estadisticas").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listJugador =
                                        it.get("ListadoJugadores") as ArrayList<String>
                                    for (j in 0..listJugador.count() - 1) {
                                        val jugador =
                                            (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                        if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Local") {
                                            jugador["rebD"] =
                                                jugador["rebD"].toString().toInt() + 1
                                            db.collection("Estadisticas")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        listJugador[j] to jugador
                                                    ) as Map<String, Any>
                                                ).addOnSuccessListener {
                                                    Toast.makeText(
                                                        binding.root.context,
                                                        "Rebote Defensivo del jugador " + lista[i].textOn,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    calcularVal(listJugador[j], jugador)
                                                }
                                        }
                                    }

                                }
                            db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listRegistros =
                                        it.get("registro") as ArrayList<Map<String?, Any?>>
                                    val registro = hashMapOf(
                                        "cuarto" to cuarto,
                                        "dorsal" to lista[i].text,
                                        "frase" to "REBOTE DEFENSIVO",
                                        "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                        "tiempo" to binding.TiempoCuarto.text.toString(),
                                        "equipo" to "Local",
                                        "tipoFrase" to "1"
                                    ) as Map<String?, Any?>
                                    listRegistros.add(registro)
                                    db.collection("MinutoaMinuto")
                                        .document(idpartido.toString())
                                        .update(
                                            hashMapOf(
                                                "registro" to listRegistros,
                                            ) as Map<String?, Any?>
                                        )
                                }
                        } else {
                            db.collection("Estadisticas").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listJugador =
                                        it.get("ListadoJugadores") as ArrayList<String>
                                    for (j in 0..listJugador.count() - 1) {
                                        val jugador =
                                            (it.get(listJugador[j]) as Map<String?, Any?>).toMutableMap()

                                        if (jugador["dorsal"] == lista[i].text && jugador["equipo"] == "Visitante") {
                                            jugador["rebD"] =
                                                jugador["rebD"].toString().toInt() + 1
                                            db.collection("Estadisticas")
                                                .document(idpartido.toString())
                                                .update(
                                                    hashMapOf(
                                                        listJugador[j] to jugador
                                                    ) as Map<String, Any>
                                                ).addOnSuccessListener {
                                                    Toast.makeText(
                                                        binding.root.context,
                                                        "Rebote Defensivo del jugador " + lista[i].textOn,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    calcularVal(listJugador[j], jugador)
                                                }
                                        }
                                    }

                                }
                            db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                                .addOnSuccessListener {
                                    val listRegistros =
                                        it.get("registro") as ArrayList<Map<String?, Any?>>
                                    val registro = hashMapOf(
                                        "cuarto" to cuarto,
                                        "dorsal" to lista[i].text,
                                        "frase" to "REBOTE DEFENSIVO",
                                        "resultado" to binding.txtPuntosLocal.text.toString() + "-" + binding.txtPuntosVisitante.text.toString(),
                                        "tiempo" to binding.TiempoCuarto.text.toString(),
                                        "equipo" to "Visitante",
                                        "tipoFrase" to "1"
                                    ) as Map<String?, Any?>
                                    listRegistros.add(registro)
                                    db.collection("MinutoaMinuto")
                                        .document(idpartido.toString())
                                        .update(
                                            hashMapOf(
                                                "registro" to listRegistros,
                                            ) as Map<String?, Any?>
                                        )
                                }
                        }
                    }
                }
                vaciarToggle(lista)
                dialog.hide()
            }
        }

        binding.TiempoCuarto.setOnClickListener {
            play()
        }

        return binding.root
    }

    fun play() {
        if (!isPlay) {
            binding.TiempoCuarto.base = SystemClock.elapsedRealtime() + pauseOffSet
            binding.TiempoCuarto.setTextColor(Color.WHITE)
            binding.TiempoCuarto.start()
            isPlay = true
        } else {
            binding.TiempoCuarto.stop()
            binding.TiempoCuarto.setTextColor(Color.RED)
            pauseOffSet = -1 * (SystemClock.elapsedRealtime() - binding.TiempoCuarto.base)
            isPlay = false
        }
    }

    fun calcularVal(idJugador: String, jugador: MutableMap<String?, Any?>) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val idpartido = prefs.getString("idPartido", "")
        val valoracion: Int =
            (jugador["puntos"].toString().toInt() + jugador["tc2pA"].toString()
                .toInt() + jugador["tc3pA"].toString().toInt() +
                    jugador["asi"].toString().toInt() + jugador["rebO"].toString()
                .toInt() + jugador["rebD"].toString().toInt() +
                    jugador["recu"].toString().toInt() + jugador["taCom"].toString()
                .toInt() + jugador["tlA"].toString().toInt()) - (jugador["fal"].toString()
                .toInt() + jugador["tc2pF"].toString().toInt() + jugador["tc3pF"].toString()
                .toInt() + jugador["per"].toString().toInt() + jugador["taRec"].toString()
                .toInt() + jugador["tlF"].toString().toInt())
        jugador["val"] = valoracion
        db.collection("Estadisticas")
            .document(idpartido.toString())
            .update(
                hashMapOf(
                    idJugador to jugador
                ) as Map<String, Any>
            )
    }

    fun vaciarToggle(lista: ArrayList<ToggleButton>) {
        for (i in 0..lista.count() - 1) {
            if (lista[i].isChecked) {
                lista[i].isChecked = false
            }
        }
    }

    fun llenarListToggle(): ArrayList<ToggleButton> {
        val lista: ArrayList<ToggleButton> = java.util.ArrayList<ToggleButton>()
        lista.add(binding.TBLocal1)
        lista.add(binding.TBLocal2)
        lista.add(binding.TBLocal3)
        lista.add(binding.TBLocal4)
        lista.add(binding.TBLocal5)
        lista.add(binding.TBVisitante1)
        lista.add(binding.TBVisitante2)
        lista.add(binding.TBVisitante3)
        lista.add(binding.TBVisitante4)
        lista.add(binding.TBVisitante5)
        return lista
    }

    fun llenarListToggleLocal(): ArrayList<ToggleButton> {
        val lista: ArrayList<ToggleButton> = java.util.ArrayList<ToggleButton>()
        lista.add(binding.TBLocal1)
        lista.add(binding.TBLocal2)
        lista.add(binding.TBLocal3)
        lista.add(binding.TBLocal4)
        lista.add(binding.TBLocal5)
        return lista
    }

    fun Faltas5Local(lista: ArrayList<ToggleButton>, toggleButtonSale: ToggleButton) {
        val builder = AlertDialog.Builder(binding.root.context)
        val view = layoutInflater.inflate(R.layout.cambios_equipo, null)
        view.findViewById<TextView>(R.id.txtQuinteto).text =
            "El jugador #" + toggleButtonSale.text + " lleva 5 faltas, por lo que esta expulsado."

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val idpartido = prefs.getString("idPartido", "")
        var contLoca: Int = 0
        var quinteto: Int = 0

        db.collection("Estadisticas").document(idpartido.toString()).get()
            .addOnSuccessListener {
                val listJugador = it.get("ListadoJugadores") as ArrayList<String>

                val listToggleButton: ArrayList<ToggleButton> =
                    java.util.ArrayList<ToggleButton>()

                for (i in 0..listJugador.count() - 1) {
                    val jugador = it.get(listJugador[i].toString()) as Map<String?, Any?>
                    if (jugador["equipo"].toString() == "Local" && jugador["fal"].toString() != "5") {
                        if (jugador["dorsal"] != lista[0].text && jugador["dorsal"] != lista[1].text && jugador["dorsal"] != lista[2].text && jugador["dorsal"] != lista[3].text && jugador["dorsal"] != lista[4].text) {

                            val toggleButton: ToggleButton = ToggleButton(view.context)
                            toggleButton.text = jugador["dorsal"].toString()
                            toggleButton.id = contLoca
                            toggleButton.textOff = jugador["dorsal"].toString()
                            toggleButton.textOn = jugador["dorsal"].toString()
                            toggleButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonlocaldesactivado))

                            if (contLoca < 5)
                                view.findViewById<LinearLayout>(R.id.ContenedorCambios)
                                    .addView(toggleButton)
                            else
                                view.findViewById<LinearLayout>(R.id.ContenedorCambios2)
                                    .addView(toggleButton)

                            toggleButton.setOnCheckedChangeListener { compoundButton, b ->

                                if (toggleButton.isChecked) {
                                    if (quinteto < 1) {
                                        toggleButton.setBackgroundDrawable(
                                            getResources().getDrawable(
                                                R.drawable.togglebuttonlocalactivado
                                            )
                                        )
                                        quinteto++
                                    } else {
                                        toggleButton.isChecked = false
                                        Toast.makeText(
                                            binding.root.context,
                                            "Solo se puede seleccionar 1 jugador",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    if (quinteto > 0) {
                                        toggleButton.setBackgroundDrawable(
                                            getResources().getDrawable(
                                                R.drawable.togglebuttonlocaldesactivado
                                            )
                                        )
                                        quinteto--
                                    }
                                }
                            }
                            listToggleButton.add(toggleButton)
                            contLoca++
                        }
                    }

                }

                builder.setView(view)

                val dialog = builder.create()
                dialog.show()

                view.findViewById<Button>(R.id.btnGuardarPlantillaCambios).setOnClickListener {
                    if (quinteto == 1) {
                        var cont = 0
                        db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                            .addOnSuccessListener { it2 ->
                                val listRegistros =
                                    it2.get("registro") as ArrayList<Map<String?, Any?>>
                                val registro = hashMapOf(
                                    "cuarto" to cuarto,
                                    "dorsal" to toggleButtonSale.text,
                                    "frase" to "SALE DE LA PISTA EL",
                                    "resultado" to "",
                                    "tiempo" to binding.TiempoCuarto.text.toString(),
                                    "equipo" to "Local",
                                    "tipoFrase" to "3"
                                ) as Map<String?, Any?>
                                listRegistros.add(registro)
                                for (i in 0..listToggleButton.count() - 1) {
                                    val toggleButton = listToggleButton[i]
                                    if (toggleButton.isChecked) {
                                        if (cont == 0) {
                                            toggleButtonSale.text = toggleButton.text
                                            toggleButtonSale.textOff = toggleButton.textOff
                                            toggleButtonSale.textOn = toggleButton.textOn
                                        }
                                        val registro = hashMapOf(
                                            "cuarto" to cuarto,
                                            "dorsal" to toggleButton.text,
                                            "frase" to "ENTRA A LA PISTA EL",
                                            "resultado" to "",
                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                            "equipo" to "Local",
                                            "tipoFrase" to "3"
                                        ) as Map<String?, Any?>
                                        listRegistros.add(registro)
                                        cont++
                                    }
                                }
                                db.collection("MinutoaMinuto")
                                    .document(idpartido.toString())
                                    .update(
                                        hashMapOf(
                                            "registro" to listRegistros,
                                        ) as Map<String?, Any?>
                                    )
                            }
                        dialog.hide()
                    } else {
                        Toast.makeText(
                            binding.root.context,
                            "Seleccione a 1 jugador para continuar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        vaciarToggle(llenarListToggle())

    }

    fun Faltas5Visitante(lista: ArrayList<ToggleButton>, toggleButtonSale: ToggleButton) {
        val builder = AlertDialog.Builder(binding.root.context)
        val view = layoutInflater.inflate(R.layout.cambios_equipo, null)
        view.findViewById<TextView>(R.id.txtQuinteto).text =
            "El jugador #" + toggleButtonSale.text + " lleva 5 faltas, por lo que esta expulsado."
        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val idpartido = prefs.getString("idPartido", "")
        var contLoca: Int = 0
        var quinteto: Int = 0

        db.collection("Estadisticas").document(idpartido.toString()).get()
            .addOnSuccessListener {
                val listJugador = it.get("ListadoJugadores") as ArrayList<String>

                val listToggleButton: ArrayList<ToggleButton> =
                    java.util.ArrayList<ToggleButton>()

                for (i in 0..listJugador.count() - 1) {
                    val jugador = it.get(listJugador[i].toString()) as Map<String?, Any?>
                    if (jugador["equipo"].toString() == "Visitante" && jugador["fal"].toString() != "5") {
                        if (jugador["dorsal"] != lista[0].text && jugador["dorsal"] != lista[1].text && jugador["dorsal"] != lista[2].text && jugador["dorsal"] != lista[3].text && jugador["dorsal"] != lista[4].text) {

                            val toggleButton: ToggleButton = ToggleButton(view.context)
                            toggleButton.text = jugador["dorsal"].toString()
                            toggleButton.id = contLoca
                            toggleButton.textOff = jugador["dorsal"].toString()
                            toggleButton.textOn = jugador["dorsal"].toString()
                            toggleButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonvisitantedesactivado))

                            if (contLoca < 5)
                                view.findViewById<LinearLayout>(R.id.ContenedorCambios)
                                    .addView(toggleButton)
                            else
                                view.findViewById<LinearLayout>(R.id.ContenedorCambios2)
                                    .addView(toggleButton)

                            toggleButton.setOnCheckedChangeListener { compoundButton, b ->

                                if (toggleButton.isChecked) {
                                    if (quinteto < 1) {
                                        toggleButton.setBackgroundDrawable(
                                            getResources().getDrawable(
                                                R.drawable.togglebuttonvisitanteactivo
                                            )
                                        )
                                        quinteto++
                                    } else {
                                        toggleButton.isChecked = false
                                        Toast.makeText(
                                            binding.root.context,
                                            "Solo se puede seleccionar 1 jugador",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    if (quinteto > 0) {
                                        toggleButton.setBackgroundDrawable(
                                            getResources().getDrawable(
                                                R.drawable.togglebuttonvisitantedesactivado
                                            )
                                        )
                                        quinteto--
                                    }
                                }
                            }
                            listToggleButton.add(toggleButton)
                            contLoca++
                        }
                    }

                }

                builder.setView(view)

                val dialog = builder.create()
                dialog.show()

                view.findViewById<Button>(R.id.btnGuardarPlantillaCambios).setOnClickListener {
                    if (quinteto == 1) {
                        var cont = 0
                        db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                            .addOnSuccessListener { it2 ->
                                val listRegistros =
                                    it2.get("registro") as ArrayList<Map<String?, Any?>>
                                val registro = hashMapOf(
                                    "cuarto" to cuarto,
                                    "dorsal" to toggleButtonSale.text,
                                    "frase" to "SALE DE LA PISTA EL",
                                    "resultado" to "",
                                    "tiempo" to binding.TiempoCuarto.text.toString(),
                                    "equipo" to "Visitante",
                                    "tipoFrase" to "3"
                                ) as Map<String?, Any?>
                                listRegistros.add(registro)
                                for (i in 0..listToggleButton.count() - 1) {
                                    val toggleButton = listToggleButton[i]
                                    if (toggleButton.isChecked) {
                                        if (cont == 0) {
                                            toggleButtonSale.text = toggleButton.text
                                            toggleButtonSale.textOff = toggleButton.textOff
                                            toggleButtonSale.textOn = toggleButton.textOn
                                        }
                                        val registro = hashMapOf(
                                            "cuarto" to cuarto,
                                            "dorsal" to toggleButton.text,
                                            "frase" to "ENTRA A LA PISTA EL",
                                            "resultado" to "",
                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                            "equipo" to "Visitante",
                                            "tipoFrase" to "3"
                                        ) as Map<String?, Any?>
                                        listRegistros.add(registro)
                                        cont++
                                    }
                                }
                                db.collection("MinutoaMinuto")
                                    .document(idpartido.toString())
                                    .update(
                                        hashMapOf(
                                            "registro" to listRegistros,
                                        ) as Map<String?, Any?>
                                    )
                            }
                        dialog.hide()
                    } else {
                        Toast.makeText(
                            binding.root.context,
                            "Seleccione a 1 jugador para continuar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        vaciarToggle(llenarListToggle())

    }


    fun hacerCambiosLocal(lista: ArrayList<ToggleButton>, toggleButtonSale: ToggleButton) {
        val builder = AlertDialog.Builder(binding.root.context)
        val view = layoutInflater.inflate(R.layout.cambios_equipo, null)

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val idpartido = prefs.getString("idPartido", "")
        var contLoca: Int = 0
        var quinteto: Int = 0

        db.collection("Estadisticas").document(idpartido.toString()).get()
            .addOnSuccessListener {
                val listJugador = it.get("ListadoJugadores") as ArrayList<String>

                val listToggleButton: ArrayList<ToggleButton> =
                    java.util.ArrayList<ToggleButton>()

                for (i in 0..listJugador.count() - 1) {
                    val jugador = it.get(listJugador[i].toString()) as Map<String?, Any?>
                    if (jugador["equipo"].toString() == "Local" && jugador["fal"].toString() != "5") {
                        if (jugador["dorsal"] != lista[0].text && jugador["dorsal"] != lista[1].text && jugador["dorsal"] != lista[2].text && jugador["dorsal"] != lista[3].text && jugador["dorsal"] != lista[4].text) {

                            val toggleButton: ToggleButton = ToggleButton(view.context)
                            toggleButton.text = jugador["dorsal"].toString()
                            toggleButton.id = contLoca
                            toggleButton.textOff = jugador["dorsal"].toString()
                            toggleButton.textOn = jugador["dorsal"].toString()
                            toggleButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonlocaldesactivado))

                            if (contLoca < 5)
                                view.findViewById<LinearLayout>(R.id.ContenedorCambios)
                                    .addView(toggleButton)
                            else
                                view.findViewById<LinearLayout>(R.id.ContenedorCambios2)
                                    .addView(toggleButton)

                            toggleButton.setOnCheckedChangeListener { compoundButton, b ->

                                if (toggleButton.isChecked) {
                                    if (quinteto < 1) {
                                        toggleButton.setBackgroundDrawable(
                                            getResources().getDrawable(
                                                R.drawable.togglebuttonlocalactivado
                                            )
                                        )
                                        quinteto++
                                    } else {
                                        toggleButton.isChecked = false
                                        Toast.makeText(
                                            binding.root.context,
                                            "Solo se puede seleccionar 1 jugador",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    if (quinteto > 0) {
                                        toggleButton.setBackgroundDrawable(
                                            getResources().getDrawable(
                                                R.drawable.togglebuttonlocaldesactivado
                                            )
                                        )
                                        quinteto--
                                    }
                                }
                            }
                            listToggleButton.add(toggleButton)
                            contLoca++
                        }
                    }

                }

                builder.setView(view)

                val dialog = builder.create()
                dialog.show()

                view.findViewById<Button>(R.id.btnGuardarPlantillaCambios).setOnClickListener {
                    if (quinteto == 1) {
                        var cont = 0
                        db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                            .addOnSuccessListener { it2 ->
                                val listRegistros =
                                    it2.get("registro") as ArrayList<Map<String?, Any?>>
                                val registro = hashMapOf(
                                    "cuarto" to cuarto,
                                    "dorsal" to toggleButtonSale.text,
                                    "frase" to "SALE DE LA PISTA EL",
                                    "resultado" to "",
                                    "tiempo" to binding.TiempoCuarto.text.toString(),
                                    "equipo" to "Local",
                                    "tipoFrase" to "3"
                                ) as Map<String?, Any?>
                                listRegistros.add(registro)
                                for (i in 0..listToggleButton.count() - 1) {
                                    val toggleButton = listToggleButton[i]
                                    if (toggleButton.isChecked) {
                                        if (cont == 0) {
                                            toggleButtonSale.text = toggleButton.text
                                            toggleButtonSale.textOff = toggleButton.textOff
                                            toggleButtonSale.textOn = toggleButton.textOn
                                        }
                                        val registro = hashMapOf(
                                            "cuarto" to cuarto,
                                            "dorsal" to toggleButton.text,
                                            "frase" to "ENTRA A LA PISTA EL",
                                            "resultado" to "",
                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                            "equipo" to "Local",
                                            "tipoFrase" to "3"
                                        ) as Map<String?, Any?>
                                        listRegistros.add(registro)
                                        cont++
                                    }
                                }
                                db.collection("MinutoaMinuto")
                                    .document(idpartido.toString())
                                    .update(
                                        hashMapOf(
                                            "registro" to listRegistros,
                                        ) as Map<String?, Any?>
                                    )
                            }
                        dialog.hide()
                    } else {
                        Toast.makeText(
                            binding.root.context,
                            "Seleccione a 1 jugador para continuar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        vaciarToggle(llenarListToggle())

    }

    fun hacerCambiosVisitante(lista: ArrayList<ToggleButton>, toggleButtonSale: ToggleButton) {
        val builder = AlertDialog.Builder(binding.root.context)
        val view = layoutInflater.inflate(R.layout.cambios_equipo, null)

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val idpartido = prefs.getString("idPartido", "")
        var contLoca: Int = 0
        var quinteto: Int = 0

        db.collection("Estadisticas").document(idpartido.toString()).get()
            .addOnSuccessListener {
                val listJugador = it.get("ListadoJugadores") as ArrayList<String>

                val listToggleButton: ArrayList<ToggleButton> =
                    java.util.ArrayList<ToggleButton>()

                for (i in 0..listJugador.count() - 1) {
                    val jugador = it.get(listJugador[i].toString()) as Map<String?, Any?>
                    if (jugador["equipo"].toString() == "Visitante" && jugador["fal"].toString() != "5") {
                        if (jugador["dorsal"] != lista[0].text && jugador["dorsal"] != lista[1].text && jugador["dorsal"] != lista[2].text && jugador["dorsal"] != lista[3].text && jugador["dorsal"] != lista[4].text) {

                            val toggleButton: ToggleButton = ToggleButton(view.context)
                            toggleButton.text = jugador["dorsal"].toString()
                            toggleButton.id = contLoca
                            toggleButton.textOff = jugador["dorsal"].toString()
                            toggleButton.textOn = jugador["dorsal"].toString()
                            toggleButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonvisitantedesactivado))

                            if (contLoca < 5)
                                view.findViewById<LinearLayout>(R.id.ContenedorCambios)
                                    .addView(toggleButton)
                            else
                                view.findViewById<LinearLayout>(R.id.ContenedorCambios2)
                                    .addView(toggleButton)

                            toggleButton.setOnCheckedChangeListener { compoundButton, b ->

                                if (toggleButton.isChecked) {
                                    if (quinteto < 1) {
                                        toggleButton.setBackgroundDrawable(
                                            getResources().getDrawable(
                                                R.drawable.togglebuttonvisitanteactivo
                                            )
                                        )
                                        quinteto++
                                    } else {
                                        toggleButton.isChecked = false
                                        Toast.makeText(
                                            binding.root.context,
                                            "Solo se puede seleccionar 1 jugador",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    if (quinteto > 0) {
                                        toggleButton.setBackgroundDrawable(
                                            getResources().getDrawable(
                                                R.drawable.togglebuttonvisitantedesactivado
                                            )
                                        )
                                        quinteto--
                                    }
                                }
                            }
                            listToggleButton.add(toggleButton)
                            contLoca++
                        }
                    }

                }

                builder.setView(view)

                val dialog = builder.create()
                dialog.show()

                view.findViewById<Button>(R.id.btnGuardarPlantillaCambios).setOnClickListener {
                    if (quinteto == 1) {
                        var cont = 0
                        db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                            .addOnSuccessListener { it2 ->
                                val listRegistros =
                                    it2.get("registro") as ArrayList<Map<String?, Any?>>
                                val registro = hashMapOf(
                                    "cuarto" to cuarto,
                                    "dorsal" to toggleButtonSale.text,
                                    "frase" to "SALE DE LA PISTA EL",
                                    "resultado" to "",
                                    "tiempo" to binding.TiempoCuarto.text.toString(),
                                    "equipo" to "Visitante",
                                    "tipoFrase" to "3"
                                ) as Map<String?, Any?>
                                listRegistros.add(registro)
                                for (i in 0..listToggleButton.count() - 1) {
                                    val toggleButton = listToggleButton[i]
                                    if (toggleButton.isChecked) {
                                        if (cont == 0) {
                                            toggleButtonSale.text = toggleButton.text
                                            toggleButtonSale.textOff = toggleButton.textOff
                                            toggleButtonSale.textOn = toggleButton.textOn
                                        }
                                        val registro = hashMapOf(
                                            "cuarto" to cuarto,
                                            "dorsal" to toggleButton.text,
                                            "frase" to "ENTRA A LA PISTA EL",
                                            "resultado" to "",
                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                            "equipo" to "Visitante",
                                            "tipoFrase" to "3"
                                        ) as Map<String?, Any?>
                                        listRegistros.add(registro)
                                        cont++
                                    }
                                }
                                db.collection("MinutoaMinuto")
                                    .document(idpartido.toString())
                                    .update(
                                        hashMapOf(
                                            "registro" to listRegistros,
                                        ) as Map<String?, Any?>
                                    )
                            }
                        dialog.hide()
                    } else {
                        Toast.makeText(
                            binding.root.context,
                            "Seleccione a 1 jugador para continuar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        vaciarToggle(llenarListToggle())

    }

    fun llenarListToggleVisitante(): ArrayList<ToggleButton> {
        val lista: ArrayList<ToggleButton> = java.util.ArrayList<ToggleButton>()
        lista.add(binding.TBVisitante1)
        lista.add(binding.TBVisitante2)
        lista.add(binding.TBVisitante3)
        lista.add(binding.TBVisitante4)
        lista.add(binding.TBVisitante5)
        return lista
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        }
    }

    fun quintetoLocal() {

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val idpartido = prefs.getString("idPartido", "")
        var contLoca: Int = 0
        var quinteto: Int = 0

        val builder = AlertDialog.Builder(binding.root.context)
        val view = layoutInflater.inflate(R.layout.cambios_equipo, null)

        db.collection("Estadisticas").document(idpartido.toString()).get()
            .addOnSuccessListener {
                val listJugador = it.get("ListadoJugadores") as ArrayList<String>
                val listToggleButton: ArrayList<ToggleButton> =
                    java.util.ArrayList<ToggleButton>()
                for (i in 0..listJugador.count() - 1) {
                    val jugador = it.get(listJugador[i].toString()) as Map<String?, Any?>
                    if (jugador["equipo"].toString() == "Local") {
                        val toggleButton: ToggleButton = ToggleButton(view.context)
                        toggleButton.text = jugador["dorsal"].toString()
                        toggleButton.id = contLoca
                        toggleButton.textOff = jugador["dorsal"].toString()
                        toggleButton.textOn = jugador["dorsal"].toString()
                        toggleButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonlocaldesactivado))

                        if (contLoca < 6)
                            view.findViewById<LinearLayout>(R.id.ContenedorCambios)
                                .addView(toggleButton)
                        else
                            view.findViewById<LinearLayout>(R.id.ContenedorCambios2)
                                .addView(toggleButton)

                        toggleButton.setOnCheckedChangeListener { compoundButton, b ->

                            if (toggleButton.isChecked) {
                                if (quinteto < 5) {
                                    toggleButton.setBackgroundDrawable(
                                        getResources().getDrawable(
                                            R.drawable.togglebuttonlocalactivado
                                        )
                                    )
                                    quinteto++
                                } else {
                                    toggleButton.isChecked = false
                                    Toast.makeText(
                                        binding.root.context,
                                        "Solo se puede seleccionar 5 jugadores ",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                if (quinteto > 0) {
                                    toggleButton.setBackgroundDrawable(
                                        getResources().getDrawable(
                                            R.drawable.togglebuttonlocaldesactivado
                                        )
                                    )
                                    quinteto--
                                }
                            }
                        }
                        listToggleButton.add(toggleButton)
                        contLoca++
                    }
                }

                builder.setView(view)
                val dialog = builder.create()
                dialog.show()

                view.findViewById<Button>(R.id.btnGuardarPlantillaCambios).setOnClickListener {
                    if (quinteto == 5) {
                        var cont = 0
                        db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                            .addOnSuccessListener { it2 ->
                                val listRegistros =
                                    it2.get("registro") as ArrayList<Map<String?, Any?>>
                                val registro = hashMapOf(
                                    "cuarto" to cuarto,
                                    "dorsal" to "",
                                    "frase" to "INICIO DEL PERIODO",
                                    "resultado" to "",
                                    "tiempo" to "",
                                    "equipo" to "",
                                    "tipoFrase" to "2"
                                ) as Map<String?, Any?>
                                listRegistros.add(registro)
                                for (i in 0..listToggleButton.count() - 1) {
                                    val toggleButton = listToggleButton[i]
                                    if (toggleButton.isChecked) {
                                        if (cont == 0) {
                                            binding.TBLocal1.text = toggleButton.text
                                            binding.TBLocal1.textOff = toggleButton.textOff
                                            binding.TBLocal1.textOn = toggleButton.textOn
                                        } else if (cont == 1) {
                                            binding.TBLocal2.text = toggleButton.text
                                            binding.TBLocal2.textOff = toggleButton.textOff
                                            binding.TBLocal2.textOn = toggleButton.textOn
                                        } else if (cont == 2) {
                                            binding.TBLocal3.text = toggleButton.text
                                            binding.TBLocal3.textOff = toggleButton.textOff
                                            binding.TBLocal3.textOn = toggleButton.textOn
                                        } else if (cont == 3) {
                                            binding.TBLocal4.text = toggleButton.text
                                            binding.TBLocal4.textOff = toggleButton.textOff
                                            binding.TBLocal4.textOn = toggleButton.textOn
                                        } else if (cont == 4) {
                                            binding.TBLocal5.text = toggleButton.text
                                            binding.TBLocal5.textOff = toggleButton.textOff
                                            binding.TBLocal5.textOn = toggleButton.textOn
                                        }
                                        val registro = hashMapOf(
                                            "cuarto" to cuarto,
                                            "dorsal" to toggleButton.text,
                                            "frase" to "ENTRA A LA PISTA EL",
                                            "resultado" to "",
                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                            "equipo" to "Local",
                                            "tipoFrase" to "3"
                                        ) as Map<String?, Any?>
                                        listRegistros.add(registro)
                                        cont++
                                    }
                                }
                                db.collection("MinutoaMinuto")
                                    .document(idpartido.toString())
                                    .update(
                                        hashMapOf(
                                            "registro" to listRegistros,
                                        ) as Map<String?, Any?>
                                    )
                            }
                        dialog.hide()
                        quintetoVisitante()
                    } else {
                        Toast.makeText(
                            binding.root.context,
                            "Seleccione a 5 jugadores para continuar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    fun quintetoVisitante() {

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val idpartido = prefs.getString("idPartido", "")
        var contLoca: Int = 0
        var quinteto: Int = 0

        val builder = AlertDialog.Builder(binding.root.context)
        val view = layoutInflater.inflate(R.layout.cambios_equipo, null)
        db.collection("Estadisticas").document(idpartido.toString()).get()
            .addOnSuccessListener {
                val listJugador = it.get("ListadoJugadores") as ArrayList<String>
                val listToggleButton: ArrayList<ToggleButton> =
                    java.util.ArrayList<ToggleButton>()
                for (i in 0..listJugador.count() - 1) {
                    val jugador = it.get(listJugador[i].toString()) as Map<String?, Any?>
                    if (jugador["equipo"].toString() == "Visitante") {
                        val toggleButton: ToggleButton = ToggleButton(view.context)
                        toggleButton.text = jugador["dorsal"].toString()
                        toggleButton.id = contLoca
                        toggleButton.textOff = jugador["dorsal"].toString()
                        toggleButton.textOn = jugador["dorsal"].toString()
                        toggleButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.togglebuttonvisitantedesactivado))

                        if (contLoca < 6)
                            view.findViewById<LinearLayout>(R.id.ContenedorCambios)
                                .addView(toggleButton)
                        else
                            view.findViewById<LinearLayout>(R.id.ContenedorCambios2)
                                .addView(toggleButton)

                        toggleButton.setOnCheckedChangeListener { compoundButton, b ->

                            if (toggleButton.isChecked) {
                                if (quinteto < 5) {
                                    toggleButton.setBackgroundDrawable(
                                        getResources().getDrawable(
                                            R.drawable.togglebuttonvisitanteactivo
                                        )
                                    )
                                    quinteto++
                                } else {
                                    toggleButton.isChecked = false
                                    Toast.makeText(
                                        binding.root.context,
                                        "Solo se puede seleccionar 5 jugadores ",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                if (quinteto > 0) {
                                    toggleButton.setBackgroundDrawable(
                                        getResources().getDrawable(
                                            R.drawable.togglebuttonvisitantedesactivado
                                        )
                                    )
                                    quinteto--
                                }
                            }
                        }
                        listToggleButton.add(toggleButton)
                        contLoca++
                    }
                }

                builder.setView(view)
                val dialog = builder.create()
                dialog.show()

                view.findViewById<Button>(R.id.btnGuardarPlantillaCambios).setOnClickListener {
                    if (quinteto == 5) {
                        var cont = 0
                        db.collection("MinutoaMinuto").document(idpartido.toString()).get()
                            .addOnSuccessListener { it2 ->
                                val listRegistros =
                                    it2.get("registro") as ArrayList<Map<String?, Any?>>
                                for (i in 0..listToggleButton.count() - 1) {
                                    val toggleButton = listToggleButton[i]
                                    if (toggleButton.isChecked) {
                                        if (cont == 0) {
                                            binding.TBVisitante1.text = toggleButton.text
                                            binding.TBVisitante1.textOff = toggleButton.textOff
                                            binding.TBVisitante1.textOn = toggleButton.textOn
                                        } else if (cont == 1) {
                                            binding.TBVisitante2.text = toggleButton.text
                                            binding.TBVisitante2.textOff = toggleButton.textOff
                                            binding.TBVisitante2.textOn = toggleButton.textOn
                                        } else if (cont == 2) {
                                            binding.TBVisitante3.text = toggleButton.text
                                            binding.TBVisitante3.textOff = toggleButton.textOff
                                            binding.TBVisitante3.textOn = toggleButton.textOn
                                        } else if (cont == 3) {
                                            binding.TBVisitante4.text = toggleButton.text
                                            binding.TBVisitante4.textOff = toggleButton.textOff
                                            binding.TBVisitante4.textOn = toggleButton.textOn
                                        } else if (cont == 4) {
                                            binding.TBVisitante5.text = toggleButton.text
                                            binding.TBVisitante5.textOff = toggleButton.textOff
                                            binding.TBVisitante5.textOn = toggleButton.textOn
                                        }
                                        val registro = hashMapOf(
                                            "cuarto" to cuarto,
                                            "dorsal" to toggleButton.text,
                                            "frase" to "ENTRA A LA PISTA EL",
                                            "resultado" to "",
                                            "tiempo" to binding.TiempoCuarto.text.toString(),
                                            "equipo" to "Visitante",
                                            "tipoFrase" to "3"
                                        ) as Map<String?, Any?>
                                        listRegistros.add(registro)
                                        cont++
                                    }
                                }
                                db.collection("MinutoaMinuto")
                                    .document(idpartido.toString())
                                    .update(
                                        hashMapOf(
                                            "registro" to listRegistros,
                                        ) as Map<String?, Any?>
                                    )

                            }
                        dialog.hide()

                    } else {
                        Toast.makeText(
                            binding.root.context,
                            "Seleccione a 5 jugadores para continuar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

}


