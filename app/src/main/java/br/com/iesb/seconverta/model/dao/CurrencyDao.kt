package br.com.iesb.seconverta.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.iesb.seconverta.model.Currency

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrency(currency: Currency)

    @get: Query("SELECT * FROM currency")
    val currency: LiveData<List<Currency>>

    @Query("DELETE FROM currency WHERE code = :code")
    fun delete(code: String)

}