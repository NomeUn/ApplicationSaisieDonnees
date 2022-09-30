package com.example.ApplicationSaisieDonnees

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPClientConfig
import java.io.*
import java.lang.Exception


class FTPHandler(var context: Context) {


    /*----------------------------------*/
    /*à compléter avec votre serveur FTP*/
    /*----------------------------------*/
    var FTPid: String? = ""
    var FTPpwd: String? = ""
    var FTPhost: String? = ""


    /*fun onCreate(){
        GlobalScope.launch {
            try{
                val ftpClient = FTPClient()
                val config = FTPClientConfig()
                ftpClient.configure(config)
                ftpClient.connect(FTPhost, 21)
                ftpClient.replyString
                ftpClient.login(FTPid, FTPpwd)
                Log.d("ah", ftpClient.replyString.toString())
                ftpClient.enterLocalPassiveMode()
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
                var outputstream = BufferedOutputStream(FileOutputStream(File(context.cacheDir, "balances.txt")))
                ftpClient.retrieveFile("/euronutrition/BALANCES.TXT", outputstream)

                outputstream.close()
                ftpClient.appendFile("/euronutrition/saisie.txt",FileInputStream(File(context.cacheDir, "fichier*.csv")))


                var list = ftpClient.listFiles()
                for (i in list){
                    Log.d("ah", i.toString())
                }
                //Toast.makeText(this, ftpClient.replyString, Toast.LENGTH_LONG).show()

                //Toast.makeText(this, ftpClient.replyString, Toast.LENGTH_LONG).show()
            }
            catch (e: Exception){
                Log.d(TAG, e.message.toString())
            }
        }
    }*/

    fun getBalances(){
        GlobalScope.launch {
            try{
                val ftpClient = FTPClient()
                val config = FTPClientConfig()
                ftpClient.configure(config)
                ftpClient.connect(FTPhost, 21)
                ftpClient.replyString
                ftpClient.login(FTPid, FTPpwd)
                Log.d("ah", ftpClient.replyString.toString())
                ftpClient.enterLocalPassiveMode()
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
                var outputstream = BufferedOutputStream(FileOutputStream(File( context.filesDir,"balancestemp.txt")))
                ftpClient.retrieveFile("/euronutrition/BALANCES.TXT", outputstream)
                outputstream.close()
                ftpClient.disconnect()

                val fichier = BufferedReader(InputStreamReader(context.openFileInput("balancestemp.txt")))
                val ligne = fichier.readLine()
                Log.e("fichier", ligne)
                if (ligne != ""){
                    File(context.filesDir,"balances.txt").delete()
                    //if(Arrays.equals(Files(context.filesDir,"balances.txt").)
                    File( context.filesDir,"balancestemp.txt").renameTo(File( context.filesDir,"balances.txt"))
                    File( context.filesDir,"balancestemp.txt").delete()
                    BDDHandler(context).parseFichierBalances()
                }



            }
            catch (e: Exception){
                Log.d(TAG, e.message.toString())
            }
        }
    }

