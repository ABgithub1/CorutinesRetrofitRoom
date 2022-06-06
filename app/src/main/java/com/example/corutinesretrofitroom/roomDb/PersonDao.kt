package com.example.corutinesretrofitroom.roomDb

import androidx.room.*
import com.example.corutinesretrofitroom.data.Person
import kotlinx.coroutines.flow.Flow


@Dao
interface PersonDao {

    @Query("SELECT * FROM person")
    suspend fun getAll(): List<Person>

    @Query("SELECT * FROM person WHERE id IN (:personId)")
    suspend fun loadAllByIds(personId: IntArray): List<Person>

    @Query("SELECT * FROM person WHERE id LIKE (:id)")
    suspend fun loadPersonById(id: Long): Person

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(person: Person)

    @Query("SELECT * FROM person")
    fun subscribeChanges(): Flow<List<Person>>

    @Delete
    suspend fun delete(person: Person)

}