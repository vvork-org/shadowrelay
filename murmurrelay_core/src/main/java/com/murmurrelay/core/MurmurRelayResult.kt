package com.murmurrelay.core

sealed class MurmurRelayResult {
    data object Success : MurmurRelayResult()
    data class Error(val message: String) : MurmurRelayResult()
}