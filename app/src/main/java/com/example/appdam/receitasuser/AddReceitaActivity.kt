package com.example.appdam.receitasuser

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.appdam.R
import com.example.appdam.receitasuser.RetrofitUser
import com.example.appdam.receitasuser.add.ReceitaRequest
import com.example.appdam.receitasuser.add.ResponseAddReceitas
import com.example.appdam.receitasuser.UserApiService
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReceitaActivity : AppCompatActivity() {

    private lateinit var etTitulo: EditText
    private lateinit var etIngredientes: EditText
    private lateinit var etPreparo: EditText
    private lateinit var ivFotoReceita: ImageView
    private lateinit var btnTirarFoto: Button
    private lateinit var btnSalvarReceita: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_receita)

        // Referenciando os elementos do layout
        etTitulo = findViewById(R.id.etTitulo)
        etIngredientes = findViewById(R.id.etIngredientes)
        etPreparo = findViewById(R.id.etPreparo)
        ivFotoReceita = findViewById(R.id.ivFotoReceita)
        btnTirarFoto = findViewById(R.id.btnTirarFoto)
        btnSalvarReceita = findViewById(R.id.btnSalvarReceita)

        // Ação do botão "Tirar Foto" (aqui você pode configurar a câmera, por exemplo)
        btnTirarFoto.setOnClickListener {
            // Aqui você pode adicionar o código para tirar a foto
        }

        // Ação do botão "Salvar"
        btnSalvarReceita.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val ingredientes = etIngredientes.text.toString()
            val preparo = etPreparo.text.toString()

            // Criar o objeto de requisição (ReceitaRequest)
            val receitaRequest = ReceitaRequest(
                fotourl = "", // Aqui você pode adicionar a URI da foto, caso tenha implementado
                ingredientes = ingredientes,
                preparo = preparo,
                titulo = titulo,
                user_id = "123" // Exemplo de ID de usuário, substitua conforme necessário
            )

            // Chamar o método para salvar a receita
            salvarReceita(receitaRequest)
        }
    }
    private fun salvarReceita(receitaRequest: ReceitaRequest) {
        val apiService = RetrofitUser.instance

        // Encapsula o objeto ReceitaRequest dentro de um mapa com a chave "folha1"
        val requestBody = mapOf("folha1" to receitaRequest)

        apiService.addReceita(requestBody).enqueue(object : Callback<ResponseAddReceitas> {
            override fun onResponse(call: Call<ResponseAddReceitas>, response: Response<ResponseAddReceitas>) {
                if (response.isSuccessful) {
                    Snackbar.make(
                        findViewById(R.id.scrollAddReceita),
                        "Receita adicionada com sucesso!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    Snackbar.make(
                        findViewById(R.id.scrollAddReceita),
                        "Erro ao salvar a receita. Tente novamente.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseAddReceitas>, t: Throwable) {
                Snackbar.make(
                    findViewById(R.id.scrollAddReceita),
                    "Erro ao conectar com o servidor.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
    }


}
