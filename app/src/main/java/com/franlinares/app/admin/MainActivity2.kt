package com.franlinares.app.admin

import android.content.ContentValues
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.franlinares.app.R
import com.franlinares.app.databinding.ActivityMain2Binding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class MainActivity2 : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMain2Binding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.appBarMain2.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout2
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main2)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragmentAdmin,
                R.id.arbitrosFragment,
                R.id.jugadoresFragment,
                R.id.equiposFragment,
                R.id.ligasFragment,
                R.id.designacionPartidoFragment,
                R.id.datosPersonalesFragment2,
                R.id.configuracionFragment2
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val usuario = prefs.getString("usuario", "")

        db.collection("Mesa_oficial").document(usuario.toString())
            .get().addOnSuccessListener {

                navView.getHeaderView(0).findViewById<TextView>(R.id.tvM2NombreApellidos).setText(
                    it.get("nombre").toString() + " " + it.get("apellido1")
                        .toString() + " " + it.get("apellido2").toString()
                )
                navView.getHeaderView(0).findViewById<TextView>(R.id.tvM2Email).setText(
                    it.get("correo").toString()
                )
                if (it.get("UrlFoto").toString() != "") {
                    Picasso.get()
                        .load(it.get("UrlFoto").toString())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(navView.getHeaderView(0).findViewById<ImageView>(R.id.imageM2Usuario))
                }
            }.addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_drawer2, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main2)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}