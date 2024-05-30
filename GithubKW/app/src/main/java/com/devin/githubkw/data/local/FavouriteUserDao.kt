package com.devin.githubkw.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavouriteUserDao {
    @Insert
    suspend fun addToFavorite(favouriteUser: FavouriteUser)

    @Query("SELECT * FROM  favorite_list")
    fun getFavoriteUser():LiveData<List<FavouriteUser>>

    @Query("SELECT count(*) FROM favorite_list WHERE favorite_list.id = :id")
    suspend fun checkUser(id: Int) : Int

    @Query("DELETE FROM favorite_list WHERE favorite_list.id = :id")
    suspend fun removeFromFavorite(id: Int) : Int
}