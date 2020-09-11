package com.labs1904.biometrics

import android.app.Application

class CredentialBiometricsHelper(app: Application) : BaseBiometricsHelper<Credentials>(app) {

    override fun serializeData(data: Credentials): String? =
        safeLet(data.userName.toBase64String(), data.password.toBase64String()) { encodedUserName, encodedPassword ->
            DATA_FORMAT.format(encodedUserName, encodedPassword)
        }

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

data class Credentials(val userName: String, val password: String)