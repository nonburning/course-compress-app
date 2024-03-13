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

/*             if(isHexString(dataToEncrypt)) {
                val input = "263e5df7a93ec5f5ea6ac215ed957c30"
                val bytes = input.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
                bytes.toString(Charsets.UTF_8)
            } else {
                 dataToEncrypt.toByteArray().toBase64()
            }*/

            val b64 = if (isHexadecimal(dataToEncrypt)) {
                val byteArray = hexStringToByteArray(dataToEncrypt)
                Base64.getEncoder().encodeToString(byteArray)
            } else {
                dataToEncrypt.toByteArray().toBase64()
            }

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





fun isHexadecimal(s: String): Boolean {
    for (char in s) {
        if (!char.isDigit() && char !in 'a'..'f' && char !in 'A'..'F') {
            return false
        }
    }
    return true
}

fun hexStringToByteArray(hexString: String): ByteArray {
    val len = hexString.length
    val data = ByteArray(len / 2)
    var i = 0
    while (i < len) {
        data[i / 2] = ((Character.digit(hexString[i], 16) shl 4)
                + Character.digit(hexString[i + 1], 16)).toByte()
        i += 2
    }
    return data
}

    fun isHexString(s: String): Boolean {
        // Regular expression for matching hexadecimal strings
        val hexPattern = Regex("^[0-9a-fA-F]+$")

        // Check if the string matches the hexadecimal pattern
        return hexPattern.matches(s)
    }
    override  fun getCurrentUrl(): String {
        return sharedPrefs.getString("api_url", "https://7c6a-77-121-152-86.ngrok-free.app/")
            ?: "https://7c6a-77-121-152-86.ngrok-free.app/"
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