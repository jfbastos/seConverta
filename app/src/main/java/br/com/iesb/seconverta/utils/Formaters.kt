package br.com.iesb.seconverta.utils

import java.lang.Exception
import java.util.*

class Formaters {

    companion object {
        fun formatMoneyToString(valor: Double?): String {
           return try{
                String.format(Locale.ENGLISH, "Currency: %.2f", valor)
            }catch (e : Exception){
                "R$ 0.00"
            }
        }

    }
}