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

/**
 *  Base class that creates ciphers, keys, and IVs, and also handles encryption, decryption, storage, and retrieval of the data. This class
 *  also has basic logging and error handling as well as some helper functions to help keep the UI logic clean and readable. If you have a need to
 *  encrypt/decrypt a data model other than a token or username and password using biometrics, you can extend this class and overwrite [serializeData] and
 *  [deserializeData]. This class will treat your data as a single String value and will encrypt/store and decrypt/retrieve the data for you.
 */
abstract class BaseBiometricsHelper<T>(private val app: Application) {

    /**
     *  Instance of BiometricManager. This instance is encapsulated within this helper to keep the UI code clean and more readable.
     */
    private val biometricManager: BiometricManager = BiometricManager.from(app)

    /**
     * Returns True if biometrics is available and the user has at least one biometric interface set up on their device and
     * False otherwise.
     *
     * @return True if biometrics is available and the user has at least one biometric interface set up on their device and
     * False otherwise.
     */
    fun canAuthenticate(): Boolean =
        biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS

    /**
     * Returns True if there are no biometrics enrolled within the settings of the device and False otherwise.
     *
     * @return True if there are no biometrics enrolled within the settings of the device and False otherwise.
     */
    fun isNotEnrolledOnDevice(): Boolean =
        biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED

    /**
     * Returns True if biometrics has already been set up and False otherwise.
     *
     * @return True if biometrics has already been set up and False otherwise.
     */
    fun hasSetUpBiometrics(): Boolean = getPreferences().getString(BIOMETRIC_DATA_KEY, null) != null

    /**
     * Returns a Cipher that can be used to encrypt the data. If an exception is encountered, null is returned.
     *
     * @return The Cipher used for encryption or null if an exception was encountered.
     */
    fun getEncryptionCipher(): Cipher? {
        return try {
            Cipher.getInstance(TRANSFORMATION).apply { init(Cipher.ENCRYPT_MODE, getSecretKey()) }
        } catch (e: Exception) {
            Log.e(TAG,"getEncryptionCipher error", e)
            null
        }
    }

    /**
     * Returns a Cipher that can be used to decrypt the data. If an exception is encountered, the stored data is cleared
     * and null is returned.
     *
     * @return The Cipher used for decryption or null if an exception was encountered.
     */
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

    /**
     * This function takes in a Cipher and an object defined within the implementing class and encrypts and stores the data
     * for later retrieval. If the operation succeeds without any exceptions being thrown, True is returned. If an exception
     * is encountered then False is returned.
     *
     * @param data Data of a type specified within the implementing class that will get encrypted.
     * @param cipher Cipher used to encrypt the data.
     * @return True if no exception was encountered and False otherwise.
     */
    fun encryptAndStoreData(data: T, cipher: Cipher): Boolean {
        return try {
            serializeData(data)?.takeIf { it.isNotEmpty() }?.let { serializedData ->
                val encryptedData = cipher.doFinal(serializedData.toByteArray()).toBase64String()
                val biometricData: String = BIOMETRIC_DATA_FORMAT.format(encryptedData, cipher.iv.toBase64String())

                getPreferences().edit().putString(BIOMETRIC_DATA_KEY, biometricData).apply()
                true
            } ?: throw RuntimeException("Error encrypting data!")
        } catch (e: Exception) {
            Log.e(TAG, "encryptAndStoreData error", e)
            false
        }
    }

    /**
     * Takes in an object depending on the implementation used and returns that object serialized to a single String.
     * This function needs to be overridden inorder to use this class or you can use an instance of [CredentialBiometricsHelper]
     * or [TokenBiometricsHelper]. This function should take in the specified object and serialize it to a single String. If delimiters
     * are used, it is recommended to Base64 encode each part before combining them with delimiters unless you are certain the data will never contain
     * the delimiter used.
     *
     * NOTE: This function is only to be called within this class.
     *
     * @param data An object of specified type that will get serialized to a String.
     * @return The String representation of that object.
     */
    protected abstract fun serializeData(data: T): String?

    /**
     * This function takes in a Cipher and decrypts and returns the data type specified within the implementing class.
     * If an exception occurs, then the stored data is cleared and null is returned.
     *
     * @param cipher Cipher used to decrypt the data.
     * @return The object specified within the implementing class or null if an issue occurs.
     */
    fun decryptAndRetrieveData(cipher: Cipher): T? {
        return try {
            deserializeData(String(cipher.doFinal(getEncryptedData()))) ?: throw RuntimeException("Error decrypting data!")
        } catch (e: Exception) {
            Log.e(TAG, "decryptAndRetrieveData error", e)
            clearData()
            null
        }
    }

    /**
     * Takes in a String of serialized data and returns the specified object depending on the implementation used.
     * This function needs to be overridden inorder to use this class or you can use an instance of [CredentialBiometricsHelper]
     * or [TokenBiometricsHelper]. This function should reverse the data transformations within [serializedData].
     *
     * NOTE: This function is only to be called within this class.
     *
     * @param serializedData A string of serialized data.
     * @return An object specified in the implementation of this class.
     */
    protected abstract fun deserializeData(serializedData: String): T?

    /**
     * Clears the stored encrypted data and the IV used.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun clearData() {
        getPreferences().edit().clear().apply()
    }

    /**
     * Retrieves the SharedPreferences instance that is used by this class.
     *
     * @return The SharedPreferences instance.
     */
    private fun getPreferences(): SharedPreferences =
        app.getSharedPreferences(BIO_SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * Retrieves the data that was encrypted.
     *
     * @return The data that was encrypted as a ByteArray.
     */
    private fun getEncryptedData(): ByteArray? = getData(BiometricsDataType.DATA)

    /**
     * Retrieves the IV that was used during the encryption process from SharedPreferences.
     *
     * @return The IV used during encryption as a ByteArray.
     */
    private fun getStoredIV(): ByteArray? = getData(BiometricsDataType.IV)

    /**
     * Retrieves the encrypted data by [BiometricsDataType] as a ByteArray. This can either be the user encrypted data or
     * the IV that was used to encrypt it.
     *
     * @param type Type of data to retrieve from SharedPreferences. See [BiometricsDataType]
     * @return The data requested as a ByteArray.
     */
    private fun getData(type: BiometricsDataType): ByteArray? = getPreferences()
        .getString(BIOMETRIC_DATA_KEY, null)
        ?.split(DELIMITER)
        ?.takeIf { it.size == BiometricsDataType.values().size }
        ?.get(type.ordinal)
        .defaultIfNull()
        .decodeBase64ToByteArray()

    /**
     * Returns the secret key from the keystore if it exists. If the keystore does not contain the specified
     * key, then a new secret key is generated and then returned.
     *
     * @return The secret key used for encryption/decryption.
     */
    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(KEY_STORE).apply { load(null) }

        if (!keyStore.containsAlias(KEY_NAME)) generateSecretKey()

        return keyStore.getKey(KEY_NAME, null) as SecretKey
    }

    /**
     * Generates the secret key within the Android keystore that will be used to encrypt/decrypt the data.
     * This key requires user authentication and requires a secure lock screen to be set up.
     */
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

/**
 * DATA - The serialized token, username and password, or other custom data model.
 * IV - The initialization vector used when encrypting. This needs to be stored so that we can use it again
 * when we decrypt the data.
 */
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