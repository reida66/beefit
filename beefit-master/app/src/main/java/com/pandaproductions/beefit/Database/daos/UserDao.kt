package com.pandaproductions.beefit.Database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pandaproductions.beefit.Models.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getUsers(): List<User>

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUser(id: Int): User

    @Insert
    fun insert(user: User)

    @Update
    fun updateUser(user: User)
}