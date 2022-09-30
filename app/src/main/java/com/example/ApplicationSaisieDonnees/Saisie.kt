package com.example.ApplicationSaisieDonnees

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.iterator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

class Saisie : AppCompatActivity() {

    var critereListe = mutableListOf<Critere>()
    var increment = false
    var id = 0
    var bdd = BDDHandler(this)
    var bt = BluetoothAdapter.getDefaultAdapter()
    lateinit var ibClose:ImageButton
    lateinit var rvListeCriteresSaisie : RecyclerView
    lateinit var rvListeBluetooth : RecyclerView
    lateinit var btnSuivant : Button
    lateinit var btnPrecedent : Button
    lateinit var saisieAdapter: SaisieAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saisie)

        var pref = getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE)
        var bal = pref.getString("bal", "")

        if (bal.isNullOrBlank()){
            Intent(this, ParametresBalances::class.java).also {
                startActivity(it)
            }
        }

        val saisie = intent.getStringExtra("EXTRA_NOMFICHIER").toString()
        val nomTemplate = bdd.getNomModeleFromSaisie(saisie)
        critereListe = bdd.getListCritereFromModele(nomTemplate)
        var listeView: MutableList<TextView>
        var serie = false



        val tvNomSaisie = findViewById<TextView>(R.id.tvNomSaisie)
        rvListeCriteresSaisie = findViewById<RecyclerView>(R.id.rvListeCriteresSaisie)
        rvListeBluetooth = findViewById<RecyclerView>(R.id.rvListeBluetooth)
        btnSuivant = findViewById<Button>(R.id.btnSuivant)
        btnPrecedent = findViewById<Button>(R.id.btnPrecedent)
        val btnFinSaisie = findViewById<Button>(R.id.btnFinSaisie)
        ibClose = findViewById<ImageButton>(R.id.ibClose)

        btnSuivant.visibility = View.INVISIBLE
        btnPrecedent.visibility = View.INVISIBLE
        rvListeCriteresSaisie.visibility = View.INVISIBLE



        id = bdd.getMaxID(saisie) + 1
        //Toast.makeText(this, id.toString(), Toast.LENGTH_LONG).show()

        /*val fis = openFileInput(fichierSaisie)
        val isr = InputStreamReader(fis)
        val fichier = BufferedReader(isr)
        val nomTemplate = fichier.readLine()
        fichier.close()
        isr.close()
        fis.close()*/


        tvNomSaisie.text = nomTemplate









        for(i in critereListe){
            if(i.serie.toBoolean()){
                serie = true
            }
        }

        if (serie){

            if (bt == null) {
                Toast.makeText(this,"Votre appareil n'est pas compatible avec le bluetooth",Toast.LENGTH_LONG).show()
            }

            if (!bt.isEnabled) {
                if (bt.enable()) {

                    Toast.makeText(this, "Bluetooth activé", Toast.LENGTH_LONG).show()
                }
            }

            var devices = bt.bondedDevices
            if (devices.isEmpty()){
                Toast.makeText(this, "Aucun appareil apparayé en Bluetooth",Toast.LENGTH_SHORT).show()
                changeVisibility(null)
            }
            else {
                var btAdapter = BtAdapter(devices, this, ::changeVisibility)
                rvListeBluetooth.adapter = btAdapter
                rvListeBluetooth.layoutManager = LinearLayoutManager(this)

                //var saisieAdapter = SaisieAdapter(critereListe, btAdapter)
                //rvListeCriteresSaisie.adapter = saisieAdapter
                //rvListeCriteresSaisie.layoutManager = LinearLayoutManager(this)

                //rvListeCriteresSaisie.visibility = View.INVISIBLE
            }
        }
        else{
            changeVisibility(null)
        }







        Intent(this, IncremCrit::class.java).also {
            it.putExtra("EXTRA_NOMCRITERE", critereListe[0].nomCritere)
            startActivityForResult(it, 1)
        }




        btnSuivant.setOnClickListener {
            closeKeyboard()
            try {
                if (estVide(rvListeCriteresSaisie)) {
                    Toast.makeText(this, "certains champs ne sont pas remplis", Toast.LENGTH_LONG).show()
                }
                else {
                    var borne = true

                    for (i in 0..critereListe.size - 1) {


                        var valeur = saisieAdapter.listeValeurs[i].text.toString()

                        if (valeur.toFloat() > critereListe.get(i).max.toFloat()) {
                            borne = false
                            Toast.makeText(this, "le champ ${saisieAdapter.listeValeurs[i].text.toString()} est superieur au max : $valeur>${critereListe.get(i).max}", Toast.LENGTH_LONG).show()
                        }
                        if (valeur.toFloat() < critereListe.get(i).min.toFloat()) {
                            borne = false
                            Toast.makeText(this, "le champ ${saisieAdapter.listeValeurs[i].text.toString()} est inferieur au min : $valeur<${critereListe.get(i).min}", Toast.LENGTH_LONG).show()
                        }
                    }
                    if (borne) {
                        sauvegarderChamps(saisie, saisieAdapter.listeValeurs, true)
                        id++
                    }

                }
            }
            catch (e:Exception){
                //Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }

        btnPrecedent.setOnClickListener {
            closeKeyboard()
            try {

                if (id > 1) {
                    if (estVide(rvListeCriteresSaisie)) {
                        precedent(saisie, rvListeCriteresSaisie)
                    } else {
                        sauvegarderChamps(saisie, saisieAdapter.listeValeurs, false)
                    }
                    id--
                }
            }
            catch (e:Exception){
                //Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        btnFinSaisie.setOnClickListener {
            closeKeyboard()
            try {


                if (!estVide(rvListeCriteresSaisie)) {
                    sauvegarderChamps(saisie, saisieAdapter.listeValeurs, true)
                }
                bdd.genererFichier(saisie)

                finish()
            }
            catch (e:Exception){
                //Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }

        }

        ibClose.setOnClickListener {
            changeVisibility(null)
        }

    }




    /*private fun remplirListe(nomFichier: String) {
        try {
            val fis = openFileInput(nomFichier)
            val isr = InputStreamReader(fis)
            val fichier = BufferedReader(isr)
            var ligne = fichier.readLine()

            while (ligne != null){
                definirCritere(ligne)
                ligne = fichier.readLine()
            }
        }
        catch (e: FileNotFoundException){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }*/
/*
    private fun definirCritere(ligne: String) {
        try {
            var chaine = ligne

            val nomCritere = chaine.takeWhile { it != ';' }
            chaine = chaine.dropWhile { it != ';' }.drop(1)
            val arrondi = chaine.takeWhile { it != ';' }
            chaine = chaine.dropWhile { it != ';' }.drop(1)
            val min = chaine.takeWhile { it != ';' }
            chaine = chaine.dropWhile { it != ';' }.drop(1)
            val max = chaine.takeWhile { it != ';' }
            chaine = chaine.dropWhile { it != ';' }.drop(1)
            val serie = chaine

            val critere = Critere(nomCritere, arrondi, min, max, serie)

            critereListe.add(critere)
        }
        catch (e:Exception){

        }
    }
*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK){
            increment = true
        }
    }


    fun sauvegarderChamps(saisie: String, listeValeurs: MutableList<EditText>, plus: Boolean){
        try {
            var compteur = 0;
            for (i in listeValeurs){

                var valeur = i.text.toString()
                if (critereListe[compteur].nbArrondi.toInt() == 0){
                    bdd.insertDataSaisie(saisie, id, valeur.toInt().toString(), compteur)
                }
                else{
                    bdd.insertDataSaisie(saisie, id, valeur.toFloat().round(critereListe[compteur].nbArrondi.toInt()).toString(), compteur)
                }


                var value = bdd.checkValues(saisie, id + if(plus) 1 else -1, compteur)
                if (value == ""){
                    if (compteur == 0 && increment){
                        if (critereListe[compteur].nbArrondi.toInt() == 0){
                            i.setText((valeur.toInt() + 1).toString())
                        }
                        else{
                            i.setText((valeur.toFloat() + 1.toFloat()).round(critereListe[compteur].nbArrondi.toInt()).toString())
                        }

                    }
                    else{
                        i.setText("")
                    }
                }
                else{
                    i.setText(value)
                }
                compteur ++
            }

        }
        catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun estVide(rvListeCriteresSaisie: RecyclerView):Boolean{
        for (i in rvListeCriteresSaisie){
            if (i.findViewById<EditText>(R.id.etSaisie).text.toString() == ""){
                return true
            }
        }
        return false
    }

    fun precedent(saisie : String, rvListeCriteresSaisie: RecyclerView){
        try {
            var compteur = 0;
            for (i in rvListeCriteresSaisie){

                var value = bdd.checkValues(saisie, id -1, compteur)
                i.findViewById<EditText>(R.id.etSaisie).setText(value)
                compteur ++
            }

        }
        catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    /*fun sauvegarderChamps(fichierSaisie:String, rvListeCriteresSaisie: RecyclerView){
        try {
            val file: FileOutputStream? = openFileOutput(fichierSaisie, MODE_APPEND)
            file?.write("\n".toByteArray())
            for (i in rvListeCriteresSaisie){

                file?.write((i.findViewById<EditText>(R.id.etSaisie).text.toString()+";").toByteArray())
                if (increment && i == rvListeCriteresSaisie[0]){
                    i.findViewById<EditText>(R.id.etSaisie).setText((i.findViewById<EditText>(R.id.etSaisie).text.toString().toInt() + 1).toString())
                }else{
                    i.findViewById<EditText>(R.id.etSaisie).setText("")
                }

            }
            file?.close()
        }
        catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }*/

    fun Float.round(decimals:Int): Float{
        var multiplier = 1.0
        repeat(decimals){multiplier *= 10}
        return (kotlin.math.round(this * multiplier) /multiplier).toFloat()
    }

    fun changeVisibility(btAdapter: BtAdapter?){
        ibClose.visibility = View.INVISIBLE
        rvListeBluetooth.visibility = View.INVISIBLE
        saisieAdapter = SaisieAdapter(critereListe, btAdapter)
        rvListeCriteresSaisie.adapter = saisieAdapter
        rvListeCriteresSaisie.layoutManager = LinearLayoutManager(this)
        rvListeCriteresSaisie.visibility = View.VISIBLE
        btnPrecedent.visibility = View.VISIBLE
        btnSuivant.visibility = View.VISIBLE
    }

    fun closeKeyboard(){
        val view = currentFocus
        if(view != null){
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}