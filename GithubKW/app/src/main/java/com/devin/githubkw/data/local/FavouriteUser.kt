package com.devin.githubkw.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_list")
data class FavouriteUser(
    val login: String,

    @PrimaryKey
    val id: Int,
    val avatar_url : String
):Serializable
