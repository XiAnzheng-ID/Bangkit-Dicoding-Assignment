package com.devin.storykw.ui.drawer.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.devin.storykw.backend.UserRepository
import com.devin.storykw.backend.conn.ListStoryItem

class StoryViewModel(private val repository: UserRepository) : ViewModel() {
    val stories: LiveData<PagingData<ListStoryItem>> =
        repository.stories().cachedIn(viewModelScope)

    fun refresh() {
        repository.refresh()
    }
}