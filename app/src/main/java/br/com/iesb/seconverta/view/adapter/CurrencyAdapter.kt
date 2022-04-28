package br.com.iesb.seconverta.view.adapter

import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.iesb.seconverta.databinding.CurrencyItemBinding
import br.com.iesb.seconverta.model.Currency
import br.com.iesb.seconverta.utils.Formatters
import com.google.android.material.textfield.TextInputEditText

class CurrencyAdapter(valueEditText: TextInputEditText) : ListAdapter<Currency, CurrencyAdapter.CurrencyViewHolder>(differCallback) {

    private val valueToConvert = valueEditText

    companion object{
        private val differCallback : DiffUtil.ItemCallback<Currency> = object: DiffUtil.ItemCallback<Currency>(){
            override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
                return oldItem.code == newItem.code
            }
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class CurrencyViewHolder(private val itemBinding: CurrencyItemBinding, private val conversionValue: Editable?) :
        RecyclerView.ViewHolder(itemBinding.root) {

         fun bind(currency: Currency) {
            val currencyValueText = "${Formatters.formatMoneyToString(currency.value)} unit."
            val totalValueText = "${currency.code} ${
                Formatters.formatMoneyToString(
                    Formatters.formatStringToDouble(
                        conversionValue.toString(),
                    ) * currency.value
                )
            }"

            itemBinding.currencyName.text = currency.code
            itemBinding.currencyDate.text = Formatters.formatDate(currency.lastUpdate)
            itemBinding.currencyItemValue.text = currencyValueText
            itemBinding.totalCurrencyValue.text = totalValueText


            itemBinding.root.setOnLongClickListener {
                onLongItemClickListener.invoke(currency)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding = CurrencyItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding, valueToConvert.text)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    private lateinit var onLongItemClickListener: ((Currency) -> Boolean)

    fun setOnLongItemClickListener(longClickListener: (Currency) -> Boolean) {
        onLongItemClickListener = longClickListener
    }
}