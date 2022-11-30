package com.pandaproductions.beefit.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.pandaproductions.beefit.Database.Converters
import com.pandaproductions.beefit.ENUM.PetType

@Entity(tableName = "pets")
@TypeConverters(Converters::class)
class Pet(
    var name: String,
    var type: PetType
) {
    @PrimaryKey
    var id: Long = 0

    var happiness: Int = 100
    var health: Int = 100

    fun incrementHealth(num: Int) {
        health = maxOf(minOf(100, health + num), 0)
    }

    override fun toString(): String {
        return "pet(name='$name', type='${type.type}', happiness=$happiness, health=$health)"
    }

}
