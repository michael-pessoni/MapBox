package com.michaelpessoni.mapdesafiofordiel.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pins_table")
data class Pin(

    @ColumnInfo(name = "latitude")
    var latitude: Double,

    @ColumnInfo(name = "longitude")
    var longitude: Double,

    @PrimaryKey(autoGenerate = true)
    var pointId: Long = 0L
)
