package com.franlinares.app.Estadisticas.ListViewEstadisticas

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.isVisible
import com.franlinares.app.R
import java.time.LocalTime

class AdaptadorMinuto(private val mcontext: Context, private val listaMinuto: List<MinutoAMinuto>) :
    ArrayAdapter<MinutoAMinuto>(mcontext, 0, listaMinuto) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mcontext).inflate(R.layout.minutoaminuto, parent, false)

        val minuto = listaMinuto[position]

        if (minuto.tipoFrase == "1") {
            if (minuto.equipo == "Local") {
                layout.findViewById<TextView>(R.id.txtLVMFrase).setText(minuto.frase)
                layout.findViewById<TextView>(R.id.txtLVMFrase).gravity = Gravity.LEFT
                layout.findViewById<TextView>(R.id.txtLVM)
                    .setText("#" + minuto.dorsal + ", p" + minuto.cuarto + ", " + minuto.tiempo + "    " + minuto.resultado)
                layout.findViewById<TextView>(R.id.txtLVM).gravity = Gravity.LEFT
                layout.findViewById<View>(R.id.LineaSeparadora)
                    .setBackgroundColor(Color.parseColor("#FF3D00"))
            } else {
                layout.findViewById<TextView>(R.id.txtLVMFrase).setText(minuto.frase)
                layout.findViewById<TextView>(R.id.txtLVMFrase).gravity = Gravity.RIGHT
                layout.findViewById<TextView>(R.id.txtLVM)
                    .setText("#" + minuto.dorsal + ", p" + minuto.cuarto + ", " + minuto.tiempo + "    " + minuto.resultado)
                layout.findViewById<TextView>(R.id.txtLVM).gravity = Gravity.RIGHT
                layout.findViewById<View>(R.id.LineaSeparadora)
                    .setBackgroundColor(Color.parseColor("#00B0FF"))
            }
        } else if (minuto.tipoFrase == "2") {
            layout.findViewById<TextView>(R.id.txtLVMFrase)
                .setText(minuto.frase + " " + minuto.cuarto)
            layout.findViewById<TextView>(R.id.txtLVMFrase)
                .setBackgroundColor(Color.parseColor("#FF4CAF50"))
            layout.findViewById<TextView>(R.id.txtLVMFrase).setTextColor(Color.WHITE)
            layout.findViewById<TextView>(R.id.txtLVMFrase).gravity = Gravity.CENTER
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layout.findViewById<TextView>(R.id.txtLVM).setText("")
                layout.findViewById<TextView>(R.id.txtLVM).gravity = Gravity.CENTER
                layout.findViewById<TextView>(R.id.txtLVM)
                    .setBackgroundColor(Color.parseColor("#FF4CAF50"))
                layout.findViewById<TextView>(R.id.txtLVM).setTextColor(Color.WHITE)
            }
            layout.findViewById<View>(R.id.LineaSeparadora).isVisible = false
        } else if (minuto.tipoFrase == "3") {
            if (minuto.equipo == "Local") {
                layout.findViewById<TextView>(R.id.txtLVMFrase)
                    .setText(minuto.frase + "#" + minuto.dorsal)
                layout.findViewById<TextView>(R.id.txtLVMFrase).gravity = Gravity.LEFT
                layout.findViewById<TextView>(R.id.txtLVM)
                    .setText("p" + minuto.cuarto + ", " + minuto.tiempo)
                layout.findViewById<TextView>(R.id.txtLVM).gravity = Gravity.LEFT
                layout.findViewById<View>(R.id.LineaSeparadora)
                    .setBackgroundColor(Color.parseColor("#FF3D00"))
            } else {
                layout.findViewById<TextView>(R.id.txtLVMFrase)
                    .setText(minuto.frase + "#" + minuto.dorsal)
                layout.findViewById<TextView>(R.id.txtLVMFrase).gravity = Gravity.RIGHT
                layout.findViewById<TextView>(R.id.txtLVM)
                    .setText("p" + minuto.cuarto + ", " + minuto.tiempo)
                layout.findViewById<TextView>(R.id.txtLVM).gravity = Gravity.RIGHT
                layout.findViewById<View>(R.id.LineaSeparadora)
                    .setBackgroundColor(Color.parseColor("#00B0FF"))
            }
        } else if (minuto.tipoFrase == "4") {
            layout.findViewById<TextView>(R.id.txtLVMFrase)
                .setText(minuto.frase + " " + minuto.cuarto)
            layout.findViewById<TextView>(R.id.txtLVMFrase)
                .setBackgroundColor(Color.parseColor("#3A3A3A"))
            layout.findViewById<TextView>(R.id.txtLVMFrase).setTextColor(Color.WHITE)
            layout.findViewById<TextView>(R.id.txtLVMFrase).gravity = Gravity.CENTER
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layout.findViewById<TextView>(R.id.txtLVM).setText("")
                layout.findViewById<TextView>(R.id.txtLVM).gravity = Gravity.CENTER
                layout.findViewById<TextView>(R.id.txtLVM)
                    .setBackgroundColor(Color.parseColor("#3A3A3A"))
                layout.findViewById<TextView>(R.id.txtLVM).setTextColor(Color.WHITE)
            }
            layout.findViewById<View>(R.id.LineaSeparadora).isVisible = false
        }



        return layout
    }
}