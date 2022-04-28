package br.com.iesb.seconverta.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class Currency(
    @PrimaryKey val code: String,
    val value: Double,
    val lastUpdate: String?,
    val countryName: String
) : java.io.Serializable
