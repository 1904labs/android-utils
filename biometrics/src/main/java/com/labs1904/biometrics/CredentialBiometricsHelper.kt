package com.labs1904.biometrics

import android.app.Application

/**
 *  An instance of [BaseBiometricsHelper] that works with a username and password.
 */
class CredentialBiometricsHelper(app: Application) : BaseBiometricsHelper<Credentials>(app) {

    /**
     *  Serializes a [Credentials] object into a single String so that it can be encrypted and stored within [BaseBiometricsHelper].
     *  This function Base64 encodes each part before putting them together separated by a delimiter to ensure that the data
     *  does not contain any instances of the delimiter.
     *
     *  @param data The [Credentials] object that is getting serialized into a String.
     *  @return The String representation of the [Credentials] object.
     */
    override fun serializeData(data: Credentials): String? =
        safeLet(data.userName.toBase64String(), data.password.toBase64String()) { encodedUserName, encodedPassword ->
            DATA_FORMAT.format(encodedUserName, encodedPassword)
        }

    /**
     *  Deserializes the data into a [Credentials] object to be returned. This function splits the data at the delimiter and
     *  decodes each Base64 part.
     *
     *  @param serializedData The serialized String that needs to be encoded.
     *  @return The deserialized [Credentials] object.
     */
    override fun deserializeData(serializedData: String): Credentials? =
        serializedData.split(DELIMITER).takeIf { it.size == 2 }?.let {
            safeLet(it[0].decodeBase64(), it[1].decodeBase64()) { email, pass ->
                Credentials(email, pass)
            }
        }

    companion object {
        private const val DELIMITER: String = "|"
        private const val DATA_FORMAT: String = "%s${DELIMITER}%s"
    }
}

/**
 *  Simple data class so that this implementation can serialize and deserialize a username and a password.
 */
data class Credentials(val userName: String, val password: String)