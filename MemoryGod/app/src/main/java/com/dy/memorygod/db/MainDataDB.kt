package com.dy.memorygod.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dy.memorygod.converter.MainDataTypeConverter
import com.dy.memorygod.dao.MainDataDao
import com.dy.memorygod.entity.MainDataEntity

@Database(entities = [MainDataEntity::class], version = 1)
@TypeConverters(MainDataTypeConverter::class)
abstract class MainDataDB : RoomDatabase() {

    abstract fun mainDataDao(): MainDataDao

    companion object {
        private var INSTANCE: MainDataDB? = null

        fun getInstance(context: Context): MainDataDB? {
            if (INSTANCE == null) {
                synchronized(MainDataDB::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MainDataDB::class.java, "main_data.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }

}