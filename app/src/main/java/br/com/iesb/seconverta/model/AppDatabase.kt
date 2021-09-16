package br.com.iesb.seconverta.model

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.iesb.seconverta.model.dao.CurrencyDao

@Database(entities = [CurrencyItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun CurrencyDao(): CurrencyDao
}