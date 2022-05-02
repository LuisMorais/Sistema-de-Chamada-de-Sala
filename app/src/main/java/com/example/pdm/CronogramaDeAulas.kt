package com.example.pdm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class CronogramaDeAulas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cronograma_de_aulas)


        val btnSairCronograma: Button = findViewById(R.id.btnSairCronograma)
        btnSairCronograma.setOnClickListener {
            Toast.makeText(applicationContext, "Voltando para a área do aluno!", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}