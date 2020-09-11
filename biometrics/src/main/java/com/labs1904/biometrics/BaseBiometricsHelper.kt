package com.labs1904.biometrics

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import androidx.biometric.BiometricManager
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

abstract class BaseBiometricsHelper<T>(private val app: Application) {

    private val biometricManager: BiometricManager = BiometricManager.from(app)

    fun canAuthenticate(): Boolean =
        biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS

    fun isNotEnrolledOnDevice(): Boolean =
        biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED

    fun hasSetUpBiometrics(): Boolean = getPreferences().getString(BIOMETRIC_DATA_KEY, null) != null

    fun getEncryptionCipher(): Cipher? {
        return try {
            Cipher.getInstance(TRANSFORMATION).apply { init(Cipher.ENCRYPT_MODE, getSecretKey()) }
        } catch (e: Exception) {
            Log.e(TAG,"getEncryptionCipher error", e)
            null
        }
    }

    fun getDecryptionCipher(): Cipher? {
        return try {
            Cipher
                .getInstance(TRANSFORMATION)
                .apply { init(Cipher.DECRYPT_MODE, getSecretKey(), IvParameterSpec(getStoredIV())) }
        } catch (e: Exception) {
            Log.e(TAG, "getDecryptionCipher error", e)
            clearData()
            null
        }
    }

    fun encryptAndStoreData(data: T, cipher: Cipher): Boolean {
        return try {
            serializeData(data)?.takeIf { it.isNotEmpty() }?.let { encodedString ->
                val encryptedCredentials = cipher.doFinal(encodedString.toByteArray()).toBase64String()
                val biometricData: String = BIOMETRIC_DATA_FORMAT.format(encryptedCredentials, cipher.iv.toBase64String())

                getPreferences().edit().putString(BIOMETRIC_DATA_KEY, biometricData).apply()
                true
            } ?: throw RuntimeException("Error encrypting data!")
        } catch (e: Exception) {
            Log.e(TAG, "encryptAndStoreData error", e)
            false
        }
    }

    protected abstract fun serializeData(data: T): String?

    fun decryptAndRetrieveData(cipher: Cipher): T? {
        return try {
            deserializeData(String(cipher.doFinal(getEncryptedData()))) ?: throw RuntimeException("Error decrypting data!")
        } catch (e: Exception) {
            Log.e(TAG, "decryptAndRetrieveData error", e)
            clearData()
            null
        }
    }

    protected abstract fun deserializeData(serializedData: String): T?

    @Suppress("MemberVisibilityCanBePrivate")
    fun clearData() {
        getPreferences().edit().clear().apply()
    }

    private fun getPreferences(): SharedPreferences =
        app.getSharedPreferences(BIO_SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    private fun getEncryptedData(): ByteArray? = getData(BiometricsDataType.DATA)

    private fun getStoredIV(): ByteArray? = getData(BiometricsDataType.IV)

    private fun getData(type: BiometricsDataType): ByteArray? = getPreferences()
        .getString(BIOMETRIC_DATA_KEY, null)
        ?.split(DELIMITER)
        ?.takeIf { it.size == BiometricsDataType.values().size }
        ?.get(type.ordinal)
        .defaultIfNull()
        .decodeBase64ToByteArray()

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(KEY_STORE).apply { load(null) }

        if (!keyStore.containsAlias(KEY_NAME)) generateSecretKey()

        return keyStore.getKey(KEY_NAME, null) as SecretKey
    }

    private fun generateSecretKey() {
        KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEY_STORE).apply {
            init(
                KeyGenParameterSpec.Builder(
                    KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setUserAuthenticationRequired(true)
                    .build()
            )
            generateKey()
        }
    }

    companion object {
        private const val TAG: String = "BaseBiometricsHelper"
        private const val KEY_STORE: String = "AndroidKeyStore"
        private const val KEY_NAME: String = "BiometricsKey"
        private const val TRANSFORMATION: String =
            "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_CBC}/${KeyProperties.ENCRYPTION_PADDING_PKCS7}"
        private const val BIO_SHARED_PREFS_NAME = "biometric_shared_prefs"
        private const val BIOMETRIC_DATA_KEY: String = "BIOMETRIC_DATA_KEY"
        private const val DELIMITER: String = "|"
        private const val BIOMETRIC_DATA_FORMAT: String = "%s${DELIMITER}%s"
    }
}

private enum class BiometricsDataType {
    DATA, IV
}

/**
 * Safe and simple way to convert a ByteArray into a Base64 encoded string. This function
 * catches all exceptions and returns null if one is encountered.
 *
 * @param flags (defaults to Base64.DEFAULT) Controls certain features of the encoded output. Passing {@code DEFAULT} results in output that adheres to RFC 2045.
 * @return The base64 encoded string or null if an exception occurred.
 */
internal fun ByteArray?.toBase64String(flags: Int = Base64.DEFAULT): String? =
    try {
        Base64.encodeToString(this, flags)
    } catch (e: Exception) {
        null
    }

/**
 * Decodes this Base64 String into a ByteArray.
 *
 * @param flags Controls certain features of the decoded output. See {@link android.util.Base64} for more information
 * @return A ByteArray of the decoded data, or null if an exception occurred.
 */
internal fun String.decodeBase64ToByteArray(flags: Int = Base64.DEFAULT): ByteArray? =
    try {
        Base64.decode(this, flags)
    } catch (e: Exception) {
        null
    }

/**
 * Returns [defaultValue] if this String is null
 *
 * @param defaultValue The value to return if [this] is null
 * @return this String, or if it's null, [defaultValue]
 */
internal fun String?.defaultIfNull(defaultValue: String = ""): String = this ?: defaultValue

/**
 * If both [p1] and [p2] are not null, execute [block] and return its result. Otherwise return null.
 *
 * @param p1 The first value required to be non-null
 * @param p2 The second value required to be non-null
 * @return The result of [block] if both [p1] and [p2] are not null. If at least one parameter is null, this function returns null
 */
internal fun <T1 : Any, T2: Any, R: Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? =
    if (p1 != null && p2 != null) block(p1, p2) else null

/**
 * Safe and simple way to convert a string into a Base64 encoded string. This function
 * catches all exceptions and returns null if one is encountered.
 *
 * @param flags (defaults to Base64.DEFAULT) Controls certain features of the encoded output. Passing {@code DEFAULT} results in output that adheres to RFC 2045.
 * @return The base64 encoded string or null if an exception occurred.
 */
internal fun String?.toBase64String(flags: Int = Base64.DEFAULT): String? =
    try {
        Base64.encodeToString(this?.toByteArray(), flags)
    } catch (e: Exception) {
        null
    }

/**
 * Safe and simple way to decode a Base64 encoded string. This function catches all exceptions and
 * returns null if one is encountered.
 *
 * @param flags (defaults to Base64.DEFAULT) Controls certain features of the encoded output. Passing {@code DEFAULT} results in output that adheres to RFC 2045.
 * @return The decoded string or null if an exception occurred.
 */
internal fun String?.decodeBase64(flags: Int = Base64.DEFAULT): String? =
    try {
        String(Base64.decode(this, flags))
    } catch (e: Exception) {
        null
    }