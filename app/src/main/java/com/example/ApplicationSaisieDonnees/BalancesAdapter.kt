package com.example.ApplicationSaisieDonnees

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class BalancesAdapter(
        var liste: MutableList<ListeTemplate>,
        var context: Context
) : RecyclerView.Adapter<BalancesAdapter.BalancesViewHolder>(){

    var pref = context.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE)
    var bal = pref.getString("bal", "")
    var pos = if (bal.isNullOrBlank())0 else liste.indexOf(ListeTemplate(bal!!, false))
    var double = false

    private val gestureDetector : GestureDetector = GestureDetector(context, GestureDetector.SimpleOnGestureListener())
    //var edit = pref.edit()
    //edit.putString("utilisateur", animal)
    //edit.apply()



    inner class BalancesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalancesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_template, parent, false)
        return BalancesViewHolder(view)
    }

    override fun onBindViewHolder(holder: BalancesViewHolder, position: Int) {
        holder.itemView.apply {

            if (position == pos){
                liste[position].isChecked = true
                var edit = pref.edit()
                edit.putString("bal", liste[position].titre)
                edit.apply()
            }

            if (position%2 == 0){
                this.setBackgroundColor(Color.LTGRAY)
            }
            else{
                this.setBackgroundColor(Color.WHITE)
            }

            findViewById<TextView>(R.id.tvFichiers).text = liste[position].titre
            findViewById<CheckBox>(R.id.cbChoix).isChecked = liste[position].isChecked
            findViewById<CheckBox>(R.id.cbChoix).setOnClickListener {

                liste[pos].isChecked = false
                liste[position].isChecked = true
                notifyDataSetChanged()
                pos = position
                var edit = pref.edit()
                edit.putString("bal", liste[position].titre)
                edit.apply()
            }



            this.setOnClickListener {
                if (double){
                    Intent(context, InfosBalance::class.java).also {
                        it.putExtra("EXTRA_NOM_BAL", liste[position].titre)
                        context.startActivity(it)
                        var listeBalances = BDDHandler(context).listeBalances(null)
                        Log.d("bal", listeBalances[position].nom)
                        liste[position].titre = listeBalances[position].nom
                        notifyItemChanged(position)
                    }
                }

                double = true
                postDelayed({double = false}, 500)

            }


        }


    }



    override fun getItemCount(): Int {
        return liste.size
    }
}