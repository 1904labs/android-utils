package com.labs1904.biometrics

import android.app.Application

/**
 *  An instance of [BaseBiometricsHelper] that works with a token.
 */
class TokenBiometricsHelper(app: Application) : BaseBiometricsHelper<String>(app) {

    /**
     *  Since the data is already a single String, we do not need to do anything special to it.
     *
     *  @param data The String that needs to be serialized.
     *  @return The serialized string.
     */
    override fun serializeData(data: String): String? = data

    /**
     *  Since the data was stored as a single String, no further actions need to be done to deserialize it.
     *
     *  @param serializedData The String that needs to be deserialized.
     *  @return The deserialized String.
     */
    override fun deserializeData(serializedData: String): String? = serializedData
}