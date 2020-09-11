package com.labs1904.biometrics

import android.app.Application

class TokenBiometricsHelper(app: Application) : BaseBiometricsHelper<String>(app) {

    override fun serializeData(data: String): String? = data

    override fun deserializeData(serializedData: String): String? = serializedData
}