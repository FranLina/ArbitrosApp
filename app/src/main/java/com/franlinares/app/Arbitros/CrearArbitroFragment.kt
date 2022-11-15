package com.franlinares.app.Arbitros

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.franlinares.app.BaseFragment
import com.franlinares.app.R
import com.franlinares.app.databinding.FragmentCrearArbitroBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CrearArbitroFragment : BaseFragment() {

    private var _binding: FragmentCrearArbitroBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearArbitroBinding.inflate(inflater, container, false)

        binding.btnCACrear.setOnClickListener {
            if (binding.txtCAUsuario.text.toString() != "" && binding.txtCAContraseA.text.toString() != "") {
                db.collection("Mesa_oficial").document(binding.txtCAUsuario.text.toString()).get()
                    .addOnSuccessListener {
                        if (it.get("contraseña") != null) {
                            Toast.makeText(
                                binding.root.context,
                                "Ya existe un arbitro con este usuario",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            db.collection("Mesa_oficial")
                                .document(binding.txtCAUsuario.text.toString()).set(
                                    hashMapOf(
                                        "1vez" to true,
                                        "contraseña" to binding.txtCAContraseA.text.toString(),
                                        "admin" to binding.switchAdmin.isChecked
                                    ) as Map<String, Any>
                                ).addOnSuccessListener {
                                    Toast.makeText(
                                        binding.root.context,
                                        "Creado con exito",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }.addOnFailureListener { exception ->
                                    Log.w(ContentValues.TAG, "Error setting documents.", exception)
                                }
                        }
                    }
            } else {
                Toast.makeText(
                    binding.root.context,
                    "Rellene los campos",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        return binding.root
    }

}