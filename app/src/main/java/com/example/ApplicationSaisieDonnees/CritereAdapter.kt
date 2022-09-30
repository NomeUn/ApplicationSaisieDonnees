package com.example.ApplicationSaisieDonnees

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CritereAdapter(
    var criteres: MutableList<Critere>,
    var context: Context,
    val startIntent: (crit: Critere, pos: Int) -> Unit
) : RecyclerView.Adapter<CritereAdapter.CritereViewHolder>() {

    inner class CritereViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CritereViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_critere, parent, false)
        return CritereViewHolder(view)
    }

    override fun onBindViewHolder(holder: CritereViewHolder, position: Int) {
        holder.itemView.apply {


            findViewById<TextView>(R.id.tvItemNomCritere).text = criteres[position].nomCritere
            findViewById<TextView>(R.id.tvItemNombreArrondi).text = criteres[position].nbArrondi
            findViewById<TextView>(R.id.tvItemMin).text = criteres[position].min
            findViewById<TextView>(R.id.tvItemMax).text = criteres[position].max
            findViewById<TextView>(R.id.tvSerie).text = criteres[position].serie


            this.setOnClickListener {
                startIntent(criteres[position], position)
                /*Intent(context, NouveauCritere::class.java).also {
                    it.putExtra("EXTRA_CRITERE", criteres[position].nomCritere)
                    it.putExtra("EXTRA_ARRONDI", criteres[position].nbArrondi)
                    it.putExtra("EXTRA_MIN", criteres[position].min)
                    it.putExtra("EXTRA_MAX", criteres[position].max)
                    it.putExtra("EXTRA_SERIE", criteres[position].serie)
                    context.startActivity(it)
                }*/
            }
        }
    }



    override fun getItemCount() = criteres.size

    fun onItemMove(fromPosition: Int?, toPosition: Int?): Boolean {
        fromPosition?.let {
            toPosition?.let {
                if (fromPosition < toPosition) {
                    for (i in fromPosition until toPosition) {
                        Collections.swap(criteres, i, i + 1)
                    }
                } else {
                    for (i in fromPosition downTo toPosition + 1) {
                        Collections.swap(criteres, i, i - 1)
                    }
                }
                notifyItemMoved(fromPosition, toPosition)
                return true
            }
        }
        return false
    }

    fun notifiy(){

    }

}





