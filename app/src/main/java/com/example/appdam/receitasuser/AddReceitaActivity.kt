package com.example.appdam.receitasuser

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.appdam.R
import com.example.appdam.api.RetrofitUser
import com.example.appdam.receitasuser.add.ReceitaRequest
import com.example.appdam.utils.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class AddReceitaActivity : AppCompatActivity() {

    private lateinit var etTitulo: EditText
    private lateinit var etIngredientes: EditText
    private lateinit var etPreparo: EditText
    private lateinit var btnSalvarReceita: Button
    private lateinit var ivFotoReceita: ImageView
    private lateinit var btnAbrirCamera: Button
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private var fotoBase64: String? = null // Armazena a imagem codificada

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA_PERMISSION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_receita)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        etTitulo = findViewById(R.id.etTitulo)
        etIngredientes = findViewById(R.id.etIngredientes)
        etPreparo = findViewById(R.id.etPreparo)
        btnSalvarReceita = findViewById(R.id.btnSalvarReceita)
        ivFotoReceita = findViewById(R.id.ivFotoReceita)
        btnAbrirCamera = findViewById(R.id.btnTirarFoto)

        val btnBack: ImageButton = findViewById(R.id.imgToolbarBtnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, ListaReceitaActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnAbrirCamera.setOnClickListener {
            verificarPermissaoCamera()
        }

        btnSalvarReceita.setOnClickListener {
            salvarReceita()
        }
    }

    private fun verificarPermissaoCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            abrirCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamera()
            } else {
                Toast.makeText(this, "Permissão de câmera negada!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun abrirCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            ivFotoReceita.setImageBitmap(imageBitmap)
            fotoBase64 = bitmapParaBase64(imageBitmap)
        }
    }

    private fun bitmapParaBase64(bitmap: Bitmap): String {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 600, 600, true) // Reduz tamanho
        val byteArrayOutputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream) // Compressão 50%
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP) // Remove quebras de linha
    }


    private fun salvarReceita() {
        val titulo = etTitulo.text.toString()
        val ingredientes = etIngredientes.text.toString()
        val preparo = etPreparo.text.toString()
        val username = sharedPreferencesHelper.getUserName()

        if (username.isNullOrEmpty()) {
            Toast.makeText(this, "Erro: nome do utilizador não encontrado!", Toast.LENGTH_SHORT).show()
            return
        }

        if (!fotoBase64.isNullOrEmpty()) {
            val base64Size = fotoBase64!!.length / 1024 // Converte para KB
            println("Tamanho da imagem Base64: ${base64Size} KB")
            if (base64Size > 500) { // Limite arbitrário de 500 KB
                Toast.makeText(this, "Imagem muito grande! Reduza o tamanho.", Toast.LENGTH_LONG).show()
                return
            }
        }

        val receitaRequest = ReceitaRequest(
            titulo = titulo,
            fotourl = fotoBase64 ?: "", // Agora estamos enviando a imagem codificada
            ingredientes = ingredientes,
            preparo = preparo,
            username = username
        )

        RetrofitUser.instance.addReceita(receitaRequest)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddReceitaActivity, "Receita adicionada com sucesso!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(this@AddReceitaActivity, "Erro ao guardar receita: $errorBody", Toast.LENGTH_LONG).show()
                        println("Erro ao salvar receita: $errorBody")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@AddReceitaActivity, "Erro de conexão: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}