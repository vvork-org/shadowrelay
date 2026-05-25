package com.murmurrelay.core

import kotlinx.serialization.Serializable

@Serializable
data class MurmurMessage(
    val version: Int = 1,
    val id: String,
    val timestamp: Long,
    val type: String = "text",
    val payload: String
)