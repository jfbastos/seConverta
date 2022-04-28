package br.com.iesb.seconverta.model

class CountryCode {
    companion object {
        val countries: HashMap<String, String> = HashMap()

        fun addCountry(key: String, value: String) {
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