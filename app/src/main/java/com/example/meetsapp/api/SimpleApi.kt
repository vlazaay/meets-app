package com.example.meetsapp.api

import com.example.meetsapp.model.User
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface SimpleApi {

    @POST("api/meets-user-add")
    suspend fun pushUser(
        @Body user: User
    ): Response<String>

//    @PUT("api/wheel-amount-update/{playerID}/{amount}")
//    suspend fun putAmount(
//        @Path("playerID") playerID: String,
//        @Path("amount") amount: Int
//    ): Response<String>
//
    @GET("api/meetsgetuser/{deviceID}")
    suspend fun getUser(
        @Path("deviceID") deviceID: String?
    ): Response<JsonObject>
}