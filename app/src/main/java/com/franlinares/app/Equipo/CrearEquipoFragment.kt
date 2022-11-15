package com.franlinares.app.Equipo

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.franlinares.app.BaseFragment
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentCrearEquipoBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CrearEquipoFragment : BaseFragment() {

    private var _binding: FragmentCrearEquipoBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private val File = 1
    private var nombreEquipo = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearEquipoBinding.inflate(inflater, container, false)
        llenarSpinner(2021, 100)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCECrear.setOnClickListener {
            val nombre = binding.txtCENombreEquipo.text.toString()
            val categoria = binding.spinnerECategoria.selectedItem.toString()
            val sexo = binding.spinnerESexo.selectedItem.toString()
            val temporada = binding.spinnerETemporada.selectedItem.toString()
            val localidad = binding.txtCELocalidad.text.toString()
            val direccionPabellon = binding.txtCEDireccion.text.toString()
            val correo = binding.txtCECorreo.text.toString()
            val telefono = binding.txtCETelefono.text.toString()

            if (nombre != "" && categoria != "" && sexo != "" && temporada != "" && localidad != "" && direccionPabellon != "" && correo != "" && telefono != "") {
                nombreEquipo = generarNombre(nombre, categoria, sexo, temporada)
                db.collection("Equipos")
                    .document(nombreEquipo)
                    .get().addOnSuccessListener {
                        if (it.get("Nombre") != null) {
                            Toast.makeText(
                                binding.root.context,
                                "Ya existe un equipo con este nombre",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {

                            db.collection("Equipos")
                                .document(nombreEquipo).set(
                                    hashMapOf(
                                        "Nombre" to nombre,
                                        "UrlFoto" to "",
                                        "Categoria" to categoria,
                                        "Sexo" to sexo,
                                        "Temporada" to temporada,
                                        "Localidad" to localidad,
                                        "DireccionPabellon" to direccionPabellon,
                                        "Correo" to correo,
                                        "Telefono" to telefono,
                                        "Liga" to "",
                                        "Jugadores" to listOf<String>()
                                    ) as Map<String, Any>
                                ).addOnSuccessListener {
                                    fileUpload()
                                }.addOnFailureListener { exception ->
                                    Log.w(ContentValues.TAG, "Error setting documents.", exception)
                                }
                        }


                    }.addOnFailureListener { exception ->
                        Log.w(ContentValues.TAG, "Error getting documents.", exception)
                    }
            } else {
                Toast.makeText(
                    binding.root.context,
                    "Rellene los campos",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
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
                    FirebaseStorage.getInstance().getReference().child("Equipos")
                val file_name: StorageReference =
                    Folder.child(nombreEquipo + FileUri!!.lastPathSegment)
                file_name.putFile(FileUri).addOnSuccessListener {
                    file_name.getDownloadUrl().addOnSuccessListener {

                        db.collection("Equipos")
                            .document(nombreEquipo).update(
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

    fun generarNombre(nombre: String, Categoria: String, Sexo: String, Temporada: String): String {

        var nombreEquipo = ""

        val anio = Temporada.split("/")

        nombreEquipo =
            nombre + Categoria[0].toString().uppercase() + Sexo[0].toString().uppercase() + anio[0]

        return nombreEquipo
    }


    fun llenarSpinner(anioI: Int, cantidad: Int) {

        //AñoTemporada
        val lista = mutableListOf<String>()
        for (i in anioI..(anioI + cantidad)) {
            lista.add("" + i + "/" + (i + 1))
        }
        val adaptador =
            ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_item)
        adaptador.addAll(lista)
        binding.spinnerETemporada.adapter = adaptador

        //Categoria
        val lista2 =
            mutableListOf<String>(
                "Senior",
                "Junior",
                "Cadete",
                "Infantil"
            )
        val adaptador2 =
            ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_item)
        adaptador2.addAll(lista2)
        binding.spinnerECategoria.adapter = adaptador2

        //Sexo
        val lista3 =
            mutableListOf<String>("Masculino", "Femenino")
        val adaptador3 =
            ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_item)
        adaptador3.addAll(lista3)
        binding.spinnerESexo.adapter = adaptador3
    }

}