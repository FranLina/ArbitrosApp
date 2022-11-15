package com.franlinares.app.Liga.ListViewLigas

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.franlinares.app.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class AdaptadorJornadas(private val mcontext: Context, private val listaJornada: List<Jornadas>) :
    ArrayAdapter<Jornadas>(mcontext, 0, listaJornada) {

    private val db = Firebase.firestore

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mcontext).inflate(R.layout.jornada_item, parent, false)

        val jornada = listaJornada[position]

        layout.findViewById<TextView>(R.id.txtLVEquipoLocal).setText(jornada.local)
        layout.findViewById<TextView>(R.id.txtLVEquipoVisitante).setText(jornada.visitante)
        layout.findViewById<TextView>(R.id.txtLVPolideportivo).setText(jornada.polideportivo)
        layout.findViewById<TextView>(R.id.txtLVFecha).setText(jornada.fecha)
        layout.findViewById<TextView>(R.id.txtLVHora).setText(jornada.hora)
        layout.findViewById<TextView>(R.id.txtLVJornada).setText("Jornada " + jornada.jornada)
        if (jornada.resultado != ""){
            layout.findViewById<TextView>(R.id.txtLVResultado).setText(jornada.estado)
            layout.findViewById<TextView>(R.id.txtLVResultado).setTextColor(Color.parseColor("#4CAF50"))
        } else{
            layout.findViewById<TextView>(R.id.txtLVResultado).setText(jornada.estado)
            layout.findViewById<TextView>(R.id.txtLVResultado).setTextColor(Color.BLACK)
        }


        db.collection("Equipos").document(jornada.local.toString()).get()
            .addOnSuccessListener {
                if (it.get("UrlFoto") != "") {
                    Picasso.get()
                        .load(it.get("UrlFoto").toString())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(layout.findViewById<ImageView>(R.id.imageLVLocal))
                }
            }.addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

        db.collection("Equipos").document(jornada.visitante.toString()).get()
            .addOnSuccessListener {
                if (it.get("UrlFoto") != "") {
                    Picasso.get()
                        .load(it.get("UrlFoto").toString())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(layout.findViewById<ImageView>(R.id.imageLVVisitante))
                }
            }.addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }


        return layout
    }
}