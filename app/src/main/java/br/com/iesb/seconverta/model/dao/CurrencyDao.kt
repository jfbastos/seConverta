package br.com.iesb.seconverta.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.iesb.seconverta.model.CurrencyItem

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrency(currency : CurrencyItem)

    @get: Query("SELECT * FROM currency")
    val currency : LiveData<List<CurrencyItem>>

    @Query("DELETE FROM currency WHERE code = :code")
    fun delete(code : String)

}