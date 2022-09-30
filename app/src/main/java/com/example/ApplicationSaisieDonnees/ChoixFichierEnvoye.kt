package com.example.ApplicationSaisieDonnees

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChoixFichierEnvoye : AppCompatActivity() {
    var listeFichier = mutableListOf<ListeTemplate>()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_fichier_envoye)

        var rvListeFichier = findViewById<RecyclerView>(R.id.rvListeFichier)
        for(i in filesDir!!.list()){
            if (i.toString().endsWith(".csv",false)){
                Toast.makeText(this, i.toString(), Toast.LENGTH_SHORT).show()
                listeFichier.add(ListeTemplate(i.toString(), false))
            }

        }

        val adapter = ChoixFichierEnvoyeAdapter(listeFichier, this)
        rvListeFichier.adapter = adapter
        rvListeFichier.layoutManager = LinearLayoutManager(this)




        //FTPHandler(this).sendFile("euronutrition", "test.csv")
    }
}