package com.example.ApplicationSaisieDonnees

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class Parametres : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parametres)
        val btnEnvoyerFichiers = findViewById<Button>(R.id.btnEnvoyerFichiers)
        val btnModiUtilisateur = findViewById<Button>(R.id.btnModiUtilisateur)
        val btnAPropos = findViewById<Button>(R.id.btnAPropos)
        val btnModele = findViewById<Button>(R.id.btnModele)

        btnEnvoyerFichiers.setOnClickListener {
            Intent(this, ChoixFichierEnvoye::class.java).also {
                startActivity(it)
            }
        }

        btnModiUtilisateur.setOnClickListener {
            Intent(this, DefNomUtilisateur::class.java).also {
                startActivity(it)
            }
        }


        btnModele.setOnClickListener {
            Intent(this, ModeleAPartirDeModele::class.java).also {
                startActivity(it)
            }
        }

        btnAPropos.setOnClickListener {
            Intent(this, APropos::class.java).also {
                startActivity(it)
            }
        }


    }

}