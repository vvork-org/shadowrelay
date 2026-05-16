package com.shadowrelay.core

interface ShadowCipher {
    fun encrypt(plainText: String, password: String): String
    fun decrypt(cipherText: String, password: String): String
}