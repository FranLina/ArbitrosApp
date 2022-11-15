package com.franlinares.app.Liga.ListViewLigas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.franlinares.app.R

class AdaptadorLiga(private val mcontext: Context, private val listaLiga: List<Ligas>) :
    ArrayAdapter<Ligas>(mcontext, 0, listaLiga) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mcontext).inflate(R.layout.liga_item, parent, false)

        val liga = listaLiga[position]

        layout.findViewById<TextView>(R.id.txtLVNombreLiga).setText(liga.nombre)
        layout.findViewById<TextView>(R.id.txtLVCategoria).setText(liga.categoria)
        layout.findViewById<TextView>(R.id.txtLVSexo).setText(liga.sexo)
        layout.findViewById<TextView>(R.id.txtLVTemporada).setText(liga.temporada)
        if (liga.activo == true) {
            layout.findViewById<ImageView>(R.id.imageLVActivo).setImageResource(R.drawable.checked)
        } else {
            layout.findViewById<ImageView>(R.id.imageLVActivo).setImageResource(R.drawable.cancelar)
        }

        return layout
    }
}