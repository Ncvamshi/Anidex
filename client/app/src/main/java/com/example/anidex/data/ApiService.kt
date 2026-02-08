package com.example.anidex.data

import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body request: AuthRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    @GET("animals/search")
    suspend fun searchAnimals(@Query("q") query: String): List<Animal>

    @GET("animals")
    suspend fun getAllAnimals(): List<Animal>

    @Multipart
    @POST("predict/")
    suspend fun predict(
        @Query("user_id") userId: String,
        @Part file: MultipartBody.Part
    ): PredictResponse

    @GET("collection/{user_id}")
    suspend fun getCollection(@Path("user_id") userId: String): List<CollectionResponse>
}