package com.franlinares.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import com.franlinares.app.admin.MainActivity2
import com.franlinares.app.databinding.ActivityCambioPasswordBinding
import com.franlinares.app.user.MainActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CambioPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCambioPasswordBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCambioPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val usuario = bundle?.getString("usuario")
        val password = bundle?.getString("contraseña")

        binding.btnCambioContraseAEntrar.setOnClickListener() {
            if (binding.textCambioContraseA.text.toString() == binding.textCambioContraseAConfirmar.text.toString()) {
                if (binding.textCambioContraseA.text.toString() == "" && binding.textCambioContraseAConfirmar.text.toString() == "") {

                    db.collection("Mesa_oficial").document(usuario.toString()).update(
                        hashMapOf(
                            "1vez" to false,
                            "contraseña" to password.toString(),
                            "UrlFoto" to "",
                            "apellido1" to "",
                            "apellido2" to "",
                            "nombre" to "",
                            "correo" to "",
                            "poblacion" to "",
                            "provincia" to "",
                            "telefono" to "",
                            "direccion" to "",
                            "partidosAsignados" to listOf<String>()
                        ) as Map<String, Any>
                    ).addOnSuccessListener {
                        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                        val editor = prefs.edit()
                        editor.putString("usuario", usuario.toString())
                        editor.putString("contraseña", password.toString())
                        editor.apply()

                        db.collection("disponibilidad")
                            .document(usuario.toString()).set(
                                hashMapOf(
                                    "ViernesM" to false,
                                    "ViernesT" to false,
                                    "SabadoM" to false,
                                    "SabadoT" to false,
                                    "DomingoM" to false,
                                    "DomingoT" to false,
                                    "LunesM" to false,
                                    "LunesT" to false,
                                    "MartesM" to false,
                                    "MartesT" to false,
                                    "MiercolesM" to false,
                                    "MiercolesT" to false,
                                    "JuevesM" to false,
                                    "JuevesT" to false,
                                    "Coche" to false,
                                    "Observacion" to ""
                                ) as Map<String, Any>
                            ).addOnSuccessListener {
                                db.collection("Mesa_oficial").document(usuario.toString()).get()
                                    .addOnSuccessListener {
                                        if (it.get("admin") == true as Boolean?) {
                                            val intent = Intent(this, MainActivity2::class.java)
                                            startActivity(intent)
                                        } else {
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                        }
                                        Toast.makeText(
                                            this,
                                            "Inicio sesion correctamente",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                            }
                    }

                } else {
                    db.collection("Mesa_oficial").document(usuario.toString()).update(
                        hashMapOf(
                            "1vez" to false,
                            "contraseña" to binding.textCambioContraseA.text.toString(),
                            "UrlFoto" to "",
                            "apellido1" to "",
                            "apellido2" to "",
                            "nombre" to "",
                            "correo" to "",
                            "poblacion" to "",
                            "provincia" to "",
                            "telefono" to "",
                            "direccion" to "",
                            "partidosAsignados" to listOf<String>()
                        ) as Map<String, Any>
                    ).addOnSuccessListener {
                        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                        val editor = prefs.edit()
                        editor.putString("usuario", usuario.toString())
                        editor.putString("contraseña", binding.textCambioContraseA.text.toString())
                        editor.apply()

                        db.collection("disponibilidad")
                            .document(usuario.toString()).set(
                                hashMapOf(
                                    "ViernesM" to false,
                                    "ViernesT" to false,
                                    "SabadoM" to false,
                                    "SabadoT" to false,
                                    "DomingoM" to false,
                                    "DomingoT" to false,
                                    "LunesM" to false,
                                    "LunesT" to false,
                                    "MartesM" to false,
                                    "MartesT" to false,
                                    "MiercolesM" to false,
                                    "MiercolesT" to false,
                                    "JuevesM" to false,
                                    "JuevesT" to false,
                                    "Coche" to false,
                                    "Observacion" to ""
                                ) as Map<String, Any>
                            ).addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "Cambio de contraseña con exito",
                                    Toast.LENGTH_SHORT
                                ).show()
                                db.collection("Mesa_oficial").document(usuario.toString()).get()
                                    .addOnSuccessListener {
                                        if (it.get("admin") == true as Boolean?) {
                                            val intent = Intent(this, MainActivity2::class.java)
                                            startActivity(intent)
                                        } else {
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                        }
                                        Toast.makeText(
                                            this,
                                            "Inicio sesion correctamente",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                            }
                    }
                }

            } else {
                Toast.makeText(
                    this,
                    "Las contraseñas no coinciden, intentelo de nuevo",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }

    }
}