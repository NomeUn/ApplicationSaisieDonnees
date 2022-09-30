package com.example.ApplicationSaisieDonnees

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class DefNomUtilisateur : AppCompatActivity() {
    var liste = mutableListOf("Erreur" )
    lateinit var spnSecteur:Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_def_nom_utilisateur)
        FTPHandler(this).secteurs(::modifListe)



        val etNomUtilisateur = findViewById<EditText>(R.id.etNomUtilisateur)
        spnSecteur = findViewById(R.id.spnSecteur)
        val btnValider = findViewById<Button>(R.id.btnValider)
        val tvTexte = findViewById<TextView>(R.id.tvTexte)
        var euro = false
        var secteur = "lapins"
        var pref = getSharedPreferences("pref", MODE_PRIVATE)
        var nom = pref.getString("nom", "")
        etNomUtilisateur.setText(nom)




        spnSecteur.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                secteur = parent?.getItemAtPosition(position).toString().toLowerCase()
                //Toast.makeText(this@DefNomUtilisateur, animal, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        btnValider.setOnClickListener {
            if(etNomUtilisateur.text.toString() != ""){
                if (euro){
                    pref = getSharedPreferences("pref", MODE_PRIVATE)
                    var edit = pref.edit()
                    edit.putString("utilisateur", secteur)
                    edit.apply()
                    setResult(RESULT_OK)
                    finish()
                }
                else if (etNomUtilisateur.text.toString().toLowerCase() == "euronutrition"){
                    var pref = getSharedPreferences("pref", MODE_PRIVATE)

                    var edit = pref.edit()
                    edit.putString("nom", etNomUtilisateur.text.toString().toLowerCase())
                    edit.apply()

                    var adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, liste);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnSecteur.adapter = adapter
                    spnSecteur.visibility = View.VISIBLE
                    tvTexte.setText("Choisissez le secteur")
                    euro = true
                }
                /*else if (FTPHandler(this).existe(etNomUtilisateur.text.toString().toLowerCase())){
                    Toast.makeText(this, "Cet utilisateur existe dans la base", Toast.LENGTH_SHORT).show()
                    var pref = getSharedPreferences("pref", MODE_PRIVATE)
                    var edit = pref.edit()
                    edit.putString("nom", etNomUtilisateur.text.toString().toLowerCase())
                    edit.apply()
                    finish()
                }*/
                else{
                    Toast.makeText(this, "Cet utilisateur n'existe pas dans la base", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "entrez un nom d'utilisateur", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun modifListe(secteurs:MutableList<String>){
        liste = secteurs

    }



}