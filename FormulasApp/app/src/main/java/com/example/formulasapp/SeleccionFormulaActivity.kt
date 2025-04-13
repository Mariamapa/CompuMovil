package com.example.formulasapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class SeleccionFormulaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_formula)

        // Mostrar el GIF
        val gifView = findViewById<ImageView>(R.id.gifView)
        Glide.with(this)
            .asGif()
            .load(R.drawable.brain)
            .into(gifView)

        // Configurar el Spinner
        val spinner = findViewById<Spinner>(R.id.spinnerFormulas)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.formulas_array,
            R.layout.spinner_dropdown_item
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.adapter = adapter


        // Botón para abrir la fórmula seleccionada
        val btnIr = findViewById<Button>(R.id.btnIr)
        btnIr.setOnClickListener {
            val seleccion = spinner.selectedItemPosition
            abrirFormula(seleccion)
        }
    }

    private fun abrirFormula(idFormula: Int) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("formula_id", idFormula)
        startActivity(intent)
    }
}
