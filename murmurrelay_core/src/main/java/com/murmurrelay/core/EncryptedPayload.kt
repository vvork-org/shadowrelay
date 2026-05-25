package com.murmurrelay.core

data class EncryptedPayload(
    val version: Int,
    val saltBase64: String,
    val ivBase64: String,
    val cipherTextBase64: String
)