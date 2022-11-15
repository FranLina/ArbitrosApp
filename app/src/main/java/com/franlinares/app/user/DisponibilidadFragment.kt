package com.franlinares.app.user

import android.content.ContentValues
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.franlinares.app.BaseFragment
import com.franlinares.app.databinding.FragmentDisponibilidadBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DisponibilidadFragment : BaseFragment() {

    private var _binding: FragmentDisponibilidadBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDisponibilidadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val usuario = prefs.getString("usuario", "")

        //Recuperamos los valores de disponibilidad guardados.
        db.collection("disponibilidad").document(usuario.toString())
            .get().addOnSuccessListener {
                binding.ViernesM.isChecked = it.get("ViernesM") as Boolean
                binding.ViernesT.isChecked = it.get("ViernesT") as Boolean
                binding.SabadoM.isChecked = it.get("SabadoM") as Boolean
                binding.SabadoT.isChecked = it.get("SabadoT") as Boolean
                binding.DomingoM.isChecked = it.get("DomingoM") as Boolean
                binding.DomingoT.isChecked = it.get("DomingoT") as Boolean
                binding.LunesM.isChecked = it.get("LunesM") as Boolean
                binding.LunesT.isChecked = it.get("LunesT") as Boolean
                binding.MartesM.isChecked = it.get("MartesM") as Boolean
                binding.MartesT.isChecked = it.get("MartesT") as Boolean
                binding.MiercolesM.isChecked = it.get("MiercolesM") as Boolean
                binding.MiercolesT.isChecked = it.get("MiercolesT") as Boolean
                binding.JuevesM.isChecked = it.get("JuevesM") as Boolean
                binding.JuevesT.isChecked = it.get("JuevesT") as Boolean
                binding.SwitchCoche.isChecked = it.get("Coche") as Boolean
                binding.txtObservacion.setText(it.get("Observacion").toString())
            }.addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

        binding.btnGuardar.setOnClickListener {
            db.collection("disponibilidad")
                .document(usuario.toString()).update(
                    hashMapOf(
                        "ViernesM" to binding.ViernesM.isChecked,
                        "ViernesT" to binding.ViernesT.isChecked,
                        "SabadoM" to binding.SabadoM.isChecked,
                        "SabadoT" to binding.SabadoT.isChecked,
                        "DomingoM" to binding.DomingoM.isChecked,
                        "DomingoT" to binding.DomingoT.isChecked,
                        "LunesM" to binding.LunesM.isChecked,
                        "LunesT" to binding.LunesT.isChecked,
                        "MartesM" to binding.MartesM.isChecked,
                        "MartesT" to binding.MartesT.isChecked,
                        "MiercolesM" to binding.MiercolesM.isChecked,
                        "MiercolesT" to binding.MiercolesT.isChecked,
                        "JuevesM" to binding.JuevesM.isChecked,
                        "JuevesT" to binding.JuevesT.isChecked,
                        "Coche" to binding.SwitchCoche.isChecked,
                        "Observacion" to binding.txtObservacion.text.toString()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}