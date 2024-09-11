package com.example.submissionintermediate.database.api

import com.example.submissionintermediate.database.response.AddStoryResponse
import com.example.submissionintermediate.database.response.DetailStoryResponse
import com.example.submissionintermediate.database.response.ListStoryResponse
import com.example.submissionintermediate.database.response.LoginResponse
import com.example.submissionintermediate.database.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File

interface ApiService {
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

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): AddStoryResponse

    @GET("stories/{id}")
    suspend fun getDetail(
        @Path("id") id: String
    ):DetailStoryResponse

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): ListStoryResponse

    @GET("stories")
    suspend fun getLocationStories(
        @Query("location") location: Int = 1
    ): ListStoryResponse
}