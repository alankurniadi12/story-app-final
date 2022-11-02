package com.alankurniadi.storyapp.network

import com.alankurniadi.storyapp.model.ResponseAddNewStory
import com.alankurniadi.storyapp.model.ResponseAllStories
import com.alankurniadi.storyapp.model.ResponseLogin
import com.alankurniadi.storyapp.model.ResponseRegister
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun userRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseRegister>

    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseLogin>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int? = null ?: 1,
    ): ResponseAllStories

    @Multipart
    @POST("stories")
    fun postNewStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<ResponseAddNewStory>

    @GET("stories")
    fun getStoriesLocation(
        @Header("Authorization") token: String,
        @Query("size") size: Int? = null ?: 20,
        @Query("location") location: Int? = null ?: 1,
    ): Call<ResponseAllStories>
}
