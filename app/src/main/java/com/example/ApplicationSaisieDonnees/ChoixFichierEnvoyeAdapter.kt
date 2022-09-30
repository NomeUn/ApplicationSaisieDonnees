package com.example.ApplicationSaisieDonnees


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class ChoixFichierEnvoyeAdapter (
    var fichiers: MutableList<ListeTemplate>,
    var context: Context
) : RecyclerView.Adapter<ChoixFichierEnvoyeAdapter.ChoixViewHolder>() {

    var ftp = FTPHandler(context)
    var pref = context.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE)

    inner class ChoixViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoixViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fichier, parent, false)
        return ChoixViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChoixViewHolder, position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.tvNomFich).text = fichiers[position].titre
            this.setOnClickListener {

                var nom = pref.getString("nom", "")
                if (nom != null) {
                    //FTPHandler(context).sendFile(nom, fichiers[position].titre)
                    ftp.sendFile(nom, fichiers[position].titre)
                    Toast.makeText(context, "fichier ${fichiers[position].titre} envoyé", Toast.LENGTH_SHORT).show()
                    fichiers.removeAt(position)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return fichiers.size
    }

}