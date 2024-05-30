package com.devin.storykw.backend

import com.devin.storykw.backend.conn.FileUploadResponse
import com.devin.storykw.backend.conn.LoginResponse
import com.devin.storykw.backend.conn.RegisterResponse
import com.devin.storykw.backend.conn.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface Api {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories/{id}")
    suspend fun getStoryDetail(@Path("id") storyId: String): StoryResponse

    @GET("stories")
    suspend fun getStoriesWithLoc(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 50,
        @Query("location") location: Int = 0
    ): StoryResponse

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): retrofit2.Call<FileUploadResponse>
}