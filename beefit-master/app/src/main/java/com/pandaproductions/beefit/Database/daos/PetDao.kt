package com.pandaproductions.beefit.Database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pandaproductions.beefit.Models.Pet

@Dao
interface PetDao {

    @Query("SELECT * FROM pets WHERE id = :id")
    fun getPet(id: Int): Pet

    @Query("SELECT * FROM pets")
    fun getPets(): MutableList<Pet>

    @Insert
    fun insert(pet: Pet)

    @Update
    fun updatePet(pet: Pet)
}