package com.example.encryptionkurs.data

import com.example.encryptionkurs.data.api.ApiService
import com.example.encryptionkurs.domain.EncryptionRepository
import com.example.encryptionkurs.domain.model.EncodeDecodeRequestModel
import java.util.Base64

class EncryptionRepositoryImpl(
    private val apiService: ApiService,
) : EncryptionRepository {

    override suspend fun proceedData(
        dataToEncrypt: String,
        algorithm: Int,
        operation: String,
    ): Result<EncodeDecodeRequestModel> {
        return try {
            fun ByteArray.toBase64(): String =
                String(Base64.getEncoder().encode(this))

            val b64 = dataToEncrypt.toByteArray().toBase64()
            val algo = when(algorithm) {
                2 -> "bwt"
                3 -> "lz77"
                else -> "rel"
            }

            val res = apiService.encode(
                algo, operation, EncodeDecodeRequestModel(
                    b64
                )
            )

            val body = res.body()

            if (res.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(NullPointerException())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}