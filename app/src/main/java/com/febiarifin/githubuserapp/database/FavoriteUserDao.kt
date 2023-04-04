package com.febiarifin.githubuserapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg favorite_user: FavoriteUser)

    @Delete
    fun delete(favorite_user: FavoriteUser)

    @Query("SELECT * FROM FavoriteUser")
    fun getAll(): List<FavoriteUser>

    @Query("SELECT * FROM FavoriteUser WHERE username = :username LIMIT 1")
    fun findByUsername(username: String): FavoriteUser
}