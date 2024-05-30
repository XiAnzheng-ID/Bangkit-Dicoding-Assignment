package com.devin.githubkw.backend

import com.devin.githubkw.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val endpoint = BuildConfig.endpointWeb

    private val retrofit = Retrofit.Builder()
        .baseUrl(endpoint)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiGithub = retrofit.create(Api::class.java)
}