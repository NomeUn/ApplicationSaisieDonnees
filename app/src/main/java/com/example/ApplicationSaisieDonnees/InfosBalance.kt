package com.example.ApplicationSaisieDonnees

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class InfosBalance : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infos_balance)
        var nomBal = intent.getStringExtra("EXTRA_NOM_BAL")
        val bdd = BDDHandler(this)
        val bal = nomBal?.let { bdd.getBalance(it) }

        val etbNom = findViewById<EditText>(R.id.etbNom)
        val etbVitesse = findViewById<EditText>(R.id.etbVitesse)
        val spnParite = findViewById<Spinner>(R.id.spnParite)
        val etbNDB = findViewById<EditText>(R.id.etbNDB)
        val etbBDS = findViewById<EditText>(R.id.etbBDS)
        val cbbPEK = findViewById<CheckBox>(R.id.cbbPEK)
        val cbbasG = findViewById<CheckBox>(R.id.cbbasG)
        val etbPortee = findViewById<EditText>(R.id.etbPortee)

        val btnbValider = findViewById<Button>(R.id.btnbValider)
        val btnbAnnuler = findViewById<Button>(R.id.btnbAnnuler)

        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listOf("Impaire", "Aucune", "Paire"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnParite.adapter = adapter
        spnParite.visibility = View.VISIBLE




        etbNom.setText(bal?.nom.toString())
        etbVitesse.setText(bal?.vitesse.toString())
        spnParite.setSelection(bal?.parite!! - 1)
        etbNDB.setText(bal?.nb_bits.toString())
        etbBDS.setText(bal?.bits_stop.toString())
        cbbPEK.isChecked = bal?.isKilo.toString() == "1"
        cbbasG.isChecked = bal?.asGChar.toString() == "1"
        etbPortee.setText(bal?.porte.toString())


        btnbValider.setOnClickListener {
            if (bal != null) {
                bdd.modifBalance(nomBal, Balances(etbNom.text.toString(), etbVitesse.text.toString().toInt(), spnParite.selectedItemId.toInt()+1, etbNDB.text.toString().toInt(), etbBDS.text.toString().toInt(), etbPortee.text.toString().toInt(), if(cbbPEK.isChecked)1 else 0, if(cbbasG.isChecked)1 else 0, bal.chaineDemande, bal.chaineDemandedsD, bal.posistionDSD, bal.longueurDSD,bal.longueurStable,bal.ligneStable,bal.longueurInstable,bal.ligneInstable))
            }
            finish()
        }

        btnbAnnuler.setOnClickListener {
            finish()
        }

        //Toast.makeText(this, bal.toString(), Toast.LENGTH_SHORT).show()
    }

}