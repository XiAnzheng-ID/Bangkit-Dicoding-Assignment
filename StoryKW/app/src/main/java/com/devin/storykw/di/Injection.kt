package com.devin.storykw.di

import android.content.Context
import com.devin.storykw.backend.RetrofitClient
import com.devin.storykw.backend.UserRepository
import com.devin.storykw.backend.pref.UserPreference
import com.devin.storykw.backend.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    fun initializeRetrofit(context: Context) {
        RetrofitClient.init(context)
    }
}