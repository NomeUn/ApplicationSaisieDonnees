package com.example.ApplicationSaisieDonnees

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NouvelleSaisie : AppCompatActivity() {

    var templateListe = mutableListOf<ListeTemplate>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nouvelle_saisie)
        val btnValiderTemplateNouvelleSaisie = findViewById<Button>(R.id.btnValiderTemplateNouvelleSaisie)

        val rvListeTemplates = findViewById<RecyclerView>(R.id.rvListeTemplates)

        val bdd = BDDHandler(this)
        if (! bdd.listeModele().isNullOrEmpty()){
            for (i in bdd.listeModele()!!) {
                templateListe.add(ListeTemplate(i, templateListe.size == 0))
            }
        }

        if (templateListe.size == 0){
            Toast.makeText(this, "Il n'y a pas de modèle", Toast.LENGTH_SHORT).show()
            Intent(this, NomTemplate::class.java).also{
                startActivity(it)
                finish()
            }
        }
        else{
            val adapter = TemplateListeAdapter(templateListe)

            rvListeTemplates.adapter = adapter
            rvListeTemplates.layoutManager = LinearLayoutManager(this)


            btnValiderTemplateNouvelleSaisie.setOnClickListener {
                Intent(this, NomFichier::class.java).also {
                    it.putExtra("EXTRA_NOMMODELE", templateListe[adapter.i].titre)
                    startActivity(it)
                    finish()
                }
            }
        }
    }

}