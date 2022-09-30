package com.example.ApplicationSaisieDonnees

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class NomFichier : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nom_fichier)

        val tvNomModele = findViewById<TextView>(R.id.tvNomModele)
        val etNomFichier = findViewById<EditText>(R.id.etNomFichier)
        val btnValiderNomFichier = findViewById<Button>(R.id.btnValiderNomFichier)

        val modele = intent.getStringExtra("EXTRA_NOMMODELE")
        tvNomModele.text = modele
        val bdd = BDDHandler(this)

        val sdf = SimpleDateFormat("dd-MM-yyyy")

        etNomFichier.setText(modele+sdf.format(Date()))


        btnValiderNomFichier.setOnClickListener {
            if (etNomFichier.text.toString() == ""){
                Toast.makeText(this, "Veuillez entrer un nom de sauvegarde", Toast.LENGTH_SHORT).show()
            }else{
                try {
                    if (bdd.existeSaisie(etNomFichier.text.toString())){
                        Intent(this, ContinuerSaisie::class.java).also {
                            Toast.makeText(this, "une saisie du même nom existe déjà", Toast.LENGTH_LONG).show()
                            startActivity(it)
                        }
                    }
                    else{

                        bdd.nouvelleSaisie(etNomFichier.text.toString(), intent.getStringExtra("EXTRA_NOMMODELE").toString())

                        Intent(this, Saisie::class.java).also {
                            it.putExtra("EXTRA_NOMFICHIER", etNomFichier.text.toString())
                            startActivity(it)
                        }
                    }

                }
                catch (e:Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        }

    }
}