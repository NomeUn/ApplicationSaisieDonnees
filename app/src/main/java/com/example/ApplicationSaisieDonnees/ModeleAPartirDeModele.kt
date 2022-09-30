package com.example.ApplicationSaisieDonnees

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ModeleAPartirDeModele : AppCompatActivity() {

    var bdd = BDDHandler(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modele_a_partir_de_modele)

        val rvListeModele = findViewById<RecyclerView>(R.id.rvListeModele)
        val etNomModele = findViewById<EditText>(R.id.etNomModele)
        val btnValiderNouveauModele = findViewById<Button>(R.id.btnValiderNouveauModele)
        val btnAnnulerNouveauModele = findViewById<Button>(R.id.btnAnnulerNouveauModele)

        var templateListe = mutableListOf<ListeTemplate>()

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
        var adapter = TemplateListeAdapter(templateListe, 0)
        rvListeModele.adapter = adapter
        rvListeModele.layoutManager = LinearLayoutManager(this)

        btnValiderNouveauModele.setOnClickListener {
            if (etNomModele.text.toString() == "" ){
                Toast.makeText(this, "Entrez un nom avant de valider", Toast.LENGTH_LONG).show()
            }
            else if(bdd.existeModele(etNomModele.text.toString())){
                Toast.makeText(this, "Ce modèle existe déjà", Toast.LENGTH_LONG).show()
            }
            else{
                Intent(this, Template::class.java).also {
                    it.putExtra("EXTRA_NOMTEMPLATE", etNomModele.text.toString())
                    Toast.makeText(this, templateListe[adapter.i].titre, Toast.LENGTH_LONG).show()
                    it.putExtra("EXTRA_MODELEBASE", templateListe[adapter.i].titre)
                    startActivity(it)
                    finish()
                }
            }
        }

        btnAnnulerNouveauModele.setOnClickListener {
            finish()
        }

    }
}