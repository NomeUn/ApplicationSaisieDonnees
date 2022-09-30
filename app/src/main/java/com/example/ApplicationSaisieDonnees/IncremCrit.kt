package com.example.ApplicationSaisieDonnees

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class IncremCrit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_increm_crit)

        val tvIncrem = findViewById<TextView>(R.id.tvIncrem)
        val btnNo = findViewById<Button>(R.id.btnNo)
        val btnYes = findViewById<Button>(R.id.btnYes)

        tvIncrem.setText("Voulez-vous incrémenter le critère "+ intent.getStringExtra("EXTRA_NOMCRITERE"))

        btnNo.setOnClickListener {
            setResult(0)
            finish()
        }

        btnYes.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }


    }
}