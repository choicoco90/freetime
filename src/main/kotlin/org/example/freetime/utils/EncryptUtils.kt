package org.example.freetime.utils

import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object EncryptUtils {

    // Encrypts the provided data using AES
    fun encrypt(value: String): String {
        return try {
            val aesKey: Key = SecretKeySpec(KEY.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.ENCRYPT_MODE, aesKey)
            val encrypted = cipher.doFinal(value.toByteArray(StandardCharsets.UTF_8))
            Base64.getUrlEncoder().withoutPadding().encodeToString(encrypted)
        } catch (e: Exception) {
            throw RuntimeException("Unable to encrypt")
        }
    }
    fun decrypt(encryptedValue: String): String {
        return try {
            val aesKey: Key = SecretKeySpec(KEY.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, aesKey)
            val decodedValue = Base64.getUrlDecoder().decode(encryptedValue)
            val decrypted = cipher.doFinal(decodedValue)
            String(decrypted, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            throw RuntimeException("Unable to decrypt")
        }
    }
    private const val KEY = "FREE_TIME"
}
