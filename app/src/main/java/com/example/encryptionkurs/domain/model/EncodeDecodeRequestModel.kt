package com.example.encryptionkurs.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EncodeDecodeRequestModel(
    val bytes : String
)
