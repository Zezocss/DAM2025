package com.example.appdam.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appdam.R
import com.example.appdam.databinding.Maindesign1Binding
import com.example.appdam.databinding.Maindesign2Binding
import com.example.appdam.entidades.Receitas


class SubCategoryAdapter: RecyclerView.Adapter<SubCategoryAdapter.RecipeViewHolder>() {


    var arrSubCategory = ArrayList<Receitas>() //Maindesign1Binding é gerado automaticamente pelo maindesign1xml
    class RecipeViewHolder(val binding: Maindesign2Binding) : RecyclerView.ViewHolder(binding.root) {

    }

    fun setData(arrData : List<Receitas>){
        arrSubCategory = arrData as ArrayList<Receitas>
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = Maindesign2Binding.inflate(LayoutInflater.from(parent.context),parent,false)

        return RecipeViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return arrSubCategory.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = arrSubCategory[position] // Obtemos o item atual da lista
        holder.binding.tvDishName1.text = recipe.dishName //

    }
}



