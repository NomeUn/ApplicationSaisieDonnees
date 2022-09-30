package com.example.ApplicationSaisieDonnees

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.lang.Exception

class NomTemplate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nom_template)

        val btnValiderNomTemplate = findViewById<Button>(R.id.btnValiderNomTemplate)
        val etNomTemplate = findViewById<EditText>(R.id.etNomTemplate)
        val bdd = BDDHandler(this)


        btnValiderNomTemplate.setOnClickListener {
            if (etNomTemplate.text.toString() == ""){
                Toast.makeText(this, "Le nom du modèle ne peut pas être vide",Toast.LENGTH_LONG).show()
            }
            else{
                try {
                    var existe = bdd.existeModele(etNomTemplate.text.toString())
                    /*for (i in fileList()){
                        if (i.toString() == (etNomTemplate.text.toString()+"Template.tpk")){
                            Toast.makeText(this,"Ce nom de modèle existe déjà", Toast.LENGTH_LONG).show()
                            existe = true
                        }
                    }*/
                    if (!existe){
                        Intent(this, Template::class.java).also {
                            it.putExtra("EXTRA_NOMTEMPLATE",etNomTemplate.text.toString())
                            startActivity(it)
                        }
                    }
                    else{
                        Toast.makeText(this, "Ce modèle existe déjà", Toast.LENGTH_LONG).show()
                    }
                    finish()

                }
                catch(e:Exception){
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                    finish()
                }

            }
        }
    }
}