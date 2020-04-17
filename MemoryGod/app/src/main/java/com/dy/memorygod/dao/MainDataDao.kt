package com.dy.memorygod.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dy.memorygod.entity.MainDataEntity

@Dao
interface MainDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: MainDataEntity)

    @Query("SELECT * FROM main_data_table")
    fun getAll(): List<MainDataEntity>

    @Query("DELETE FROM main_data_table")
    fun deleteAll()

}