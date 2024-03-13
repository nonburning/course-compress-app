package com.example.encryptionkurs.data

import android.content.SharedPreferences
import com.example.encryptionkurs.data.api.ApiService
import com.example.encryptionkurs.domain.EncryptionRepository
import com.example.encryptionkurs.domain.model.EncodeDecodeRequestModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.ByteString.Companion.decodeBase64
import java.util.Base64

class EncryptionRepositoryImpl(
    private val apiService: ApiService,
    private val sharedPrefs: SharedPreferences,
) : EncryptionRepository {

    override suspend fun proceedData(
        dataToEncrypt: String,
        algorithm: Int,
        operation: String,
    ): Result<EncodeDecodeRequestModel> = withContext(Dispatchers.IO) {
        try {


            val b64 = dataToEncrypt.toByteArray().toBase64()

            val algo = when (algorithm) {
                2 -> "bwt"
                3 -> "lz77"
                else -> "rle"
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

    override  fun getCurrentUrl(): String {
        return sharedPrefs.getString("api_url", "https://684a-77-121-152-86.ngrok-free.app/")
            ?: "https://684a-77-121-152-86.ngrok-free.app/"
    }

    override suspend fun changeCurrentUrl(url: String) {
        sharedPrefs.edit().putString(
            "api_url", url
        ).apply()
    }
}

fun ByteArray.toBase64(): String =
    String(Base64.getEncoder().encode(this))

fun String.fromBase64(): ByteArray? =
    Base64.getDecoder().decode(this)