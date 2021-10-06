package br.com.iesb.seconverta.utils

import android.app.Activity
import android.widget.Toast
import java.math.BigDecimal
import java.util.*

class Formaters {

    companion object {
        fun formatMoneyToString(valor: Double): String {
            return try {
                String.format(Locale("pt","BR"), "%.2f", valor)
            } catch (e: Exception) {
                "0.00"
            }
        }

        fun formatStringToDouble(value: String, activity: Activity): Double {
            return try {
                value.toDouble()
            } catch (e: Exception) {
                Toast.makeText(activity, "Use dot instead of comma!", Toast.LENGTH_SHORT).show()
                return 1.0
            }
        }

    }
}