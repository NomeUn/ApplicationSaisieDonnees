package com.example.ApplicationSaisieDonnees

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var bdd: BDDHandler
    lateinit var ftp: FTPHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnTemplate = findViewById<Button>(R.id.btnTemplate)
        val btnNouvelleSaisie = findViewById<Button>(R.id.btnNouvelleSaisie)
        val btnContinuerSaisie = findViewById<Button>(R.id.btnContinuerSaisie)
        val btnSupprimerSaisie = findViewById<Button>(R.id.btnSupprimerSaisie)
        val btnParametresBalances = findViewById<Button>(R.id.btnParametresBalances)
        val btnParametres = findViewById<ImageButton>(R.id.btnParametres)

        bdd = BDDHandler(this)
        ftp = FTPHandler(this)
        BluetoothAdapter.getDefaultAdapter()?.enable()





        var pref = getSharedPreferences("pref", MODE_PRIVATE)
        var nom = pref.getString("nom", "")
        var secteur = pref.getString("utilisateur", "")
        var dateMaj = pref.getString("dateMaj", "")

        if (nom == "" || secteur == ""){
            Intent(this, DefNomUtilisateur::class.java).also {
                startActivityForResult(it, 1)
            }
        }

        

        //Toast.makeText(this, pref.getString("nom", "").toString(), Toast.LENGTH_LONG).show()
        ftp.checkMaj(dateMaj)


        if (nom != null && secteur != null) {
            ftp.recupModele(nom, secteur, btnNouvelleSaisie)
        }
        bdd.parseFichierBalances()

        if (bdd.listeFichier().isNullOrEmpty()){
            btnContinuerSaisie.visibility = View.INVISIBLE
            btnSupprimerSaisie.visibility = View.INVISIBLE
        }
        if(bdd.listeModele().isNullOrEmpty()){
            btnNouvelleSaisie.visibility = View.INVISIBLE
        }

        btnTemplate.setOnClickListener {
            Intent(this, NomTemplate::class.java).also {
                startActivity(it)
            }
        }

        btnContinuerSaisie.setOnClickListener {
            Intent(this, ContinuerSaisie::class.java).also {
                startActivity(it)
            }
        }

        btnSupprimerSaisie.setOnClickListener {
            Intent(this, SupprimerSaisie::class.java).also {
                startActivity(it)
            }
        }

        btnNouvelleSaisie.setOnClickListener {
            Intent(this, NouvelleSaisie::class.java).also {
                startActivity(it)
            }
        }

        btnParametres.setOnClickListener {
            Intent(this, Parametres::class.java).also {
                startActivity(it)
            }
        }

        btnParametresBalances.setOnClickListener {
            Intent(this, ParametresBalances::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onRestart() {
        super.onRestart()


        val btnNouvelleSaisie = findViewById<Button>(R.id.btnNouvelleSaisie)
        val btnContinuerSaisie = findViewById<Button>(R.id.btnContinuerSaisie)
        val btnSupprimerSaisie = findViewById<Button>(R.id.btnSupprimerSaisie)
        var pref = getSharedPreferences("pref", MODE_PRIVATE)
        var nom = pref.getString("nom", "")
        var secteur = pref.getString("utilisateur", "")

        if (nom != null && secteur != null) {
            ftp.recupModele(nom, secteur, btnNouvelleSaisie)
        }

        if (bdd.listeFichier().isNullOrEmpty()){
            btnContinuerSaisie.visibility = View.INVISIBLE
            btnSupprimerSaisie.visibility = View.INVISIBLE
        }
        else{
            btnContinuerSaisie.visibility = View.VISIBLE
            btnSupprimerSaisie.visibility = View.VISIBLE
        }
        if(bdd.listeModele().isNullOrEmpty()){
            btnNouvelleSaisie.visibility = View.INVISIBLE
        }
        else{
            btnNouvelleSaisie.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val btnNouvelleSaisie = findViewById<Button>(R.id.btnNouvelleSaisie)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK){
            var pref = getSharedPreferences("pref", MODE_PRIVATE)
            var nom = pref.getString("nom", "")
            var secteur = pref.getString("utilisateur", "")

            if (nom != null && secteur != null) {
                ftp.recupModele(nom, secteur, btnNouvelleSaisie)
            }
        }
        else{
            Toast.makeText(this, "utilisateur inconnu", Toast.LENGTH_LONG).show()
        }

    }


}