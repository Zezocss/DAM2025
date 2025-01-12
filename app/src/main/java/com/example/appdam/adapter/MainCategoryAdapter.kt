package com.example.appdam.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appdam.databinding.Maindesign1Binding
import com.example.appdam.entidades.CategoriaItens
import com.bumptech.glide.Glide




class MainCategoryAdapter: RecyclerView.Adapter<MainCategoryAdapter.RecipeViewHolder>() {

    var listener: onItemClickListener? = null
    var ctx: Context? = null
    var arrMainCategory = ArrayList<CategoriaItens>() //Maindesign1Binding é gerado automaticamente pelo maindesign1xml
    class RecipeViewHolder(val binding: Maindesign1Binding) : RecyclerView.ViewHolder(binding.root) {

    }

    fun setData(arrData: List<CategoriaItens>){
        arrMainCategory.clear()
        arrMainCategory.addAll(arrData)
        notifyDataSetChanged()
    }

    fun setClicklistener(listener1: onItemClickListener){
        listener = listener1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        ctx = parent.context
        val binding = Maindesign1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return arrMainCategory.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
            holder.binding.tvDishName.text = arrMainCategory[position].strCategory

        //glide para mostrar imagens
        Glide.with(ctx!!).load(arrMainCategory[position].strCategoryThumb).into(holder.binding.imgDish)
        holder.itemView.rootView.setOnClickListener {
            listener!!.onClicked(arrMainCategory[position].strCategory)
        }
        }

    interface onItemClickListener{
        fun onClicked(categoryName:String)
    }


    }




