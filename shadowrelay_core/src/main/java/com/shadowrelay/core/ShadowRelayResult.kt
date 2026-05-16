package com.shadowrelay.core

sealed class ShadowRelayResult {
    data object Success : ShadowRelayResult()
    data class Error(val message: String) : ShadowRelayResult()
}