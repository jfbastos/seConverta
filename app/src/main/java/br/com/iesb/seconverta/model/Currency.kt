package br.com.iesb.seconverta.model

import androidx.room.Entity
import androidx.room.PrimaryKey

class CountryCode {
    companion object {
        val countries: HashMap<String, String> = HashMap()

        fun addCountrie(key: String, value: String) {
            countries[key] = value
        }


        fun getCountriesList(): ArrayList<Country> {
            val listCountries: ArrayList<Country> = arrayListOf()

            countries.keys.forEach { key ->
                listCountries.add(Country(key, countries.getValue(key)))
            }

            listCountries.sortBy { it.name }

            return listCountries
        }
    }
}


data class Country(val code: String, val name: String) : java.io.Serializable

@Entity(tableName = "currency")
data class Currency(
    @PrimaryKey val code: String,
    val value: Double,
    val lastUpdate: String?,
    val countryName: String
) : java.io.Serializable
