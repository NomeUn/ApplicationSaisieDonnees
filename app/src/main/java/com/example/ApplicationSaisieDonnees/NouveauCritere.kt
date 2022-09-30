 package com.example.ApplicationSaisieDonnees

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast

class NouveauCritere : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nouveau_critere)

        val btnAnnulerNouveauCritere = findViewById<Button>(R.id.btnAnnulerNouveauCritere)
        val btnValiderNouveauCritere = findViewById<Button>(R.id.btnValiderNouveauCritere)

        val etNomCritere = findViewById<EditText>(R.id.etNomCritere)
        val etNbChiffreVirgule = findViewById<EditText>(R.id.etNbChiffreVirgule)
        val etValMin = findViewById<EditText>(R.id.etValMin)
        val etValMax = findViewById<EditText>(R.id.etValMax)
        val cbSerie = findViewById<CheckBox>(R.id.cbSerie)

        var nom = intent.getStringExtra("EXTRA_CRITERE")
        var arrondi = intent.getStringExtra("EXTRA_ARRONDI")
        var min = intent.getStringExtra("EXTRA_MIN")
        var max = intent.getStringExtra("EXTRA_MAX")
        var serie = intent.getStringExtra("EXTRA_SERIE")

        if (nom != null) etNomCritere.setText(nom)
        if (arrondi != null) etNbChiffreVirgule.setText(arrondi)
        if (min != null) etValMin.setText(min)
        if (max != null) etValMax.setText(max)
        if (serie != null) cbSerie.isChecked = serie.toBoolean()


        btnAnnulerNouveauCritere.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        btnValiderNouveauCritere.setOnClickListener {
            if (etNomCritere.text.toString() == ""){
                Toast.makeText(this, "Le nom de votre critère ne peut pas être vide", Toast.LENGTH_LONG).show()
            }
            else if (etValMin.text.toString() != "" && etValMin.text.toString().toFloat() < 0){
                Toast.makeText(this, "La valeur minimale ne peut pas être inferieur à 0", Toast.LENGTH_LONG).show()
            }
            else if (etValMax.text.toString() == ""){
                Toast.makeText(this, "La valeur maximale ne peut pas être vide", Toast.LENGTH_LONG).show()
            }
            else if (etValMin.text.toString() != "" && etValMax.text.toString().toFloat() <= etValMin.text.toString().toFloat()){
                Toast.makeText(this, "La valeur maximale ne peut pas être inferieur ou égale à la valeur minimale", Toast.LENGTH_LONG).show()
            }
            else{
                if (etNbChiffreVirgule.text.toString() == ""){
                    Toast.makeText(this, "Le nombre de chiffre après la virgule est égal à 0", Toast.LENGTH_LONG).show()
                    etNbChiffreVirgule.setText("0")
                }
                if (etValMin.text.toString() == ""){
                    Toast.makeText(this, "La valeure minimale est égal à 0", Toast.LENGTH_LONG).show()
                    etValMin.setText("0")
                }
                val critere = Critere(etNomCritere.text.toString(), etNbChiffreVirgule.text.toString(), etValMin.text.toString(), etValMax.text.toString(), cbSerie.isChecked.toString())
                BDDHandler(this).setTemp(critere)

                setResult(RESULT_OK, Intent().putExtra("EXTRA_CRITERE", critere))
                finish()
            }
        }
    }

}