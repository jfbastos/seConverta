package br.com.iesb.seconverta.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.iesb.seconverta.R
import br.com.iesb.seconverta.databinding.CountryItemBinding
import br.com.iesb.seconverta.model.Country

class ListCountriesAdapter(
    private val countries: ArrayList<Country>, private val selectedCountries: ArrayList<Country>
) :
    RecyclerView.Adapter<ListCountriesAdapter.CountriesViewHolder>() {


    inner class CountriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemBinding = CountryItemBinding.bind(itemView)

        fun bind(country: Country) {
            itemBinding.currName.text = country.name
            itemBinding.currNameDetail.text = country.code
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_item, parent, false)
        return CountriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        holder.bind(countries[position])

        holder.itemBinding.checkBox.isChecked = selectedCountries.contains(countries[position])

        holder.itemBinding.checkBox.setOnClickListener {
            if (holder.itemBinding.checkBox.isChecked) {
                selectedCountries.add(countries[position])
            } else if (selectedCountries.contains(countries[position])) {
                selectedCountries.remove(countries[position])
            }
        }
    }

    override fun getItemCount(): Int = countries.size

    fun update(countries: List<Country>) {
        this.countries.clear()
        this.countries.addAll(countries)
        notifyDataSetChanged()
    }

    fun getSelectedCountries(): List<Country> {
        return selectedCountries
    }
}