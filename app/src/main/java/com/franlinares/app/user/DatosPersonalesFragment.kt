package com.franlinares.app.user

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
import android.widget.Toast
import com.franlinares.app.BaseFragment
import com.franlinares.app.databinding.FragmentDatosPersonalesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DatosPersonalesFragment : BaseFragment() {

    private var _binding: FragmentDatosPersonalesBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private val File = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDatosPersonalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val usuario = prefs.getString("usuario", "")

        db.collection("Mesa_oficial").document(usuario.toString())
            .get().addOnSuccessListener {
                binding.etDPNombre.setText(it.get("nombre").toString())
                binding.etDPApellido1.setText(it.get("apellido1").toString())
                binding.etDPApellido2.setText(it.get("apellido2").toString())
                binding.etDPUbicacion.setText(it.get("direccion").toString())
                binding.etDPPoblacion.setText(it.get("poblacion").toString())
                binding.etDPProvincia.setText(it.get("provincia").toString())
                binding.etDPCorreo.setText(it.get("correo").toString())
                binding.etDPTelefono.setText(it.get("telefono").toString())
            }.addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

        binding.btnDPCargar.setOnClickListener {
            fileUpload()
        }

        binding.btnDPEnviar.setOnClickListener {
            db.collection("Mesa_oficial")
                .document(usuario.toString()).update(
                    hashMapOf(
                        "apellido1" to binding.etDPApellido1.text.toString(),
                        "apellido2" to binding.etDPApellido2.text.toString(),
                        "nombre" to binding.etDPNombre.text.toString(),
                        "correo" to binding.etDPCorreo.text.toString(),
                        "poblacion" to binding.etDPPoblacion.text.toString(),
                        "provincia" to binding.etDPProvincia.text.toString(),
                        "telefono" to binding.etDPTelefono.text.toString(),
                        "direccion" to binding.etDPUbicacion.text.toString()
                    ) as Map<String, Any>
                ).addOnSuccessListener {
                    Toast.makeText(
                        binding.root.context,
                        "Guardado los datos con exito",
                        Toast.LENGTH_LONG
                    )
                        .show()
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

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val usuario = prefs.getString("usuario", "")

        if (requestCode == File) {
            if (resultCode == Activity.RESULT_OK) {
                val FileUri = data!!.data
                val Folder: StorageReference =
                    FirebaseStorage.getInstance().getReference().child("Mesa_oficial")
                val file_name: StorageReference =
                    Folder.child(usuario.toString() + FileUri!!.lastPathSegment)
                file_name.putFile(FileUri).addOnSuccessListener {
                    file_name.getDownloadUrl().addOnSuccessListener {

                        db.collection("Mesa_oficial")
                            .document(usuario.toString()).update(
                                hashMapOf(
                                    "UrlFoto" to java.lang.String.valueOf(it),
                                ) as Map<String, Any>
                            ).addOnSuccessListener {
                                Toast.makeText(
                                    binding.root.context,
                                    "Se subió correctamente",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}