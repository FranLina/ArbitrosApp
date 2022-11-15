package com.franlinares.app

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.franlinares.app.Estadisticas.ElegirLigaFragment
import com.franlinares.app.Estadisticas.MainActivity3
import com.franlinares.app.admin.MainActivity2
import com.franlinares.app.databinding.ActivityLoginBinding
import com.franlinares.app.user.MainActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val usuario = prefs.getString("usuario", "")
        val contraseña = prefs.getString("contraseña", "")

        if (usuario.toString() != "" && contraseña.toString() != "") {
            binding.textUsuario.setText(usuario.toString())
            binding.textContraseA.setText(contraseña.toString())

            db.collection("Mesa_oficial").document(binding.textUsuario.text.toString())
                .get().addOnSuccessListener {
                    if (it.get("contraseña") == binding.textContraseA.text.toString() as String?) {
                        if (it.get("admin") == true as Boolean?) {

                            val intent = Intent(this, MainActivity2::class.java)
                            Toast.makeText(this, "Inicio sesion correctamente", Toast.LENGTH_LONG)
                                .show()
                            startActivity(intent)
                        } else {

                            val intent = Intent(this, MainActivity::class.java)
                            Toast.makeText(this, "Inicio sesion correctamente", Toast.LENGTH_LONG)
                                .show()
                            startActivity(intent)

                        }

                    } else {
                        Toast.makeText(
                            this,
                            "El usuario o contraseña estan mal, intentelo de nuevo",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }


        }

        binding.txtInvitado.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

        binding.btnEntrar.setOnClickListener() {
            db.collection("Mesa_oficial").document(binding.textUsuario.text.toString())
                .get().addOnSuccessListener {
                    if (it.get("contraseña") == binding.textContraseA.text.toString() as String?) {
                        if (it.get("admin") == true as Boolean?) {
                            if (it.get("1vez") == true as Boolean?) {
                                val intent = Intent(this, CambioPasswordActivity::class.java)
                                intent.putExtra(
                                    "usuario",
                                    binding.textUsuario.text.toString() as String?
                                )
                                intent.putExtra(
                                    "contraseña",
                                    binding.textContraseA.text.toString() as String?
                                )
                                startActivity(intent)
                            } else {
                                val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                                val editor = prefs.edit()
                                editor.putString("usuario", binding.textUsuario.text.toString())
                                editor.putString(
                                    "contraseña",
                                    binding.textContraseA.text.toString()
                                )
                                editor.apply()
                                val intent = Intent(this, MainActivity2::class.java)
                                Toast.makeText(
                                    this,
                                    "Inicio sesion correctamente",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                startActivity(intent)
                            }

                        } else {
                            if (it.get("1vez") == true as Boolean?) {
                                val intent = Intent(this, CambioPasswordActivity::class.java)
                                intent.putExtra(
                                    "usuario",
                                    binding.textUsuario.text.toString() as String?
                                )
                                intent.putExtra(
                                    "contraseña",
                                    binding.textContraseA.text.toString() as String?
                                )
                                startActivity(intent)
                            } else {
                                val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                                val editor = prefs.edit()
                                editor.putString("usuario", binding.textUsuario.text.toString())
                                editor.putString(
                                    "contraseña",
                                    binding.textContraseA.text.toString()
                                )
                                editor.apply()
                                val intent = Intent(this, MainActivity::class.java)
                                Toast.makeText(
                                    this,
                                    "Inicio sesion correctamente",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                startActivity(intent)
                            }
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "El usuario o contraseña estan mal, intentelo de nuevo",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "El usuario no existe, intentelo de nuevo",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.w(TAG, "Error getting documents.", exception)
                }

        }

    }
}