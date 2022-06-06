package com.example.corutinesretrofitroom

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.corutinesretrofitroom.roomDb.PersonDatabase

class App : Application() {

    private var _personDatabase: PersonDatabase? = null
    val personDatabase get() = requireNotNull(_personDatabase)

    override fun onCreate() {
        super.onCreate()
        _personDatabase =
            Room.databaseBuilder(this, PersonDatabase::class.java, DATABASE_NAME).build()
    }

    companion object {
        private const val DATABASE_NAME = "personDatabase_db"
    }
}

val Context.personDatabase: PersonDatabase
    get() = when (this) {
        is App -> personDatabase
        else -> applicationContext.personDatabase
    }