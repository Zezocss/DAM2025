package com.example.appdam


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


open class BaseActivity : AppCompatActivity(), CoroutineScope {




    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    protected lateinit var drawerLayout: DrawerLayout // Alterado de private para protecte
    private lateinit var navView: NavigationView
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    // Configuração do menu lateral (chamado pelas subclasses)
    protected fun setupDrawer(layoutId: Int) {
        setContentView(layoutId)

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)

        // Configurar o botão "hambúrguer"
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Mostrar o botão do menu na ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Configuração do menu lateral
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_receitas -> {
                    startActivity(Intent(this, ReceitasActivity::class.java))
                }
                R.id.nav_comentarios -> {
                    startActivity(Intent(this, ReceitasActivity::class.java))
                }
                R.id.nav_info -> {
                    startActivity(Intent(this, ReceitasActivity::class.java))
                }
                R.id.nav_logout -> {
                    val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    sharedPref.edit().clear().apply()
                    Toast.makeText(this, "Logout realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    // Permitir que o botão "hambúrguer" controle o menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}

