package com.murmurrelay.core

interface MurmurCipher {
    fun encrypt(plainText: String, password: String): String
    fun decrypt(cipherText: String, password: String): String
}