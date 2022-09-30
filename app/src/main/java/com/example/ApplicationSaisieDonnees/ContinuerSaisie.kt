package com.example.ApplicationSaisieDonnees

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ContinuerSaisie : AppCompatActivity() {
    lateinit var bdd : BDDHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_continuer_saisie)

        val btnAnnuler = findViewById<Button>(R.id.btnAnnulerSaisie)
        val btnValiderSaisie = findViewById<Button>(R.id.btnValiderSaisie)
        val rvContinuerSaisie = findViewById<RecyclerView>(R.id.rvContinuerSaisie)

        var saisieListe = mutableListOf<ListeTemplate>()

        bdd = BDDHandler(this)



        btnAnnuler.setOnClickListener {
            finish()
        }

        if (bdd.listeFichier()?.size!! > 0) {
            for (i in bdd.listeFichier()!!) {
                saisieListe.add(ListeTemplate(i, saisieListe.size == 0))
            }
        }
        else{
            Toast.makeText(this, "Il n'y a pas de Saisie", Toast.LENGTH_SHORT).show()
            Intent(this, NouvelleSaisie::class.java).also{
                startActivity(it)
            }
            finish()
        }


        val adapter = TemplateListeAdapter(saisieListe)

        rvContinuerSaisie.adapter = adapter
        rvContinuerSaisie.layoutManager = LinearLayoutManager(this)


        btnValiderSaisie.setOnClickListener {
            Intent(this, Saisie::class.java).also {
                it.putExtra("EXTRA_NOMFICHIER", saisieListe[adapter.i].titre)
                startActivity(it)
            }
            finish()
        }


    }
}