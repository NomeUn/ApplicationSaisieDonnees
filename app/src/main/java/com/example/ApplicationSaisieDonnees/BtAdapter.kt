 package com.example.ApplicationSaisieDonnees

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context

import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

class BtAdapter(
    var listeAppareils: MutableSet<BluetoothDevice>,
    val ctx : Context,
    val changeVisibility: (btAdapter: BtAdapter?) -> Unit
) : RecyclerView.Adapter<BtAdapter.BtViewHolder>() {

    lateinit var socket: BluetoothSocket
    lateinit var listeEditText: MutableList<EditText>
    var liste: MutableList<BluetoothDevice> = mutableListOf()
    var bal = ctx.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE).getString("bal", "")?.let { BDDHandler(ctx).getBalance(it) }


    inner class BtViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BtViewHolder {
        for (i in listeAppareils){
            liste.add(i)
        }
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bt, parent, false)
        return BtViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: BtViewHolder, position: Int) {
        holder.itemView.apply {
            if (position%2 == 0){
                this.setBackgroundColor(Color.LTGRAY)
            }
            else{
                this.setBackgroundColor(Color.WHITE)
            }

            findViewById<TextView>(R.id.tvBt).text = liste[position].name
            this.setOnClickListener {
                //progressBar.visibility = View.VISIBLE


                try {

                    socket = liste[position].createInsecureRfcommSocketToServiceRecord(liste[position].uuids[0].uuid)
                    socket.connect()

                    Toast.makeText(ctx, if (socket.isConnected)"connecté" else "connexion impossible", Toast.LENGTH_SHORT).show()


                    var inputS = socket.inputStream
                    var outputStream = socket.outputStream

                    changeVisibility(this@BtAdapter)

                    //outputStream.write("test".toByteArray())

                    //Toast.makeText(context, inputS.read().toString(), Toast.LENGTH_SHORT).show()
                    GlobalScope.launch {
                        var buffer = ByteArray(256)
                        var bytes:Int
                        var msg = ""
                        while (true){
                            try {
                                bytes = inputS.read(buffer)
                                var temp = String(buffer,0, bytes)
                                Log.d("buffer", buffer.toString())
                                Log.d("temp", temp)
                                if (temp.contains("\n",false)){
                                    Log.d("oui", "oui oui oui")
                                }
                                msg += temp.replace(" ", "")
                                if (msg.contains("\n",false)) {
                                    for (i in listeEditText) {
                                        if (i.hasFocus()) {
                                            Log.d("bt", msg)
                                            //Log.d("bt", buffer.asUByteArray().toString())
                                            i.setText(getPoids(msg))
                                            msg = ""
                                            if (listeEditText.last() == i) {
                                                listeEditText[0].requestFocus()
                                            } else {
                                                listeEditText[listeEditText.indexOf(i) + 1].requestFocus()

                                            }
                                        }
                                    }
                                }
                                //Toast.makeText(context, msg,Toast.LENGTH_SHORT).show()
                            }
                            catch (e : Exception){
                                //Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                catch (e:IOException){
                    Toast.makeText(ctx, "connexion impossible", Toast.LENGTH_LONG).show()
                }

                /*
                var c = inputS.read().toChar()
                var chaine = ""
                Toast.makeText(context, c.toString(), Toast.LENGTH_SHORT).show()
                */
            }

        }
    }


    override fun getItemCount(): Int {
        return listeAppareils.size
    }

    fun getPoids(msg:String): String? {
        Log.d("msg",msg)
        var temp = msg.dropLastWhile { ! it.isDigit()  }.takeLastWhile { it.isDigit() || it == '.' || it == ','}
        Log.d("temp1",temp)
        if (bal != null){
            while (temp.toFloat() > bal!!.porte){
                temp = temp.drop(1)
                Log.d("poids", temp)
            }
        }
        Log.d("temp2",temp)
        temp = temp.dropWhile { it == '0' }
        Log.d("temp3",temp)
        if(temp.contains('.', true) || temp.contains(',',true)){
            temp = temp.dropLastWhile { it == '0' }
        }
        Log.d("temp4",temp)
        return temp
    }
}