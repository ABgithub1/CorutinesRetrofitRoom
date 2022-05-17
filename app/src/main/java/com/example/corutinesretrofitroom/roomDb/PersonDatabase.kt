package com.example.corutinesretrofitroom.roomDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.corutinesretrofitroom.data.Person

@Database(entities = [Person::class], version = 1)
abstract class PersonDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao

    companion object {
        fun buildDatabase(context: Context, dbName: String): PersonDatabase {
            return Room.databaseBuilder(context, PersonDatabase::class.java, dbName).build()
        }
    }
}
