package com.devin.githubkw.backend

import com.devin.githubkw.BuildConfig
import com.devin.githubkw.data.connect.ConnGithubAPI
import com.devin.githubkw.data.connect.DetailUser
import com.devin.githubkw.data.connect.Userlist
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    companion object{
        const val key = BuildConfig.apiKey
    }
    @GET("search/users")
    @Headers("Authorization: token $key")

    fun getSearchUsers(
        @Query("q") query: String
    ): Call<ConnGithubAPI>

    @GET("users/{username}")
    @Headers("Authorization: token $key")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUser>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $key")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<Userlist>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $key")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<Userlist>>

    @GET("users")
    @Headers("Authorization: token $key")
    fun getRandomUsers(): Call<ArrayList<Userlist>>
}