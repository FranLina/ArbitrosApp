package com.franlinares.app.Jugador.ListViewJugador

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.franlinares.app.R
import com.squareup.picasso.Picasso

class AdaptadorJugador(private val mcontext: Context, private val listaJugadores: List<Jugadores>) :
    ArrayAdapter<Jugadores>(mcontext, 0, listaJugadores) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mcontext).inflate(R.layout.jugador_item, parent, false)

        val jugador = listaJugadores[position]

        layout.findViewById<TextView>(R.id.txtLVJNombre)
            .setText("Nombre:  " + jugador.nombre)
        layout.findViewById<TextView>(R.id.txtLVJApellido1)
            .setText("Apellido1:  " + jugador.apellido1)
        layout.findViewById<TextView>(R.id.txtLVJApellido2)
            .setText("Apellido2:  " + jugador.apellido2)
        layout.findViewById<TextView>(R.id.txtLVJDorsal)
            .setText("Dorsal:  " + jugador.dorsal)
        layout.findViewById<TextView>(R.id.txtLVJDNI)
            .setText("DNI:  " + jugador.dni)
        layout.findViewById<TextView>(R.id.txtLVJCategoria)
            .setText("Categoria:  " + jugador.categoria)
        layout.findViewById<TextView>(R.id.txtLVJSexo)
            .setText("Sexo:  " + jugador.sexo)
        layout.findViewById<TextView>(R.id.txtLVJEquipo)
            .setText("Equipo:  " + jugador.equipo)

        if (jugador.foto != "") {
            Picasso.get()
                .load(jugador.foto)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(layout.findViewById<ImageView>(R.id.imageLVJJugador))
        }

        return layout
    }
}