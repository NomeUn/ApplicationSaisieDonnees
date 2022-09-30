package com.example.ApplicationSaisieDonnees


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ParametresBalances : AppCompatActivity() {

    lateinit var adapter : BalancesAdapter
    val bdd = BDDHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parametres_balances)

        bdd.listeBalances(::lister)

    }

    fun lister(liste:MutableList<Balances>) {
        val rvListeBal = findViewById<RecyclerView>(R.id.rvListeBal)
        var listeTemplate = mutableListOf<ListeTemplate>()
        if (liste.isNullOrEmpty()){
            Toast.makeText(this, "Aucune Balance", Toast.LENGTH_SHORT).show()
            finish()
        }
        else{
            for (i in liste){
                listeTemplate.add(ListeTemplate(i.nom, false))
            }
        }
        rvListeBal.layoutManager = LinearLayoutManager(this)
        val adapter = BalancesAdapter( listeTemplate,this )
        rvListeBal.adapter = adapter
    }

    /*override fun onResume() {
        super.onResume()
        liste = bdd.listeBalances()
        adapter.notifyDataSetChanged()
    }*/

}