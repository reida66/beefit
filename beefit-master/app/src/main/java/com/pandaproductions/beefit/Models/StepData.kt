package com.pandaproductions.beefit.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "step_data")
class StepData(var date: String, var steps: Int) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0


    override fun toString(): String {
        return "StepData(date='$date', steps='$steps')"
    }
}