package com.franlinares.app.Estadisticas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.franlinares.app.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CargaPartidoActivity : AppCompatActivity() {

    var tabTitle = arrayOf("En Vivo", "Estadísticas", "Mejores Jugadores")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carga_partido)

        var pager = findViewById<ViewPager2>(R.id.vPVistas)
        var t1 = findViewById<TabLayout>(R.id.tabLayout)

        pager.adapter = MyAdapter(supportFragmentManager, lifecycle)
        TabLayoutMediator(t1, pager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()
    }
}