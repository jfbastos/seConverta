package br.com.iesb.seconverta.utils

import android.app.Activity
import android.content.ContentResolver
import android.provider.Settings.Global.getString
import android.widget.Toast
import br.com.iesb.seconverta.R
import java.util.*

object Formatters {
        fun formatMoneyToString(valor: Double): String {
            return try {
                String.format(Locale("pt", "BR"), "%.2f", valor)
            } catch (e: Exception) {
                "0.00"
            }
        }

        fun formatStringToDouble(value: String): Double {
            return try {
                if(value.isEmpty()){
                    0.0
                }else{
                    value.toDouble()
                }
            } catch (e: Exception) {
                return 0.0
            }
        }

        fun formatDate(date: String?): String {
            val dateArray = date?.split("-")
            return String.format("${dateArray?.get(2)}/${dateArray?.get(1)}/${dateArray?.get(0)}")
        }

}