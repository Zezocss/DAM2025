package com.example.appdam.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appdam.R
import com.example.appdam.entidades.Receita

class ReceitaAdapter(
    private var listaReceitas: List<Receita>,
    private val onItemClick: (Receita) -> Unit
) : RecyclerView.Adapter<ReceitaAdapter.ReceitaViewHolder>() {

    inner class ReceitaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFoto: ImageView = itemView.findViewById(R.id.ivFotoItem)
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTituloItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceitaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_receita, parent, false)
        return ReceitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReceitaViewHolder, position: Int) {
        val receita = listaReceitas[position]
        holder.tvTitulo.text = receita.titulo

        // Carregar a foto, se disponível
        if (!receita.fotourl.isNullOrEmpty()) { // Alterado para fotourl
            Glide.with(holder.itemView.context)
                .load(receita.fotourl)
                .into(holder.ivFoto)
        } else {
            holder.ivFoto.setImageResource(R.drawable.ic_launcher_background) // Placeholder
        }

        holder.itemView.setOnClickListener {
            onItemClick(receita)
        }
    }

    override fun getItemCount() = listaReceitas.size

    // Função para atualizar a lista no adapter e notificar
    fun atualizarLista(novaLista: List<Receita>) {
        listaReceitas = novaLista
        notifyDataSetChanged()
    }
}
