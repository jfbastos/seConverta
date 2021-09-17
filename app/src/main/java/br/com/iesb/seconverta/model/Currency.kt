package br.com.iesb.seconverta.model

import androidx.room.Entity
import androidx.room.PrimaryKey

class CountryCode() {
    companion object {
        private val countries: HashMap<String, String> = HashMap()

        fun addCountrie(key: String, value: String) {
            countries[key] = value
        }

        fun getCountries(): HashMap<String, String> {
            return countries
        }

        fun getOneCountry(countryCode: String): String {
            return try {
                countries.getValue(countryCode)
            } catch (error: NoSuchElementException) {
                "Not found country"
            }
        }
    }
}

data class CurrencyValue(
    val date: String?,
    val brl: Double
)

@Entity(tableName = "currency")
data class CurrencyItem(
    @PrimaryKey val code: String,
    val value: Double
)
