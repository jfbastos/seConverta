package br.com.iesb.seconverta.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class CountryCode (
    val code : String?,
    val country : String?
)

data class CurrencyValue(
    val date : String?,
    val brl : Double
)

@Entity(tableName = "currency")
data class CurrencyItem(
    @PrimaryKey val code : String,
    val value : Double
)
