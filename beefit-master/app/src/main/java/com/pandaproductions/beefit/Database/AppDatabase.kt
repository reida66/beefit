package com.pandaproductions.beefit.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pandaproductions.beefit.Database.daos.PetDao
import com.pandaproductions.beefit.Database.daos.StepDataDao
import com.pandaproductions.beefit.Database.daos.UserDao
import com.pandaproductions.beefit.Models.Pet
import com.pandaproductions.beefit.Models.StepData
import com.pandaproductions.beefit.Models.User

@Database(entities = [(Pet::class), (User::class), (StepData::class)], version = 6)
abstract class AppDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao
    abstract fun userDao(): UserDao
    abstract fun stepDataDao(): StepDataDao

    companion object {
        private var databaseInstance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (databaseInstance == null) {
                databaseInstance = Room
                    .databaseBuilder(context.applicationContext, AppDatabase::class.java, "database")
                    .fallbackToDestructiveMigration()
                    .build()

            }
            return databaseInstance!!
        }
    }
}
