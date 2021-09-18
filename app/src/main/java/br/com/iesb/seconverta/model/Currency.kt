package br.com.iesb.seconverta.model

import androidx.room.Entity
import androidx.room.PrimaryKey

class CountryCode() {
    companion object {
        private val countries: HashMap<String, String> = HashMap()

        fun addCountrie(key: String, value: String) {
            countries[key] = value
        }


        fun getOneCountry(countryCode: String): String {
            return try {
                countries.getValue(countryCode)
            } catch (error: NoSuchElementException) {
                "Not found country"
            }
        }

        fun getCountriesList(): List<Country> {
            val listCountries: MutableList<Country> = mutableListOf()

            countries.keys.forEach { key ->
                listCountries.add(Country(key, countries.getValue(key)))
            }

            listCountries.sortBy { it.name }

            return listCountries
        }
    }
}

data class Country(val code: String, val name: String)

data class CurrencyValue(
    val date: String?,
    val brl: Double
)

@Entity(tableName = "currency")
data class CurrencyItem(
    @PrimaryKey val code: String,
    val value: Double
)
