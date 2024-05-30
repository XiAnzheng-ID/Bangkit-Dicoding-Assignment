package com.devin.storykw.backend

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.devin.storykw.backend.conn.ListStoryItem
import com.devin.storykw.backend.pref.UserModel
import com.devin.storykw.backend.pref.UserPreference
import com.devin.storykw.ui.drawer.home.StoryPagingSource
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference
) {
    private val storyPagingSource = StoryPagingSource()
    fun stories(): LiveData<PagingData<ListStoryItem>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { StoryPagingSource() }
    ).liveData

    fun refresh() {
        storyPagingSource.invalidate()
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference)
            }.also { instance = it }
    }
}