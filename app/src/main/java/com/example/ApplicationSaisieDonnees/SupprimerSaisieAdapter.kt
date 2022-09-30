package com.example.ApplicationSaisieDonnees

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SupprimerSaisieAdapter (
        var fichiers: List<ListeTemplate>
) : RecyclerView.Adapter<SupprimerSaisieAdapter.SupprimerViewHolder>() {

    inner class SupprimerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupprimerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_template, parent, false)
        return SupprimerViewHolder(view)
    }

    override fun onBindViewHolder(holder: SupprimerViewHolder, position: Int) {
        holder.itemView.apply {
            if (position%2 == 0){
                this.setBackgroundColor(Color.LTGRAY)
            }
            else{
                this.setBackgroundColor(Color.WHITE)
            }
            findViewById<TextView>(R.id.tvFichiers).text = fichiers[position].titre
            findViewById<CheckBox>(R.id.cbChoix).isChecked = fichiers[position].isChecked
        }
    }

    override fun getItemCount(): Int {
        return fichiers.size
    }
}