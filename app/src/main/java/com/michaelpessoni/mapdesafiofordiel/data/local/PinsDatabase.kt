package com.michaelpessoni.mapdesafiofordiel.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.michaelpessoni.mapdesafiofordiel.data.Pin

@Database(entities = [Pin::class], version = 3)
abstract class PinsDatabase : RoomDatabase() {

    abstract val pinsDatabaseDAO: PinsDAO

    companion object {

        @Volatile
        private var INSTANCE: PinsDatabase? = null

        fun getInstance(context: Context): PinsDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PinsDatabase::class.java,
                        "pins_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
                INSTANCE = instance

                return instance
            }
        }
    }

}