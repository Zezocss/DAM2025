package com.example.appdam.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appdam.R
import com.example.appdam.databinding.Maindesign1Binding
import com.example.appdam.databinding.Maindesign2Binding
import com.example.appdam.entidades.PratosItens
import com.example.appdam.entidades.Receitas


class SubCategoryAdapter: RecyclerView.Adapter<SubCategoryAdapter.RecipeViewHolder>() {
    var listener: SubCategoryAdapter.onItemClickListener? = null


    var ctx: Context? = null
    var arrSubCategory =
        ArrayList<PratosItens>() //Maindesign1Binding é gerado automaticamente pelo maindesign1xml

    class RecipeViewHolder(val binding: Maindesign2Binding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    fun setData(arrData: List<PratosItens>) {
        arrSubCategory = arrData as ArrayList<PratosItens>
    }

    fun setClicklistener(listener1: SubCategoryAdapter.onItemClickListener){
        listener = listener1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        ctx = parent.context
        val binding = Maindesign2Binding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecipeViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return arrSubCategory.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = arrSubCategory[position] // Obtemos o item atual da lista
        holder.binding.tvDishName1.text = recipe.strMeal //
        Glide.with(ctx!!).load(arrSubCategory[position].strMealThumb).into(holder.binding.imgDish1)

        holder.itemView.rootView.setOnClickListener {
            listener!!.onClicked(arrSubCategory[position].id)

        }
    }
        interface onItemClickListener {
            fun onClicked(id:Int)
        }
    }




