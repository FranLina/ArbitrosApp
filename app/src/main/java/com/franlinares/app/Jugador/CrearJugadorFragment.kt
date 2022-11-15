package com.franlinares.app.Jugador

import android.R
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.franlinares.app.BaseFragment
import com.franlinares.app.databinding.FragmentCrearJugadorBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import java.util.regex.Pattern

class CrearJugadorFragment : BaseFragment() {

    private var _binding: FragmentCrearJugadorBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private val File = 1
    private var dniJugador = ""

    private val REGEXP: Pattern = Pattern.compile("[0-9]{8}[A-Z]")
    private val DIGITO_CONTROL = "TRWAGMYFPDXBNJZSQVHLCKE"
    private val INVALIDOS = arrayOf("00000000T", "00000001R", "99999999R")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearJugadorBinding.inflate(inflater, container, false)
        llenarSpinner()
        binding.btnCJCrear.setOnClickListener {
            if (binding.txtCJDNI.text.toString() != "" && binding.txtCJFechaNacimiento.text.toString() != "" && binding.txtCJNombre.text.toString() != "" && binding.txtCJDorsal.text.toString() != "") {

                /*if (!validarDNI(binding.txtCJDNI.text.toString())) {

                    Toast.makeText(
                        binding.root.context,
                        "El DNI introducido  no es correcto",
                        Toast.LENGTH_LONG
                    ).show()

                } else */if (validarFecha(binding.txtCJFechaNacimiento.text.toString())) {

                    Toast.makeText(
                        binding.root.context,
                        "La Fecha de Nacimiento introducida no es correcta",
                        Toast.LENGTH_LONG
                    ).show()

                } else {
                    val dni = binding.txtCJDNI.text.toString()
                    val nombre = binding.txtCJNombre.text.toString()
                    val apellido1 = binding.txtCJApellido1.text.toString()
                    val apellido2 = binding.txtCJApellido2.text.toString()
                    val fechaNacimiento = binding.txtCJFechaNacimiento.text.toString()
                    val dorsal = binding.txtCJDorsal.text.toString()
                    val categoria = binding.spinnerCJCategoria.selectedItem.toString()
                    val sexo = binding.spinnerCJSexo.selectedItem.toString()
                    val localidad = binding.txtCJLocalidad.text.toString()
                    val direccion = binding.txtCJDireccion.text.toString()
                    val correo = binding.txtCJCorreo.text.toString()
                    val telefono = binding.txtCJTelefono.text.toString()

                    db.collection("Jugadores")
                        .document(dni)
                        .get().addOnSuccessListener {
                            if (it.get("Nombre") != null) {
                                Toast.makeText(
                                    binding.root.context,
                                    "Ya existe un jugador con este DNI:" + it.id,
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {

                                db.collection("Jugadores")
                                    .document(dni).set(
                                        hashMapOf(
                                            "Nombre" to nombre,
                                            "Apellido1" to apellido1,
                                            "Apellido2" to apellido2,
                                            "FechaNacimineto" to fechaNacimiento,
                                            "Dorsal" to dorsal,
                                            "Categoria" to categoria,
                                            "Sexo" to sexo,
                                            "Localidad" to localidad,
                                            "Direccion" to direccion,
                                            "Correo" to correo,
                                            "Telefono" to telefono,
                                            "UrlFoto" to "",
                                            "Equipo" to ""
                                        ) as Map<String, Any>
                                    ).addOnSuccessListener {
                                        dniJugador = dni
                                        fileUpload()
                                    }.addOnFailureListener { exception ->
                                        Log.w(
                                            ContentValues.TAG,
                                            "Error setting documents.",
                                            exception
                                        )
                                    }
                            }


                        }.addOnFailureListener { exception ->
                            Log.w(ContentValues.TAG, "Error getting documents.", exception)
                        }

                }
            } else {
                Toast.makeText(
                    binding.root.context,
                    "Rellene los campos (DNI, Nombre, Dorsal, Fecha Nacimiento)",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        return binding.root
    }

    fun fileUpload() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, File)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == File) {
            if (resultCode == Activity.RESULT_OK) {
                val FileUri = data!!.data
                val Folder: StorageReference =
                    FirebaseStorage.getInstance().getReference().child("Jugadores")
                val file_name: StorageReference =
                    Folder.child(dniJugador + FileUri!!.lastPathSegment)
                file_name.putFile(FileUri).addOnSuccessListener {
                    file_name.getDownloadUrl().addOnSuccessListener {

                        db.collection("Jugadores")
                            .document(dniJugador).update(
                                hashMapOf(
                                    "UrlFoto" to java.lang.String.valueOf(it),
                                ) as Map<String, Any>
                            ).addOnSuccessListener {
                                Toast.makeText(
                                    binding.root.context,
                                    "Guardado los datos con exito",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                }

            }
        }
    }

    fun validarFecha(fecha: String): Boolean {

        var validoFecha = false
        val fechaconsultada = fecha.split("/", "-")
        val dia: Int = fechaconsultada[0].toInt()
        val mes: Int = fechaconsultada[1].toInt()
        val anio: Int = fechaconsultada[2].toInt()

        if (dia < 1 || dia > 31) {
            validoFecha = true
        }
        if (mes < 1 || mes > 12) {
            validoFecha = true
        }
        if (mes === 2 && dia === 29 && anio % 400 === 0 || anio % 4 === 0 && anio % 100 !== 0) {
            validoFecha = true
        }
        return validoFecha
    }

    fun validarDNI(dni: String): Boolean {
        return (Arrays.binarySearch(INVALIDOS, dni) < 0
                && REGEXP.matcher(dni).matches()
                && dni[8] == DIGITO_CONTROL[dni.substring(0, 8).toInt() % 23])
    }

    fun llenarSpinner() {

        //Categoria
        val lista2 =
            mutableListOf<String>(
                "Senior",
                "Junior",
                "Cadete",
                "Infantil"
            )
        val adaptador2 =
            ArrayAdapter<String>(binding.root.context, R.layout.simple_spinner_item)
        adaptador2.addAll(lista2)
        binding.spinnerCJCategoria.adapter = adaptador2

        //Sexo
        val lista3 =
            mutableListOf<String>("Masculino", "Femenino")
        val adaptador3 =
            ArrayAdapter<String>(binding.root.context, R.layout.simple_spinner_item)
        adaptador3.addAll(lista3)
        binding.spinnerCJSexo.adapter = adaptador3
    }
}