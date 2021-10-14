package br.com.iesb.seconverta.utils

import android.app.Activity
import android.widget.Toast
import java.util.*

class Formaters {

    companion object {
        fun formatMoneyToString(valor: Double): String {
            return try {
                String.format(Locale("pt", "BR"), "%.2f", valor)
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

        fun formatDate(date: String?): String {
            val dateArray = date?.split("-")
            return String.format("${dateArray?.get(2)}/${dateArray?.get(1)}/${dateArray?.get(0)}")
        }

    }
}