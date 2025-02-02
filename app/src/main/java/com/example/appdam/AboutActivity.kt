package com.example.appdam

import android.os.Bundle
import androidx.core.view.GravityCompat
import android.widget.ImageButton
import com.example.appdam.BaseActivity
import com.example.appdam.R

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configura o layout e o menu lateral
        setupDrawer(R.layout.activity_about)

        // Configura o botão para abrir o menu lateral
        val openMenuButton: ImageButton = findViewById(R.id.open_menu_button)
        openMenuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}
