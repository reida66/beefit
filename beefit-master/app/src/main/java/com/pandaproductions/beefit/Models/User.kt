package com.pandaproductions.beefit.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.pandaproductions.beefit.Database.Converters
import com.pandaproductions.beefit.ENUM.FairyBreadType

@Entity(tableName = "users")
@TypeConverters(Converters::class)
class User(var name: String, var age: Int, var stepGoal: Int, var fairyBread: FairyBreadType) {

    @PrimaryKey
    var id: Long = 0
    var points: Int = 0

    override fun toString(): String {
        return "User(name='$name', age=$age, stepGoal=$stepGoal, fairyBread=$fairyBread)"
    }
}