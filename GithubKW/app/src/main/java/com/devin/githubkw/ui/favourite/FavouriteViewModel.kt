package com.devin.githubkw.ui.favourite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.devin.githubkw.data.local.FavouriteUser
import com.devin.githubkw.data.local.FavouriteUserDao
import com.devin.githubkw.data.local.UserDatabase

class FavouriteViewModel(application: Application): AndroidViewModel(application) {
    private var userDao: FavouriteUserDao?
    private var userDb: UserDatabase?

    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun getFavoriteuser(): LiveData<List<FavouriteUser>>?{
        return userDao?.getFavoriteUser()
    }
}