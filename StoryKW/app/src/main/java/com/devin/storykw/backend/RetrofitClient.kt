package com.devin.storykw.backend

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.devin.storykw.BuildConfig
import com.devin.storykw.backend.pref.UserPreference
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

object RetrofitClient {
    private const val endpoint = BuildConfig.endpointWeb
    private var context: Context? = null

    fun init(context: Context) {
        RetrofitClient.context = context
    }

    private val retrofit by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

        Retrofit.Builder()
            .baseUrl(endpoint)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiStory: Api by lazy {
        retrofit.create(Api::class.java)
    }

    private class AuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val originalRequest: Request = chain.request()
            val token = context?.let { UserPreference.getInstance(it.dataStore).getToken() }
            val requestWithToken: Request = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()

            return chain.proceed(requestWithToken)
        }
    }
}