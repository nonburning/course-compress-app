package com.example.encryptionkurs.data.api

import com.example.encryptionkurs.domain.model.EncodeDecodeRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {

    @POST("{algo}/{operation}")
    suspend fun encode(
        @Path("algo") algo : String,
        @Path("operation") operation : String,
        @Body body: EncodeDecodeRequestModel): Response<EncodeDecodeRequestModel>

}