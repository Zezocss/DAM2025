package com.example.appdam.receitasuser

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import com.example.appdam.R
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.appdam.entidades.Receita
import com.example.appdam.receitasuser.RetrofitUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class EditReceitaActivity : AppCompatActivity() {

    private lateinit var etTituloEdit: EditText
    private lateinit var ivFotoEdit: ImageView
    private lateinit var btnTirarFotoEdit: Button
    private lateinit var etIngredientesEdit: EditText
    private lateinit var etPreparoEdit: EditText
    private lateinit var btnSalvarEdit: Button
    private lateinit var btnEliminar: Button

    private var receitaAtual: Receita? = null
    private var fotourl: String? = null // Atualizado para fotourl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_receita)

        etTituloEdit = findViewById(R.id.etTituloEdit)
        ivFotoEdit = findViewById(R.id.ivFotoReceitaEdit)
        btnTirarFotoEdit = findViewById(R.id.btnTirarFotoEdit)
        etIngredientesEdit = findViewById(R.id.etIngredientesEdit)
        etPreparoEdit = findViewById(R.id.etPreparoEdit)
        btnSalvarEdit = findViewById(R.id.btnSalvarEdit)
        btnEliminar = findViewById(R.id.btnEliminar)

        val idReceita = intent.getLongExtra("ID_RECEITA", -1)
        if (idReceita != -1L) {
            carregarReceita(idReceita)
        }

        btnSalvarEdit.setOnClickListener {
            atualizarReceitaNoSheety()
        }

        btnEliminar.setOnClickListener {
            eliminarReceitaNoSheety()
        }
    }

    private fun carregarReceita(idReceita: Long) {
        RetrofitUser.instance.getReceitas().enqueue(object : Callback<ResponseReceitas> {
            override fun onResponse(call: Call<ResponseReceitas>, response: Response<ResponseReceitas>) {
                if (response.isSuccessful) {
                    receitaAtual = response.body()?.folha1?.find { it.id == idReceita }
                    receitaAtual?.let {
                        etTituloEdit.setText(it.titulo)
                        etIngredientesEdit.setText(it.ingredientes)
                        etPreparoEdit.setText(it.preparo)
                        fotourl = it.fotourl
                        Glide.with(this@EditReceitaActivity)
                            .load(it.fotourl)
                            .into(ivFotoEdit)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseReceitas>, t: Throwable) {
                Toast.makeText(this@EditReceitaActivity, "Erro ao carregar receita", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun atualizarReceitaNoSheety() {
        val receita = receitaAtual ?: return

        val receitaAtualizada = Receita(
            id = receita.id,
            titulo = etTituloEdit.text.toString(),
            fotourl = fotourl,
            ingredientes = etIngredientesEdit.text.toString(),
            preparo = etPreparoEdit.text.toString(),
            userId = receita.userId ?: "0"
        )

        val requestBody = mapOf("folha1" to receitaAtualizada) // 🔥 Ajuste para a estrutura correta do Sheety

        RetrofitUser.instance.updateReceita(receita.id!!, requestBody).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditReceitaActivity, "Receita atualizada!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditReceitaActivity, "Erro ao atualizar receita", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@EditReceitaActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun eliminarReceitaNoSheety() {
        val receita = receitaAtual ?: return
        RetrofitUser.instance.deleteReceita(receita.id!!).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditReceitaActivity, "Receita excluída!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditReceitaActivity, "Erro ao excluir receita", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@EditReceitaActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
