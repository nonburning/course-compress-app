package com.example.encryptionkurs.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EncodeDecodeRequestModel(
    @SerialName("bytes") val bytes : String
)
