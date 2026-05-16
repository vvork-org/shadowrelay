package com.shadowrelay.core

import kotlinx.serialization.Serializable

@Serializable
data class ShadowMessage(
    val version: Int = 1,
    val id: String,
    val timestamp: Long,
    val type: String = "text",
    val payload: String
)