package com.example.ApplicationSaisieDonnees

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.io.File
import java.lang.Exception

class ValiderSuppression : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_valider_suppression)

        val tvFichiersASupprimer = findViewById<TextView>(R.id.tvFichiersASupprimer)
        val btnAnnulerSuppr = findViewById<Button>(R.id.btnAnnulerSuppr)
        val btnValiderSuppression = findViewById<Button>(R.id.btnValiderSuppression)

        val bdd = BDDHandler(this)

        var liste = intent.getStringExtra("EXTRA_LISTE")
        while (liste != null && liste != "") {
            tvFichiersASupprimer.text = tvFichiersASupprimer.text.toString() +"\n" +liste.takeWhile { it != ';' }
            liste = liste.dropWhile { it != ';' }.drop(1)
        }

        btnAnnulerSuppr.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        btnValiderSuppression.setOnClickListener {
            var fichiers = intent.getStringExtra("EXTRA_LISTE")
            while (fichiers != null && fichiers != "") {
                try {
                    bdd.supprimerSaisie(fichiers.takeWhile { it != ';' })
                    File(externalCacheDir, fichiers.takeWhile { it != ';' }+".csv").delete()
                    fichiers = fichiers.dropWhile { it != ';' }.drop(1)
                }
                catch (e:Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }

            }
            setResult(RESULT_OK)
            finish()
        }


    }
}