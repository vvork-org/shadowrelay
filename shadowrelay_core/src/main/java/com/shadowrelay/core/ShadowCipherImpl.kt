package com.shadowrelay.core

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec

class ShadowCipherImpl : ShadowCipher {
    private val random = SecureRandom()

    override fun encrypt(plainText: String, password: String): String {
        val salt = ByteArray(16).also { random.nextBytes(it) }
        val iv = ByteArray(12).also { random.nextBytes(it) }

        val key = KeyDerivation.deriveAesKey(password, salt)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(128, iv))

        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        return PayloadCodec.encode(
            EncryptedPayload(
                version = 1,
                saltBase64 = Base64.encodeToString(salt, Base64.NO_WRAP),
                ivBase64 = Base64.encodeToString(iv, Base64.NO_WRAP),
                cipherTextBase64 = Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
            )
        )
    }

    override fun decrypt(cipherText: String, password: String): String {
        val payload = PayloadCodec.decode(cipherText)

        val salt = Base64.decode(payload.saltBase64, Base64.NO_WRAP)
        val iv = Base64.decode(payload.ivBase64, Base64.NO_WRAP)
        val encryptedBytes = Base64.decode(payload.cipherTextBase64, Base64.NO_WRAP)

        val key = KeyDerivation.deriveAesKey(password, salt)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))

        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }
}