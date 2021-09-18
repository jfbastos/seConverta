package br.com.iesb.seconverta.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.iesb.seconverta.R
import br.com.iesb.seconverta.databinding.CurrencyContentTypeBinding
import br.com.iesb.seconverta.model.Country

class ListCountriesAdapter(private val countries: ArrayList<Country>) :
    RecyclerView.Adapter<ListCountriesAdapter.CountriesViewHolder>() {

    companion object{
        val selectedItems = arrayListOf<Country>()
    }

    class CountriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemBinding = CurrencyContentTypeBinding.bind(itemView)

        fun bind(country : Country) {
            itemBinding.currName.text = country.name
            itemBinding.currNameDetail.text = country.code

                itemBinding.checkBox.setOnClickListener {
                    if(itemBinding.checkBox.isChecked) {
                       selectedItems.add(country)
                    }else if(selectedItems.contains(country)){
                        selectedItems.remove(country)
                    }
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.currency_content_type, parent, false)
        return CountriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
            holder.bind(countries[position])
    }

    override fun getItemCount(): Int = countries.size

    fun update(countries : List<Country>){
        this.countries.clear()
        this.countries.addAll(countries)
        notifyItemRangeChanged(0, countries.size)
    }

}