package com.example.formulasapp

import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var inputContainer: LinearLayout
    private lateinit var btnCalcular: Button
    private lateinit var tvResultado: TextView
    private lateinit var scrollView: ScrollView
    private lateinit var imgFormula: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputContainer = findViewById(R.id.inputContainer)
        btnCalcular = findViewById(R.id.btnCalcular)
        tvResultado = findViewById(R.id.tvResultado)
        scrollView = findViewById(R.id.scrollView)
        imgFormula = findViewById(R.id.imgFormula)

        val idFormula = intent.getIntExtra("formula_id", 0)
        mostrarCampos(idFormula)

        btnCalcular.setOnClickListener {
            calcularResultado(idFormula)
        }
    }

    private fun mostrarCampos(idFormula: Int) {
        inputContainer.removeAllViews()

        val hints: List<Int> = when (idFormula) {
            0 -> listOf(R.string.hint_base, R.string.hint_altura)
            1 -> listOf(R.string.hint_distancia, R.string.hint_tiempo)
            2 -> listOf(R.string.hint_a, R.string.hint_b, R.string.hint_c)
            3 -> listOf(R.string.hint_corriente, R.string.hint_resistencia)
            else -> emptyList()
        }

        val tituloFormula = TextView(this).apply {
            text = when (idFormula) {
                0 -> getString(R.string.titulo_area)
                1 -> getString(R.string.titulo_velocidad)
                2 -> getString(R.string.titulo_cuadratica)
                3 -> getString(R.string.titulo_ohm)
                else -> getString(R.string.titulo_default)
            }
            textSize = resources.getDimension(R.dimen.titulo_formula_text_size) / resources.displayMetrics.scaledDensity
            setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.black))
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 32)
            }
        }
        inputContainer.addView(tituloFormula)

        when (idFormula) {
            0 -> imgFormula.setImageResource(R.drawable.formula_triangulo)
            1 -> imgFormula.setImageResource(R.drawable.formula_velocidad)
            2 -> imgFormula.setImageResource(R.drawable.formula_cuadratica)
            3 -> imgFormula.setImageResource(R.drawable.formula_ohm)
        }

        for (hintResId in hints) {
            val fila = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, 16)
                }
            }

            val etiqueta = TextView(this).apply {
                text = getString(hintResId) + ":"
                textSize = resources.getDimension(R.dimen.input_label_text_size) / resources.displayMetrics.scaledDensity
                setTextColor(ContextCompat.getColor(context, R.color.teal_700))
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                gravity = Gravity.END
            }

            val campo = EditText(this).apply {
                hint = getString(hintResId)
                textSize = resources.getDimension(R.dimen.input_text_size) / resources.displayMetrics.scaledDensity
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
                background = ContextCompat.getDrawable(context, R.drawable.input_background)
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f).apply {
                    setMargins(16, 0, 0, 0)
                }
            }

            fila.addView(etiqueta)
            fila.addView(campo)
            inputContainer.addView(fila)
        }

        scrollView.post {
            scrollView.fullScroll(ScrollView.FOCUS_UP)
        }
    }


    private fun calcularResultado(idFormula: Int) {
        val valores = mutableListOf<Double>()

        for (i in 0 until inputContainer.childCount) {
            val fila = inputContainer.getChildAt(i)
            if (fila is LinearLayout) {
                for (j in 0 until fila.childCount) {
                    val view = fila.getChildAt(j)
                    if (view is EditText) {
                        val texto = view.text.toString()
                        if (texto.isEmpty()) {
                            Toast.makeText(this, getString(R.string.error_campos), Toast.LENGTH_SHORT).show()
                            return
                        }
                        val valor = texto.toDoubleOrNull()
                        if (valor == null) {
                            Toast.makeText(this, getString(R.string.error_campos), Toast.LENGTH_SHORT).show()
                            return
                        }
                        if (idFormula != 2 && valor == 0.0) {
                            Toast.makeText(this, getString(R.string.error_cero), Toast.LENGTH_SHORT).show()
                            return
                        }
                        if (idFormula != 2 && valor < 0.0) {
                            Toast.makeText(this, getString(R.string.error_negativos), Toast.LENGTH_SHORT).show()
                            return
                        }
                        valores.add(valor)
                    }
                }
            }
        }

        val resultadoTexto = when (idFormula) {
            0 -> {
                val base = valores[0]
                val altura = valores[1]
                val area = 0.5 * base * altura
                "${getString(R.string.resultado)} $area"
            }
            1 -> {
                val distancia = valores[0]
                val tiempo = valores[1]
                if (tiempo == 0.0) {
                    getString(R.string.error_division_cero)
                } else {
                    val velocidad = distancia / tiempo
                    "${getString(R.string.resultado)} $velocidad"
                }
            }
            2 -> {
                val a = valores[0]
                val b = valores[1]
                val c = valores[2]

                if (a == 0.0) {
                    getString(R.string.error_multiplicacion_cero)
                } else {
                    val discriminante = b * b - 4 * a * c
                    if (discriminante < 0) {
                        getString(R.string.error_discriminante)
                    } else {
                        val x1 = (-b + Math.sqrt(discriminante)) / (2 * a)
                        val x2 = (-b - Math.sqrt(discriminante)) / (2 * a)
                        "${getString(R.string.resultado)} x1 = %.2f, x2 = %.2f".format(x1, x2)
                    }
                }
            }
            3 -> {
                val corriente = valores[0]
                val resistencia = valores[1]
                val voltaje = corriente * resistencia
                "${getString(R.string.resultado)} $voltaje"
            }
            else -> getString(R.string.formula_desconocida)
        }

        tvResultado.text = resultadoTexto
    }
}