    fun sendFile(nomUtilisateur: String, nomFichier:String) {
        GlobalScope.launch {
            try {
                val ftpClient = FTPClient()
                val config = FTPClientConfig()
                ftpClient.configure(config)
                ftpClient.connect(FTPhost, 21)
                ftpClient.replyString
                ftpClient.login(FTPid, FTPpwd)
                Log.d("ah", ftpClient.replyString.toString())
                ftpClient.enterLocalPassiveMode()
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE)

                ftpClient.appendFile(
                    "/$nomUtilisateur/$nomFichier",
                    FileInputStream(File(context.filesDir, nomFichier))
                )
                ftpClient.disconnect()

            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
            }
        }
    }

    fun recupModele(nomUtilisateur: String, espece:String?, bouton:Button?){
        GlobalScope.launch {
            try{
                val ftpClient = FTPClient()
                val config = FTPClientConfig()
                ftpClient.configure(config)
                ftpClient.connect(FTPhost, 21)
                ftpClient.replyString
                ftpClient.login(FTPid, FTPpwd)
                ftpClient.enterLocalPassiveMode()
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
                var outputstream = BufferedOutputStream(FileOutputStream(File( context.filesDir,"modeletemp.txt")))
                if (espece == null){
                    ftpClient.retrieveFile("/$nomUtilisateur/MODELE.TXT", outputstream)
                }else{
                    ftpClient.retrieveFile("/$nomUtilisateur/$espece/MODELE.TXT", outputstream)
                }
                outputstream.close()
                ftpClient.disconnect()

                val fichier = BufferedReader(InputStreamReader(context.openFileInput("modeletemp.txt")))
                val ligne = fichier.readLine()
                Log.d("nomUtilisateur", nomUtilisateur)
                Log.d("nomUtilisateur", nomUtilisateur)
                Log.d("nomUtilisateur", nomUtilisateur)
                Log.d("nomUtilisateur", nomUtilisateur)
                Log.d("nomUtilisateur", nomUtilisateur)
                Log.d("nomUtilisateur", nomUtilisateur)
                Log.d("nomUtilisateur", nomUtilisateur)

                if (espece != null) {
                    Log.d("secteur", espece)
                }
                if (!ligne.isNullOrBlank()){
                    File(context.filesDir,"modele.txt").delete()
                    //if(Arrays.equals(Files(context.filesDir,"balances.txt").)
                    File( context.filesDir,"modeletemp.txt").renameTo(File( context.filesDir,"modele.txt"))
                    File( context.filesDir,"modeletemp.txt").delete()
                    BDDHandler(context).parseFichierModele()
                    if (bouton != null){
                        bouton.visibility = View.VISIBLE
                    }
                }
            }
            catch (e: Exception){
                Log.d(TAG, e.message.toString())
            }
        }
    }

    fun checkMaj(date:String?) {
        GlobalScope.launch {
            try{
                val ftpClient = FTPClient()
                val config = FTPClientConfig()
                ftpClient.configure(config)
                ftpClient.connect(FTPhost, 21)
                ftpClient.replyString
                ftpClient.login(FTPid, FTPpwd)
                Log.d("ftp", ftpClient.replyString.toString())
                ftpClient.enterLocalPassiveMode()
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
                var outputstream = BufferedOutputStream(FileOutputStream(File( context.filesDir,"maj.txt")))
                ftpClient.retrieveFile("/euronutrition/UPD_MAJ_BALANCES.TXT", outputstream)
                outputstream.close()
                ftpClient.disconnect()

                val fichier = BufferedReader(InputStreamReader(context.openFileInput("maj.txt")))
                val ligne = fichier.readLine()
                Log.e("fichier", ligne)

                if (! ligne.isNullOrBlank()){
                    if (ligne != date){
                        getBalances()
                        var edit = context.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE).edit()
                        edit.putString("dateMaj", ligne)
                        edit.apply()
                    }
                }

            }
            catch (e: Exception){
                Log.d(TAG, e.message.toString())
            }
        }
    }

    fun secteurs(secteurs: (liste: MutableList<String>) -> Unit){
        GlobalScope.launch {
            try{
                val ftpClient = FTPClient()
                val config = FTPClientConfig()
                ftpClient.configure(config)
                ftpClient.connect(FTPhost, 21)
                ftpClient.replyString
                ftpClient.login(FTPid, FTPpwd)
                Log.d("ftp", ftpClient.replyString.toString())
                ftpClient.enterLocalPassiveMode()
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
                var outputstream = BufferedOutputStream(FileOutputStream(File( context.filesDir,"secteurs.txt")))
                ftpClient.retrieveFile("/euronutrition/SECTEURS.TXT", outputstream)
                outputstream.close()
                ftpClient.disconnect()

                val fichier = BufferedReader(InputStreamReader(context.openFileInput("secteurs.txt")))
                var ligne = fichier.readLine()
                var liste = mutableListOf<String>()
                while (!ligne.isNullOrBlank()){
                    liste.add(ligne)
                    Log.d("ftp", ligne)
                    ligne = fichier.readLine()
                }
                secteurs(liste)
            }
            catch (e: Exception){
                Log.d(TAG, e.message.toString())
            }
        }
    }



    /*fun existe(nom:String):Boolean{
        var fini = false
        var isDirectory = false
        GlobalScope.launch {
            try {
                val ftpClient = FTPClient()
                val config = FTPClientConfig()
                ftpClient.configure(config)
                ftpClient.connect(FTPhost, 21)
                ftpClient.replyString
                ftpClient.login(FTPid, FTPpwd)
                Log.d("ah", ftpClient.replyString.toString())
                ftpClient.enterLocalPassiveMode()
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE)

                var liste = ftpClient.listDirectories()
                isDirectory = nom in liste
                ftpClient.disconnect()
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
            }
            fini = true

        }
        while (fini == false){

        }
        return isDirectory
    }*/

}