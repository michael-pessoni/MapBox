package com.michaelpessoni.mapdesafiofordiel.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.michaelpessoni.mapdesafiofordiel.data.Pin

@Dao
interface PinsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pin: Pin)

    @Query("SELECT * FROM pins_table")
    fun observeAllPins() : LiveData<List<Pin>>

}