package com.pandaproductions.beefit.Database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pandaproductions.beefit.Models.StepData

@Dao
interface StepDataDao {

    @Query("SELECT * FROM step_data")
    fun getStepData(): MutableList<StepData>

    @Insert
    fun insert(stepData: StepData): Long

    @Update
    fun update(stepData: StepData)
}