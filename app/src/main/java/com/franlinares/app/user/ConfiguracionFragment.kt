package com.franlinares.app.user

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.franlinares.app.BaseFragment
import com.franlinares.app.databinding.FragmentConfiguracionBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ConfiguracionFragment : BaseFragment() {

    private var _binding: FragmentConfiguracionBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfiguracionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val usuario = prefs.getString("usuario", "")

        binding.btnCGuardar.setOnClickListener {
            val contraseña = binding.etCContraseA.text.toString()
            val Rcontraseña = binding.etCRepiteContraseA.text.toString()
            if (contraseña != "" && Rcontraseña != "") {
                if (contraseña == Rcontraseña) {
                    db.collection("Mesa_oficial")
                        .document(usuario.toString()).update(
                            hashMapOf(
                                "contraseña" to contraseña,
                            ) as Map<String, Any>
                        ).addOnSuccessListener {
                            val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
                            val editor = prefs.edit()
                            editor.putString("contraseña", contraseña)
                            editor.apply()
                            Toast.makeText(
                                binding.root.context,
                                "Cambio de contraseña con exito",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.etCContraseA.setText("")
                            binding.etCRepiteContraseA.setText("")
                        }
                } else {
                    Toast.makeText(
                        binding.root.context,
                        "Las contraseñas no coinciden, intentelo de nuevo",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            } else {
                Toast.makeText(
                    binding.root.context,
                    "Rellene los campos",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}