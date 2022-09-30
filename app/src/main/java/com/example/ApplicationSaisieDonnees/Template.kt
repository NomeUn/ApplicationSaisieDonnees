package com.example.ApplicationSaisieDonnees

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

class Template : AppCompatActivity() {
    var criteresListe = mutableListOf<Critere>()
    var indexModif = -1
    lateinit var adapter: CritereAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template)

        val nomTemplate = intent.getStringExtra("EXTRA_NOMTEMPLATE")
        val modeleBase = intent.getStringExtra("EXTRA_MODELEBASE")

        if(modeleBase != null){
            criteresListe = BDDHandler(this).getListCritereFromModele(modeleBase)
        }


        val tvNomTemplate = findViewById<TextView>(R.id.tvNomTemplate)
        val ibPlus = findViewById<ImageButton>(R.id.ibPlus)
        val rvListeParam = findViewById<RecyclerView>(R.id.rvListeParam)
        val btnValiderTemplate = findViewById<Button>(R.id.btnValiderTemplate)


        adapter = CritereAdapter(criteresListe, this, ::startIntent)

        rvListeParam.adapter = adapter
        rvListeParam.layoutManager = LinearLayoutManager(this)

        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(UP + DOWN, RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                adapter.onItemMove(viewHolder?.adapterPosition, target?.adapterPosition);
                return true
            }

            override fun isLongPressDragEnabled(): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                criteresListe.removeAt(viewHolder.layoutPosition)
                adapter.notifyItemRemoved(viewHolder.layoutPosition)
            }


        })
        touchHelper.attachToRecyclerView(rvListeParam)



        tvNomTemplate.text = nomTemplate

        ibPlus.setOnClickListener {
            Intent(this, NouveauCritere::class.java).also {
                startActivityForResult(it, 1)
            }
        }

        btnValiderTemplate.setOnClickListener {
            if (criteresListe.size == 0){
                Toast.makeText(this, "Votre modèle est vide, complétez le avant de l'enregistrer",Toast.LENGTH_LONG).show()
            }
            else{
                //creerFichier()
                try {
                    BDDHandler(this).insertDataModele(criteresListe, intent.getStringExtra("EXTRA_NOMTEMPLATE").toString())
                }
                catch(e:Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }
                finish()

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK){

            var result = data?.getSerializableExtra("EXTRA_CRITERE")
            if (result != null){
                var chaine = result.toString().replace(" ", "")
                chaine = chaine.dropWhile { it != '=' }.drop(1)
                val nomCritere = chaine.takeWhile { it != ',' }
                chaine = chaine.dropWhile { it != '=' }.drop(1)
                val arrondi = chaine.takeWhile { it != ',' }
                chaine = chaine.dropWhile { it != '=' }.drop(1)
                val min = chaine.takeWhile { it != ',' }
                chaine = chaine.dropWhile { it != '=' }.drop(1)
                val max = chaine.takeWhile { it != ',' }
                chaine = chaine.dropWhile { it != '=' }.drop(1)
                val serie = chaine.takeWhile { it != ')' }


                val critere = Critere(nomCritere, arrondi, min, max, serie)
                criteresListe.add(critere)
                adapter.notifyItemInserted(criteresListe.size - 1)


            }
        }
        else if (requestCode == 2 && resultCode == RESULT_OK){

            Toast.makeText(this, "modification", Toast.LENGTH_SHORT).show()



            var result = data?.getSerializableExtra("EXTRA_CRITERE")
            if (result != null){
                var chaine = result.toString().replace(" ", "")
                chaine = chaine.dropWhile { it != '=' }.drop(1)
                val nomCritere = chaine.takeWhile { it != ',' }
                chaine = chaine.dropWhile { it != '=' }.drop(1)
                val arrondi = chaine.takeWhile { it != ',' }
                chaine = chaine.dropWhile { it != '=' }.drop(1)
                val min = chaine.takeWhile { it != ',' }
                chaine = chaine.dropWhile { it != '=' }.drop(1)
                val max = chaine.takeWhile { it != ',' }
                chaine = chaine.dropWhile { it != '=' }.drop(1)
                val serie = chaine.takeWhile { it != ')' }


                val critere = Critere(nomCritere, arrondi, min, max, serie)
                criteresListe[indexModif] = critere
                adapter.notifyDataSetChanged()

            }
        }


    }

    private fun creerFichier() {
        try {
            val fichier = openFileOutput(intent.getStringExtra("EXTRA_NOMTEMPLATE")+"Template.tpk", MODE_PRIVATE)
            for (i in criteresListe){
                fichier.write(critereToString(i))
            }
            fichier.close()
        }
        catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT)
        }
    }

    private fun critereToString(c:Critere):ByteArray{
        return (c.nomCritere+";"+c.nbArrondi+";"+c.min+";"+c.max+";"+c.serie+"\n").toByteArray()
    }

    fun startIntent(crit:Critere, pos:Int){
        indexModif = pos
        Intent(this, NouveauCritere::class.java).also {
            it.putExtra("EXTRA_CRITERE", crit.nomCritere)
            it.putExtra("EXTRA_ARRONDI", crit.nbArrondi)
            it.putExtra("EXTRA_MIN", crit.min)
            it.putExtra("EXTRA_MAX", crit.max)
            it.putExtra("EXTRA_SERIE", crit.serie)
            startActivityForResult(it, 2)
        }
    }

}


