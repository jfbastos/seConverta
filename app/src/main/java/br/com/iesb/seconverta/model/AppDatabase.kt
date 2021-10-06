package br.com.iesb.seconverta.model

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.iesb.seconverta.model.dao.CurrencyDao

@Database(entities = [Currency::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun CurrencyDao(): CurrencyDao
}