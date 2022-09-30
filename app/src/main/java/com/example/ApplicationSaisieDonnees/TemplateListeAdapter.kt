package com.example.ApplicationSaisieDonnees

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TemplateListeAdapter(
    var listeTemplate: List<ListeTemplate>,
    var i: Int = 0
) : RecyclerView.Adapter<TemplateListeAdapter.TemplateListeViewHolder>() {

    inner class TemplateListeViewHolder(templateListeView: View) : RecyclerView.ViewHolder(templateListeView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateListeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_template, parent, false)
        return TemplateListeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TemplateListeViewHolder, position: Int) {

        holder.itemView.apply {
            if (position%2 == 0){
                this.setBackgroundColor(Color.LTGRAY)
            }
            else{
                this.setBackgroundColor(Color.WHITE)
            }

            findViewById<TextView>(R.id.tvFichiers).text = listeTemplate[position].titre
            findViewById<CheckBox>(R.id.cbChoix).isChecked = listeTemplate[position].isChecked
            findViewById<CheckBox>(R.id.cbChoix).setOnClickListener {


                listeTemplate[i].isChecked = false
                listeTemplate[position].isChecked = true
                notifyDataSetChanged()
                i = position

            }

        }
    }

    override fun getItemCount(): Int {
        return listeTemplate.size
    }
}