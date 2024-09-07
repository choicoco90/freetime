package org.example.freetime.utils

object FreeTimeUtils {
    private const val DELIMITER = ":"

    fun encryptResetPasswordString(userId: Long, newPassword: String): String {
        val data = listOf(userId, newPassword).joinToString(DELIMITER)
        return EncryptUtils.encrypt(data)
    }

    fun decryptResetPasswordString(resetPasswordString: String): Pair<Long, String> {
        val decrypted = EncryptUtils.decrypt(resetPasswordString)
        val split = decrypted.split(DELIMITER)
        return Pair(split[0].toLong(), split[1])
    }
}
