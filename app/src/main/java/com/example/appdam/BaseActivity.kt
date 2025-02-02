package com.example.appdam


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.appdam.receitasuser.ListaReceitaActivity
import com.example.appdam.utils.SharedPreferencesHelper
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

    // Configuração do menu lateral
    protected fun setupDrawer(layoutId: Int) {
        setContentView(layoutId)

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)

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


        val headerView = navView.getHeaderView(0)
        val tvUserName = headerView.findViewById<TextView>(R.id.user_name)
        // Recuperar o nome do usuário usando o SharedPreferencesHelper
        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        val userName = sharedPreferencesHelper.getUserName()

        // Atualizar o texto no cabeçalho
        if (!userName.isNullOrEmpty()) {
            tvUserName.text = "Bem-vindo, $userName"
        } else {
            tvUserName.text = "Bem-vindo!"
        }


        // Configuração do menu lateral
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_receitas -> {
                    startActivity(Intent(this, ReceitasActivity::class.java))
                }
                R.id.nav_suasreceitas -> {
                    startActivity(Intent(this, ListaReceitaActivity::class.java))
                }
                R.id.nav_info -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                }
                R.id.nav_logout -> {
                    val sharedPreferencesHelper = SharedPreferencesHelper(this)

// Remover o token e o nome do utilizador
                    sharedPreferencesHelper.clearToken()
                    sharedPreferencesHelper.saveUserName("") // Opcional: limpar também o nome do utilizador

                    Toast.makeText(this, "Logout Realizado com Sucesso", Toast.LENGTH_SHORT).show()

// Redirecionar para a tela de login e impedir que volte ao ReceitasActivity mesmo que tente voltar
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
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

