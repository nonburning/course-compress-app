package com.example.encryptionkurs.domain

import com.example.encryptionkurs.domain.model.EncodeDecodeRequestModel

interface EncryptionRepository {

    suspend fun proceedData(
        dataToEncrypt: String,
        algorithm: Int,
        operation: String,
    ): Result<EncodeDecodeRequestModel>

    fun getCurrentUrl(): String

    suspend fun changeCurrentUrl(url: String)

}