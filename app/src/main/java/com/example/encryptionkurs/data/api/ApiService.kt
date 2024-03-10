package com.example.encryptionkurs.data.api

import com.example.encryptionkurs.domain.model.EncodeDecodeRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {

    @POST("rel/encode")
    suspend fun encode(@Body body: EncodeDecodeRequestModel): Response<EncodeDecodeRequestModel>

    @POST("rel/decode")
    suspend fun decode(@Body body: EncodeDecodeRequestModel): Response<EncodeDecodeRequestModel>
}