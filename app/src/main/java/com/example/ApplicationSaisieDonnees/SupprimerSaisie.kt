package com.example.ApplicationSaisieDonnees

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.iterator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SupprimerSaisie : AppCompatActivity() {

    var saisieListe = mutableListOf<ListeTemplate>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supprimer_saisie)

        val btnAnnulerSuppression = findViewById<Button>(R.id.btnAnnulerSuppression)
        val btnSupprimerSaisie = findViewById<Button>(R.id.btnSupprimerSaisie)
        val rvSupprimerSaisie = findViewById<RecyclerView>(R.id.rvListeSaisies)


        val bdd = BDDHandler(this)

        btnAnnulerSuppression.setOnClickListener {
            finish()
        }

        for (i in bdd.listeFichier()!!){
            saisieListe.add(ListeTemplate(i, false))
        }

        val adapter = SupprimerSaisieAdapter(saisieListe)

        rvSupprimerSaisie.adapter = adapter
        rvSupprimerSaisie.layoutManager = LinearLayoutManager(this)

        btnSupprimerSaisie.setOnClickListener {
            var liste = ""
            for (i in rvSupprimerSaisie){

                if(i.findViewById<CheckBox>(R.id.cbChoix).isChecked){
                    liste = liste + i.findViewById<TextView>(R.id.tvFichiers).text.toString() + ";"
                }
            }
            if (liste.length == 0){
                Toast.makeText(this, "Aucun fichier sélectionné", Toast.LENGTH_LONG).show()
            }
            else{
                Intent(this, ValiderSuppression::class.java).also {
                    it.putExtra("EXTRA_LISTE",liste)
                    startActivityForResult(it, 1)

                }

            }

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK){
            finish()

        }
    }

}