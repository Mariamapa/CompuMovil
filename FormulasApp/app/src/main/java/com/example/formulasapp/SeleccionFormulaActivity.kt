package com.example.formulasapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class SeleccionFormulaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_formula)

        val gifView = findViewById<ImageView>(R.id.gifView)
        Glide.with(this)
            .asGif()
            .load(R.drawable.brain)
            .into(gifView)

        findViewById<Button>(R.id.btnTriangulo).setOnClickListener {
            abrirFormula(0)
        }
        findViewById<Button>(R.id.btnVelocidad).setOnClickListener {
            abrirFormula(1)
        }
        findViewById<Button>(R.id.btnCuadratica).setOnClickListener {
            abrirFormula(2)
        }
        findViewById<Button>(R.id.btnOhm).setOnClickListener {
            abrirFormula(3)
        }
    }

    private fun abrirFormula(idFormula: Int) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("formula_id", idFormula)
        startActivity(intent)
    }
}
