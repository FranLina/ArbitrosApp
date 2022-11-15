package com.franlinares.app.Liga

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.franlinares.app.BaseFragment
import com.franlinares.app.Equipo.ListViewEquipo.AdaptadorEquipo
import com.franlinares.app.Equipo.ListViewEquipo.Equipos
import com.franlinares.app.databinding.FragmentIniciarLigaBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class IniciarLigaFragment : BaseFragment() {

    private var _binding: FragmentIniciarLigaBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIniciarLigaBinding.inflate(inflater, container, false)
        llenarSpinner()

        binding.btnILIniciar.setOnClickListener {
            generarEmparejamiento(binding.spinner.selectedItem.toString())
        }

        binding.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    llenarListView()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}

            }

        return binding.root
    }

    fun llenarListView() {
        db.collection("Equipos").get().addOnSuccessListener {
            val listaEquipos = mutableListOf<Equipos>()
            for (equipos in it) {
                if (equipos.get("Liga").toString() == binding.spinner.selectedItem.toString()) {
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
            }

            val adapter = AdaptadorEquipo(binding.root.context, listaEquipos)

            binding.ListViewEquiposALiga.adapter = adapter
        }

    }

    fun llenarSpinner() {
        val listaLigas = mutableListOf<String>()
        db.collection("Ligas").get().addOnSuccessListener {
            for (ligas in it) {
                if (ligas.get("Activa") == false)
                    listaLigas.add(ligas.id)
            }
            val adaptadorLiga =
                ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_item)
            adaptadorLiga.addAll(listaLigas)
            binding.spinner.adapter = adaptadorLiga
        }
    }

    fun generarEmparejamiento(liga: String) {

//        Las jornadas pares de la primera vuelta se forman a partir de la jornada anterior con estos cambios:
//        • El equipo que está el último de la columna izquierda –equipo comodín– se coloca en el primer lugar
//        de esa misma columna. El resto de los equipos de esa columna izquierda bajan un lugar.
//        • A continuación se intercambian las dos columnas de las que consta la jornada.
//
//        Las jornadas impares de la primera vuelta se forman a partir de la jornada impar anterior, de esta manera:
//        el equipo comodín –que está en el último lugar de la columna izquierda– permanece en su lugar; los demás
//                equipos se mueven un puesto mediante una permutación circular en el sentido contrario a las agujas del reloj.
//
//        Las jornadas de la segunda vuelta se confeccionan intercambiando las columnas de las correspondientes jornadas
//        de la primera vuelta. Para evitar que un equipo juegue tres veces seguidas en su casa, el equipo comodín se
//        intercambia con su contrario en las dos últimas jornadas de la primera vuelta.
//
//
//        | 1 | 2 | 3 | 4 |
//        -----------------
//
//        IDA               VUELTA
//         1º   2º   3º     4º   5º   6º
//        (3,2)(2,4)(2,1)  (2,3)(4,2)(1,2)
//        (4,1)(1,3)(4,3)  (1,4)(3,1)(3,4)
//
//
//        | 1 | 2 | 3 | 4 | 5 | 6 |
//        -------------------------
//        IDA                          VUELTA
//         1º   2º   3º   4º   5º      6º
//        (4,3)(3,6)(3,2)(2,6)(2,1)   (,)
//        (5,2)(2,4)(4,1)(1,3)(3,5)   (,)
//        (6,1)(1,5)(6,5)(5,4)(6,4)   (,)
//
//
//
//
//        | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |
//        ---------------------------------
//        IDA                                       VUELTA
//         1º   2º   3º   4º   5º   6º   7
//        (5,4)(4,8)(4,3)(3,8)(3,2)(2,8)(2,1)        (,)
//        (6,3)(3,5)(5,2)(2,4)(4,1)(1,3)(3,7)        (,)
//        (7,2)(2,6)(6,1)(1,5)(5,7)(7,4)(4,6)        (,)
//        (8,1)(1,7)(8,7)(7,6)(8,6)(6,5)(8,5)        (,)
//arrayListOf<String>("Andujar", "Linabasket", "FrenteBaecula", "UBBailén")

        var nombreEquipos = arrayListOf<String>()
        db.collection("Ligas").document(liga).get().addOnSuccessListener { documentSnapshot ->
            val nombreEquipo = documentSnapshot.get("Equipos") as ArrayList<String>
            for (i in 0..nombreEquipo.count() - 1) {
                nombreEquipos.add(nombreEquipo[i])
            }
            if (nombreEquipos.count() < 4) {
                Toast.makeText(
                    binding.root.context,
                    "Tiene que haber como mínimo 4 equipos, para iniciar la liga.",
                    Toast.LENGTH_LONG
                ).show()
            } else if (nombreEquipos.count() % 2 != 0) {
                Toast.makeText(
                    binding.root.context,
                    "El número de equipos tiene que ser Par para iniciar la liga.",
                    Toast.LENGTH_LONG
                ).show()
            } else {


                val equipos = hashMapOf<Int, Int?>()

                for (i in 0 until (nombreEquipos.count())) {

                    equipos[i] = i

                }

                val numJornadas = ((nombreEquipos.count()) * 2) - 2
                val numPartidos = (nombreEquipos.count()) / 2

                var jornadaAntigua = Array(2) { IntArray(numPartidos) }
                var vuelta = Array(2) { IntArray(numPartidos) }
                var jornadaAntiguaAux = Array(2) { IntArray(numPartidos) }
                var jornadaAntiguaAux2 = Array(2) { IntArray(numPartidos) }
                val jornadaActual = Array(2) { IntArray(numPartidos) }
                val jornadas = ArrayList<Array<Int>>()


                for (i in 1..(numJornadas / 2)) {
                    if (i == 1) { //Jornada 1
                        var numPartido = 0
                        for (j in numPartidos..(nombreEquipos.count() - 1)) {
                            jornadaActual[numPartido][0] = equipos[j]!!
                            jornadaActual[numPartido][1] =
                                equipos[(nombreEquipos.count() - 1) - j]!!
                            db.collection("Equipos").document(nombreEquipos[equipos[j]!!]).get()
                                .addOnSuccessListener {
                                    db.collection("Partidos")
                                        .document(nombreEquipos[equipos[j]!!] + nombreEquipos[equipos[(nombreEquipos.count() - 1) - j]!!])
                                        .set(
                                            hashMapOf(
                                                "Liga" to liga,
                                                "Jornada" to i,
                                                "Estado" to "Sin Jugar",
                                                "Polideportivo" to it.get("DireccionPabellon"),
                                                "Fecha" to "00/00/0000",
                                                "Hora" to "00:00",
                                                "Resultado" to "",
                                                "EquipoLocal" to nombreEquipos[equipos[j]!!],
                                                "EquipoVisitante" to nombreEquipos[equipos[(nombreEquipos.count() - 1) - j]!!]

                                            ) as Map<String, Any>
                                        )
                                }

                            numPartido++
                        }
                        for (j in 0..numPartidos - 1) {
                            jornadaAntigua[j][0] = jornadaActual[j][0]
                            jornadaAntigua[j][1] = jornadaActual[j][1]
                        }
                        vuelta = generarVuelta(jornadaActual, numPartidos)
                        for (j in 0..(numPartidos - 1)) {
                            db.collection("Partidos")
                                .document(nombreEquipos[vuelta[j][0]] + nombreEquipos[vuelta[j][1]])
                                .set(
                                    hashMapOf(
                                        "Liga" to liga,
                                        "Jornada" to numJornadas / 2 + i,
                                        "Estado" to "Sin Jugar",
                                        "Polideportivo" to "",
                                        "Fecha" to "00/00/0000",
                                        "Hora" to "00:00",
                                        "Resultado" to "",
                                        "EquipoLocal" to nombreEquipos[vuelta[j][0]],
                                        "EquipoVisitante" to nombreEquipos[vuelta[j][1]]
                                    ) as Map<String, Any>
                                )
                        }

                    } else {
                        jornadaActual
                        if (i % 2 == 0) {//Es Par
                            for (j in 0..numPartidos - 1) {
                                jornadaAntiguaAux[j][0] = jornadaAntigua[j][1]
                                jornadaAntiguaAux[j][1] = jornadaAntigua[j][0]
                            }

                            val aux = jornadaAntiguaAux[0][1]
                            jornadaAntiguaAux[0][1] = jornadaAntiguaAux[numPartidos - 1][1]!!
                            jornadaAntiguaAux[numPartidos - 1][1] = aux

                            if (i == (numJornadas / 2) - 1) {
                                val aux = jornadaAntiguaAux[0][1]
                                jornadaAntiguaAux[0][1] = jornadaAntiguaAux[0][0]!!
                                jornadaAntiguaAux[0][0] = aux
                            }

                            for (j in 0..(numPartidos - 1)) {

                                jornadaActual[j][0] = jornadaAntiguaAux[j][0]
                                jornadaActual[j][1] = jornadaAntiguaAux[j][1]

                                db.collection("Equipos")
                                    .document(nombreEquipos[jornadaAntiguaAux[j][0]]).get()
                                    .addOnSuccessListener {
                                        db.collection("Partidos")
                                            .document(nombreEquipos[jornadaAntiguaAux[j][0]] + nombreEquipos[jornadaAntiguaAux[j][1]])
                                            .set(
                                                hashMapOf(
                                                    "Liga" to liga,
                                                    "Jornada" to i,
                                                    "Estado" to "Sin Jugar",
                                                    "Polideportivo" to it.get("DireccionPabellon"),
                                                    "Fecha" to "00/00/0000",
                                                    "Hora" to "00:00",
                                                    "Resultado" to "",
                                                    "EquipoLocal" to nombreEquipos[jornadaAntiguaAux[j][0]],
                                                    "EquipoVisitante" to nombreEquipos[jornadaAntiguaAux[j][1]]
                                                ) as Map<String, Any>
                                            )
                                    }
                            }
                            vuelta = generarVuelta(jornadaAntiguaAux, numPartidos)
                            for (j in 0..(numPartidos - 1)) {
                                db.collection("Partidos")
                                    .document(nombreEquipos[vuelta[j][0]] + nombreEquipos[vuelta[j][1]])
                                    .set(
                                        hashMapOf(
                                            "Liga" to liga,
                                            "Jornada" to numJornadas / 2 + i,
                                            "Estado" to "Sin Jugar",
                                            "Polideportivo" to "",
                                            "Fecha" to "00/00/0000",
                                            "Hora" to "00:00",
                                            "Resultado" to "",
                                            "EquipoLocal" to nombreEquipos[vuelta[j][0]],
                                            "EquipoVisitante" to nombreEquipos[vuelta[j][1]]
                                        ) as Map<String, Any>
                                    )
                            }

                        } else {//Es Impar
                            for (j in 0..numPartidos - 1) {
                                if (j == 0) {
                                    jornadaAntiguaAux2[j][0] = jornadaAntigua[j][1]
                                } else if (j == numPartidos - 1) {
                                    jornadaAntiguaAux2[j][1] = jornadaAntigua[j - 1][0]
                                    jornadaAntiguaAux2[j][0] = jornadaAntigua[j][0]
                                } else {
                                    jornadaAntiguaAux2[j + 1][0] = jornadaAntigua[j][0]
                                    jornadaAntiguaAux2[j - 1][1] = jornadaAntigua[j][1]
                                }
                            }
                            if (i == (numJornadas / 2)) {
                                val aux = jornadaAntiguaAux2[numPartidos - 1][1]
                                jornadaAntiguaAux2[numPartidos - 1][1] =
                                    jornadaAntiguaAux2[numPartidos - 1][0]!!
                                jornadaAntiguaAux2[numPartidos - 1][0] = aux
                            }
                            for (j in 0..numPartidos - 1) {

                                jornadaActual[j][0] = jornadaAntiguaAux2[j][0]
                                jornadaActual[j][1] = jornadaAntiguaAux2[j][1]

                                db.collection("Equipos")
                                    .document(nombreEquipos[jornadaAntiguaAux2[j][0]]).get()
                                    .addOnSuccessListener {
                                        db.collection("Partidos")
                                            .document(nombreEquipos[jornadaAntiguaAux2[j][0]] + nombreEquipos[jornadaAntiguaAux2[j][1]])
                                            .set(
                                                hashMapOf(
                                                    "Liga" to liga,
                                                    "Jornada" to i,
                                                    "Estado" to "Sin Jugar",
                                                    "Polideportivo" to it.get("DireccionPabellon"),
                                                    "Fecha" to "00/00/0000",
                                                    "Hora" to "00:00",
                                                    "Resultado" to "",
                                                    "EquipoLocal" to nombreEquipos[jornadaAntiguaAux2[j][0]],
                                                    "EquipoVisitante" to nombreEquipos[jornadaAntiguaAux2[j][1]]
                                                ) as Map<String, Any>
                                            )
                                    }
                            }

                            vuelta = generarVuelta(jornadaAntiguaAux2, numPartidos)
                            for (j in 0..(numPartidos - 1)) {
                                db.collection("Partidos")
                                    .document(nombreEquipos[vuelta[j][0]] + nombreEquipos[vuelta[j][1]])
                                    .set(
                                        hashMapOf(
                                            "Liga" to liga,
                                            "Jornada" to numJornadas / 2 + i,
                                            "Estado" to "Sin Jugar",
                                            "Polideportivo" to "",
                                            "Fecha" to "00/00/0000",
                                            "Hora" to "00:00",
                                            "Resultado" to "",
                                            "EquipoLocal" to nombreEquipos[vuelta[j][0]],
                                            "EquipoVisitante" to nombreEquipos[vuelta[j][1]]
                                        ) as Map<String, Any>
                                    )
                            }

                            jornadaAntigua = jornadaAntiguaAux2
                        }
                    }
                }

                db.collection("Ligas").document(liga).update(
                    hashMapOf(
                        "Activa" to true,
                        "Clasificacion" to ""
                    ) as Map<String, Any>
                ).addOnSuccessListener {
                    Toast.makeText(
                        binding.root.context,
                        "La liga ha sido iniciada con exito",
                        Toast.LENGTH_LONG
                    ).show()
                    llenarSpinner()
                }
            }
        }
    }

    fun generarVuelta(ida: Array<IntArray>, numPartidos: Int): Array<IntArray> {
        var vuelta = Array(2) { IntArray(numPartidos) }
        for (j in 0..numPartidos - 1) {
            vuelta[j][0] = ida[j][1]
            vuelta[j][1] = ida[j][0]
        }
        return vuelta
    }

}