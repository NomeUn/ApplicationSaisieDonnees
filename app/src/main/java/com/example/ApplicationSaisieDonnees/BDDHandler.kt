package com.example.ApplicationSaisieDonnees

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*
import android.util.Log

import java.io.*


/**classe pour la gestion de la base de donnée de l'application*/
class BDDHandler(var context: Context) : SQLiteOpenHelper(context, "BDD", null, 1){

    /**fonction appelée automatiquement à l'instanciation d la classe, permet de créer nos 4 Tables dans notre BDD si elles n'existent pas*/

    override fun onCreate(db: SQLiteDatabase?) {
        var createTable = "CREATE TABLE IF NOT EXISTS ModeleChamp (num_modele INTEGER,id_champ INTEGER,nom_champ VARCHAR(256),arrondi INTEGER,min INTEGER,max FLOAT,serie INTEGER)"
        db?.execSQL(createTable)
        createTable = "CREATE TABLE IF NOT EXISTS Modele (id_modele INTEGER PRIMARY KEY AUTOINCREMENT,nom_modele VARCHAR(256))"
        db?.execSQL(createTable)
        createTable = "CREATE TABLE IF NOT EXISTS Saisie (id_saisie INTEGER PRIMARY KEY AUTOINCREMENT,date VARCHAR(10),nom_saisie VARCHAR(256),num_mod INTEGER)"
        db?.execSQL(createTable)
        createTable = "CREATE TABLE IF NOT EXISTS SaisieValeurs (id_entree INTEGER, num_saisie INTEGER,num_champ INTEGER,valeur VARCHAR(256))"
        db?.execSQL(createTable)
        createTable = "CREATE TABLE IF NOT EXISTS Balances (nom VARCHAR(32),vitesse INTEGER,parite INTEGER,nb_bits INTEGER,bits_stop INTEGER,porte INTEGER,isKilo INTEGER,asGChar INTEGER,chaineDemande VARCHAR(256),chaineDemandedsD VARCHAR(256),posistionDSD INTEGER,longueurDSD INTEGER,longueurStable INTEGER,ligneStable INTEGER,longueurInstable INTEGER,ligneInstable INTEGER)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    /**fonction pour insérer des données dans la table avec la liste des modèles et la table avec la liste des critères*/
    fun insertDataModele(listeCritere: List<Critere>, nomModele: String){
        val db = this.writableDatabase

        var curseur = db.rawQuery("SELECT id_modele FROM Modele WHERE nom_modele = \"$nomModele\"", null)
        var resultat = 0.toLong()
        if (curseur.count > 0){

        }
        else{
            var content = ContentValues()
            content.put("nom_modele", nomModele)
            resultat = db.insert("Modele", null, content)
            curseur = db.rawQuery("SELECT id_modele FROM Modele WHERE nom_modele = \"$nomModele\"", null)

        }
        if(resultat == -1.toLong()){
            //Toast.makeText(context, "Erreur", Toast.LENGTH_SHORT).show()
        }
        else{
            for (i in listeCritere){
                var cv = ContentValues()

                curseur.moveToFirst()
                var champ = curseur.getInt(0)
                cv.put("num_modele", champ)
                cv.put("id_champ", listeCritere.indexOf(i))
                cv.put("nom_champ", i.nomCritere)
                cv.put("arrondi", i.nbArrondi.toInt())
                cv.put("min", i.min.toFloat())
                cv.put("max", i.max.toFloat())
                cv.put("serie", if (i.serie == "true")1 else 0)

                var result = db.insert("ModeleChamp", null, cv)
                if(result == -1.toLong()){
                    //Toast.makeText(context, "Erreur", Toast.LENGTH_SHORT).show()
                }
                else{
                    //Toast.makeText(context, "Reussite", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**fonction permettant de retourner la liste des noms de modèles*/
    fun listeModele(): List<String>? {
        val db = this.writableDatabase
        var curseur = db.rawQuery("SELECT nom_modele FROM Modele", null)
        var resultat = mutableListOf<String>()
        curseur.moveToFirst()
        if (curseur.count > 0) {
            do {
                resultat.add(curseur.getString(0))
            } while (curseur.moveToNext())
        }
        else{
            return null
        }
        return resultat
    }

    /**fonction permettant de retourner la lisye des noms de saisies*/
    fun listeFichier(): List<String>? {
        val db = this.writableDatabase
        var curseur = db.rawQuery("SELECT nom_saisie FROM Saisie", null)
        var resultat = mutableListOf<String>()
        if (curseur.count > 0){
            curseur.moveToFirst()
            do {
                resultat.add(curseur.getString(0))
            } while (curseur.moveToNext())
        }
        else{
            return null
        }
        return resultat
    }

    fun nouvelleSaisie(nomSaisie : String, nomModele : String){
        val db = this.writableDatabase
        var curseur = db.rawQuery("SELECT id_modele FROM Modele WHERE nom_modele = \"$nomModele\"", null)
        curseur.moveToFirst()
        var cv = ContentValues()
        val date = Calendar.getInstance()
        cv.put("date", "${date.get(Calendar.DAY_OF_MONTH).toString()}-${(date.get(Calendar.MONTH)+1).toString()}-${date.get(Calendar.YEAR).toString()}")
        cv.put("nom_saisie", nomSaisie)
        cv.put("num_mod", curseur.getInt(0))
        var result = db.insert("Saisie", null, cv)
        if(result == -1.toLong()){
            Toast.makeText(context, "Erreur", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Reussite", Toast.LENGTH_SHORT).show()
        }
    }

    fun getNomModeleFromSaisie(nomSaisie: String):String{
        val db = this.writableDatabase
        val curseur = db.rawQuery("SELECT nom_modele FROM Modele m INNER JOIN Saisie s ON m.id_modele = s.num_mod WHERE s.nom_saisie = \"$nomSaisie\"", null)
        curseur.moveToFirst()
        return curseur.getString(0)
    }

    fun getListCritereFromModele(nomModele: String): MutableList<Critere>{
        val db = this.writableDatabase
        var curseur = db.rawQuery("SELECT * FROM ModeleChamp mc INNER JOIN Modele m ON mc.num_modele = m.id_modele WHERE m.nom_modele = \"$nomModele\"", null)
        var resultat = mutableListOf<Critere>()
        curseur.moveToFirst()
        do {
            resultat.add(Critere(curseur.getString(curseur.getColumnIndex("nom_champ")), curseur.getInt(curseur.getColumnIndex("arrondi")).toString(),curseur.getInt(curseur.getColumnIndex("min")).toString(), curseur.getInt(curseur.getColumnIndex("max")).toString(), if (curseur.getInt(curseur.getColumnIndex("serie")) == 0)"false" else "true" ))
        }while (curseur.moveToNext())
        return resultat
    }

    fun existeSaisie(nomSaisie: String): Boolean {
        val db = this.writableDatabase
        val curseur = db.rawQuery("SELECT * FROM Saisie WHERE nom_saisie = \"$nomSaisie\"", null)
        return curseur.count > 0
    }

    fun getMaxID(saisie: String): Int {
        val db = this.writableDatabase
        val curseur = db.rawQuery("SELECT MAX(id_entree) FROM SaisieValeurs sv INNER JOIN Saisie s ON sv.num_saisie = s.id_saisie WHERE s.nom_saisie = \"$saisie\"", null)
        if (curseur.count > 0){
            curseur.moveToFirst()
            return curseur.getInt(0)
        }
        return 0
    }

    fun insertDataSaisie(nomSaisie :String, id_entree: Int, valeur: String, num_champ: Int){
        val db = this.writableDatabase
        if (checkValues(nomSaisie, id_entree, num_champ) == ""){
            val curseur = db.rawQuery("SELECT id_saisie FROM Saisie WHERE nom_saisie = \"$nomSaisie\"", null)
            curseur.moveToFirst()
            var cv = ContentValues()
            cv.put("id_entree", id_entree)
            cv.put("num_saisie", curseur.getInt(0))
            cv.put("num_champ", num_champ)
            cv.put("valeur", valeur)

            var resultat = db.insert("SaisieValeurs", null, cv)
            if(resultat == -1.toLong()){

            }
            else{

            }
        }
        else{
            db.execSQL("UPDATE SaisieValeurs SET valeur = \"$valeur\" WHERE num_saisie = (SELECT id_saisie FROM Saisie WHERE nom_saisie = \"$nomSaisie\") AND id_entree = \"$id_entree\" AND num_champ = \"$num_champ\"")
        }

    }

    /*fun delDatatSaisie(nomSaisie :String, id_entree: Int, num_champ: Int){
        val db = this.writableDatabase
        db.rawQuery("DELETE FROM SaisieValeurs WHERE num_saisie = (SELECT id_saisie FROM Saisie WHERE nom_saisie = \"$nomSaisie\") AND id_entree = \"$id_entree\" AND num_champ = \"$num_champ\"",null)
    }*/

    fun checkValues(nomSaisie :String, id_entree: Int, num_champ: Int) : String{
        val db = this.writableDatabase
        var curseur = db.rawQuery("SELECT valeur FROM SaisieValeurs WHERE num_saisie = (SELECT id_saisie FROM Saisie WHERE nom_saisie = \"$nomSaisie\") AND id_entree = \"$id_entree\" AND num_champ = \"$num_champ\"", null)
        if(curseur.count > 0){
            curseur.moveToFirst()
            return curseur.getString(0)
        }
        return ""
    }

    fun supprimerSaisie(nomSaisie: String){
        val db = this.writableDatabase
        var curseur = db.rawQuery("SELECT id_saisie FROM Saisie WHERE nom_saisie = \"$nomSaisie\"",null)
        if (curseur.count > 0){
            curseur.moveToFirst()
            var id = curseur.getInt(0)
            db.execSQL("DELETE FROM SaisieValeurs WHERE num_saisie = \"$id\"")
            db.execSQL("DELETE FROM Saisie WHERE nom_saisie = \"$nomSaisie\"")
        }

    }

    fun genererFichier(nomSaisie: String) {
        val db = this.writableDatabase
        var date = db.rawQuery("SELECT date FROM Saisie WHERE nom_saisie = \"$nomSaisie\"",null)
        var curseur = db.rawQuery("SELECT valeur FROM SaisieValeurs sv INNER JOIN Saisie s ON sv.num_saisie = s.id_saisie WHERE nom_saisie = \"$nomSaisie\"", null)
        var nombreChamps = db.rawQuery("SELECT MAX(num_champ) FROM SaisieValeurs sv INNER JOIN Saisie s ON sv.num_saisie = s.id_saisie WHERE nom_saisie = \"$nomSaisie\"", null)
        var nombreEntree = db.rawQuery("SELECT MAX(id_entree) FROM SaisieValeurs sv INNER JOIN Saisie s ON sv.num_saisie = s.id_saisie WHERE nom_saisie = \"$nomSaisie\"", null)
        var nomsChamps = db.rawQuery("SELECT nom_champ FROM ModeleChamp mc INNER JOIN Modele m ON mc.num_modele = m.id_modele WHERE m.id_modele = (SELECT num_mod FROM Saisie WHERE nom_saisie = \"$nomSaisie\")",null)
        nombreChamps.moveToFirst()
        nombreEntree.moveToFirst()
        try {

            var fichier = File(context.filesDir, nomSaisie+".csv")
            if(fichier.exists()){
                var res = fichier.delete()
                Log.d("file", res.toString())
            }


            var file = File(context.filesDir, nomSaisie+".csv")
            var fos = FileOutputStream(file)
            date.moveToFirst()
            fos.write((date.getString(0) + "\n").toByteArray())

            nomsChamps.moveToFirst()
            for(i in 0..nomsChamps.count-1){
                fos.write((nomsChamps.getString(0)+"\t").toByteArray())
                nomsChamps.moveToNext()
            }
            fos.write("\n".toByteArray())


            if (curseur.count > 0){
                curseur.moveToFirst()
                var nbEntree = nombreEntree.getInt(0)
                var nbChamps =  nombreChamps.getInt(0)
                for (i in 1..nbEntree.toInt()) {
                    for (j in 0..nbChamps.toInt()) {
                        fos.write((curseur.getString(0).toString()+"\t").toByteArray())
                        curseur.moveToNext()
                    }
                    fos.write("\n".toByteArray())
                }
            }

            fos.close()
        }
        catch (e:Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }

    }

    fun parseFichierBalances(){
        try {
            val db = this.writableDatabase
            db.execSQL("DELETE FROM Balances")
            val fichier = BufferedReader(InputStreamReader(context.openFileInput("balances.txt")))
            var ligne = fichier.readLine()
            //Toast.makeText(context, ligne.toString(), Toast.LENGTH_SHORT).show()
            while (!ligne.isNullOrBlank()){
                var cv  = ContentValues()
                for (i in 1..16){
                    when(i){
                        1 -> cv.put("nom",ligne.replace(" ", ""))
                        2 -> cv.put("vitesse",ligne.replace(" ", "").toInt())
                        3 -> cv.put("parite",when(ligne.replace(" ", "")){
                            "O" -> 1
                            "N" -> 2
                            "E" -> 3
                            else -> 0
                        })
                        4 -> cv.put("nb_bits",ligne.replace(" ", "").toInt())
                        5 -> cv.put("bits_stop",ligne.replace(" ", "").toInt())
                        6 -> cv.put("porte",ligne.replace(" ", "").toInt())
                        7 -> cv.put("isKilo",if (ligne.replace(" ", "") == "Faux")0 else 1)
                        8 -> cv.put("asGChar",if (ligne.replace(" ", "") == "Faux")0 else 1)
                        9 -> cv.put("chaineDemande",ligne)
                        10 -> cv.put("chaineDemandedsD",ligne)
                        11 -> cv.put("posistionDSD",ligne.replace(" ", "").toInt())
                        12 -> cv.put("longueurDSD",ligne.replace(" ", "").toInt())
                        13 -> cv.put("longueurStable",ligne.replace(" ", "").toInt())
                        14 -> cv.put("ligneStable",ligne.replace(" ", "").toInt())
                        15 -> cv.put("longueurInstable",ligne.replace(" ", "").toInt())
                        16 -> cv.put("ligneInstable",ligne.replace(" ", "").toInt())
                        else -> Log.e("debug", "nothing")
                    }
                    ligne = fichier.readLine()
                }
                var resultat = db.insert("Balances", null, cv)
                if(resultat == -1.toLong()){
                    Toast.makeText(context, "Balances enregistrée avec succès", Toast.LENGTH_SHORT).show()
                }
                else{

                }
            }



        }
        catch (e: FileNotFoundException){
            Toast.makeText(context, "Le fichier de configuration des balances est innexistant, veuillez le récupérer sur le serveur", Toast.LENGTH_LONG).show()
        }
        catch (e:Exception){
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    fun listeBalances(lister: ((listeBal: MutableList<Balances>) -> Unit)?): MutableList<Balances> {
        val db = this.writableDatabase
        var liste = mutableListOf<Balances>()
        var curseur = db.rawQuery("SELECT * FROM Balances", null)

        if (curseur.count < 1 ){
            parseFichierBalances()
        }
        curseur.moveToFirst()
        for (i in 1..curseur.count){
            liste.add(Balances(curseur.getString(0), curseur.getInt(1),curseur.getInt(2), curseur.getInt(3), curseur.getInt(4), curseur.getInt(5), curseur.getInt(6), curseur.getInt(7), curseur.getString(8), curseur.getString(9), curseur.getInt(10), curseur.getInt(11), curseur.getInt(12), curseur.getInt(13), curseur.getInt(14), curseur.getInt(15)))
            curseur.moveToNext()
        }
        lister?.invoke(liste)
        return liste
    }

    fun existeModele(nom: String):Boolean {
        val db = this.writableDatabase
        val curseur = db.rawQuery("SELECT * FROM Modele WHERE nom_modele = \"$nom\"",null)
        return curseur.count > 0
    }

    fun getBalance(nom : String):Balances{
        val db = this.writableDatabase
        var curseur = db.rawQuery("SELECT * FROM Balances WHERE nom = \"$nom\"",null)
        curseur.moveToFirst()
        return Balances(curseur.getString(0), curseur.getInt(1),curseur.getInt(2),curseur.getInt(3),curseur.getInt(4),curseur.getInt(5),curseur.getInt(6),curseur.getInt(7),curseur.getString(8),curseur.getString(9),curseur.getInt(10),curseur.getInt(11),curseur.getInt(12),curseur.getInt(13),curseur.getInt(14),curseur.getInt(15))
    }

    fun modifBalance(nomBal: String?, bal: Balances) {
        val db = this.writableDatabase
        db.execSQL("UPDATE Balances SET nom = \"${bal.nom}\", vitesse = \"${bal.vitesse}\",parite = \"${bal.parite}\",nb_bits = \"${bal.nb_bits}\",bits_stop = \"${bal.bits_stop}\",porte = \"${bal.porte}\",isKilo = \"${bal.isKilo}\",asGChar = \"${bal.asGChar}\",chaineDemande = \"${bal.chaineDemande}\",chaineDemandedsD = \"${bal.chaineDemandedsD}\",posistionDSD = \"${bal.posistionDSD}\",longueurDSD = \"${bal.longueurDSD}\",longueurStable = \"${bal.longueurStable}\",ligneStable = \"${bal.ligneStable}\",longueurInstable = \"${bal.longueurInstable}\",ligneInstable = \"${bal.ligneInstable}\" WHERE nom = \"$nomBal\"")
    }

    fun setTemp(crit: Critere){
        val db = this.writableDatabase
        db.execSQL("DELETE FROM ModeleChamp WHERE num_modele = 0 AND id_champ = 0")
        var cv = ContentValues()
        cv.put("num_modele", 0)
        cv.put("id_champ", 0)
        cv.put("nom_champ", crit.nomCritere)
        cv.put("arrondi", crit.nbArrondi)
        cv.put("min", crit.min)
        cv.put("max", crit.max)
        cv.put("serie", crit.serie)

        var result = db.insert("ModeleChamp", null, cv)
        if(result == -1.toLong()){
            Toast.makeText(context, "Erreur", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Reussite", Toast.LENGTH_SHORT).show()
        }
    }


    fun parseFichierModele() {

        //ModeleChamp (num_modele INTEGER,id_champ INTEGER,nom_champ VARCHAR(256),arrondi INTEGER,min INTEGER,max FLOAT,serie INTEGER)
        // Modele (id_modele INTEGER PRIMARY KEY AUTOINCREMENT,nom_modele VARCHAR(256))"
        try {
            val db = this.writableDatabase

            var curseur = db.rawQuery("SELECT id_modele FROM Modele WHERE nom_modele = \"modelePC\"",null)
            var id:Int? = null
            if(curseur.count > 0){
                curseur.moveToFirst()
                id = curseur.getInt(0)
                db.execSQL("DELETE FROM ModeleChamp WHERE num_modele = \"$id\"")
            }
            else{

            }

            val fichier = BufferedReader(InputStreamReader(context.openFileInput("modele.txt")))
            var ligne = fichier.readLine()
            //Toast.makeText(context, ligne.toString(), Toast.LENGTH_SHORT).show()

            var criteres = mutableListOf<Critere>()
            while (!ligne.isNullOrBlank()){

                var nom = ligne.takeWhile { it != ';' }
                ligne = ligne.dropWhile { it != ';' }.drop(1)
                var arrondi = ligne.takeWhile { it != ';' }
                ligne = ligne.dropWhile { it != ';' }.drop(1)
                var min = ligne.takeWhile { it != ';' }
                ligne = ligne.dropWhile { it != ';' }.drop(1)
                var max = ligne.takeWhile { it != ';' }
                ligne = ligne.dropWhile { it != ';' }.drop(1)
                criteres.add(Critere(nom,arrondi,min,max,ligne))

                ligne = fichier.readLine()
            }

            insertDataModele(criteres, "modelePC")
        }
        catch (e: FileNotFoundException){
            Toast.makeText(context, "Le fichier de modèle n'a pas été trouvé", Toast.LENGTH_LONG).show()
        }
        catch (e:Exception){

            Log.d("erreur", e.message.toString())
            //Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }


}