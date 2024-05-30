package com.devin.storykw.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devin.storykw.backend.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel()  {
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}