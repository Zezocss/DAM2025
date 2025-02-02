package com.example.appdam



import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.appdam.entidades.PratoResponse
import com.example.appdam.interfaces.GetDataService
import com.example.appdam.retrofitclient.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : BaseActivity() {


    private lateinit var tvInstructions: TextView
    private lateinit var imgItem: com.makeramen.roundedimageview.RoundedImageView
    private lateinit var tvCategory: TextView
    private lateinit var tvIngredients: TextView
    private lateinit var imgToolbarBtnBack: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details)

        tvInstructions = findViewById<TextView>(R.id.tvInstructions)
        imgItem = findViewById<com.makeramen.roundedimageview.RoundedImageView>(R.id.imgItem)
        tvCategory = findViewById<TextView>(R.id.tvCategory)
        tvIngredients = findViewById<TextView>(R.id.tvIngredients)
        imgToolbarBtnBack = findViewById<ImageButton>(R.id.imgToolbarBtnBack)


        val id = intent.getStringExtra("id")
        getSpecificItem(id!!)

        imgToolbarBtnBack.setOnClickListener {
            finish() // Voltar para a atividade anterior

        }

    }

    // Vai buscar os detalhes do prato específico na API
    private fun getSpecificItem(id:String) {
        val service = RetrofitClient.retrofitInstance!!.create(GetDataService::class.java)
        val call = service.getSpecificItem(id)
        call.enqueue(object : Callback<PratoResponse> {
            override fun onFailure(call: Call<PratoResponse>, t: Throwable) {

                Toast.makeText(this@DetailActivity, "Algo Correu mal", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(
                call: Call<PratoResponse>,
                response: Response<PratoResponse>
            ) {
// Carrega a imagem com o Glide
                Glide.with(this@DetailActivity).load(response.body()!!.meals[0].strMealThumb).into(imgItem)



                tvCategory.text = response.body()!!.meals[0].strMeal
                tvIngredients.text = response.body()!!.meals[0].strMeal


                // Monta a lista de ingredientes
                var ingredient = "${response.body()!!.meals[0].strIngredient1}      ${response.body()!!.meals[0].strMeasure1}\n" +
                        "${response.body()!!.meals[0].strIngredient2}      ${response.body()!!.meals[0].strMeasure2}\n" +
                        "${response.body()!!.meals[0].strIngredient3}      ${response.body()!!.meals[0].strMeasure3}\n" +
                        "${response.body()!!.meals[0].strIngredient4}      ${response.body()!!.meals[0].strMeasure4}\n" +
                        "${response.body()!!.meals[0].strIngredient5}      ${response.body()!!.meals[0].strMeasure5}\n" +
                        "${response.body()!!.meals[0].strIngredient6}      ${response.body()!!.meals[0].strMeasure6}\n" +
                        "${response.body()!!.meals[0].strIngredient7}      ${response.body()!!.meals[0].strMeasure7}\n" +
                        "${response.body()!!.meals[0].strIngredient8}      ${response.body()!!.meals[0].strMeasure8}\n" +
                        "${response.body()!!.meals[0].strIngredient9}      ${response.body()!!.meals[0].strMeasure9}\n" +
                        "${response.body()!!.meals[0].strIngredient10}      ${response.body()!!.meals[0].strMeasure10}\n" +
                        "${response.body()!!.meals[0].strIngredient11}      ${response.body()!!.meals[0].strMeasure11}\n" +
                        "${response.body()!!.meals[0].strIngredient12}      ${response.body()!!.meals[0].strMeasure12}\n" +
                        "${response.body()!!.meals[0].strIngredient13}      ${response.body()!!.meals[0].strMeasure13}\n" +
                        "${response.body()!!.meals[0].strIngredient14}      ${response.body()!!.meals[0].strMeasure14}\n" +
                        "${response.body()!!.meals[0].strIngredient15}      ${response.body()!!.meals[0].strMeasure15}\n" +
                        "${response.body()!!.meals[0].strIngredient16}      ${response.body()!!.meals[0].strMeasure16}\n" +
                        "${response.body()!!.meals[0].strIngredient17}      ${response.body()!!.meals[0].strMeasure17}\n" +
                        "${response.body()!!.meals[0].strIngredient18}      ${response.body()!!.meals[0].strMeasure18}\n" +
                        "${response.body()!!.meals[0].strIngredient19}      ${response.body()!!.meals[0].strMeasure19}\n" +
                        "${response.body()!!.meals[0].strIngredient20}      ${response.body()!!.meals[0].strMeasure20}\n"


                tvIngredients.text = ingredient
                tvInstructions.text = response.body()!!.meals[0].strInstructions


            }

        })
    }


}