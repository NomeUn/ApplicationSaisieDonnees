package com.example.ApplicationSaisieDonnees

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SaisieAdapter(
        var criteres: List<Critere>,
        var bt: BtAdapter?
) : RecyclerView.Adapter<SaisieAdapter.SaisieViewHolder>() {

    var listeEditText = mutableListOf<EditText>()
    var listeValeurs = mutableListOf<EditText>()

    inner class SaisieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaisieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_saisie, parent, false)
        return SaisieViewHolder(view)
    }

    override fun onBindViewHolder(holder: SaisieViewHolder, position: Int) {
        holder.itemView.apply {

            if (criteres[position].serie.toBoolean()) {
                listeEditText.add(findViewById(R.id.etSaisie))
            }
            listeValeurs.add(findViewById(R.id.etSaisie))
            if (position == criteres.size-1 && bt != null){
                bt?.listeEditText = this@SaisieAdapter.listeEditText
            }
            findViewById<TextView>(R.id.tvNomChamp).text = criteres[position].nomCritere
        }
    }

    override fun getItemCount(): Int {
        return criteres.size
    }


}