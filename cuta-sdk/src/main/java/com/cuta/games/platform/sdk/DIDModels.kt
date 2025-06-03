package com.cuta.games.platform.sdk

import android.util.Base64
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.security.SecureRandom

// models/DIDModels.kt
@Serializable
data class DIDAuthRequest(
    @SerialName("domain") val domain: String,
    @SerialName("nonce") val nonce: String = generateNonce(),
    @SerialName("statement") val statement: String? = null,
    @SerialName("expires_at") val expiresAt: String? = null
) {
    companion object {
        fun generateNonce(): String {
            return SecureRandom().let { random ->
                ByteArray(32).apply { random.nextBytes(this) }
                    .let { bytes -> Base64.encodeToString(bytes, Base64.URL_SAFE) }
            }
        }
    }
}

@Serializable
data class DIDAuthProof(
    @SerialName("did") val did: String,
    @SerialName("signature") val signature: String, // Base64
    @SerialName("public_key") val publicKey: String, // Base64
    @SerialName("signed_message") val signedMessage: String
)

@Serializable
sealed class DIDAuthResult {
    @Serializable data class Success(
        val did: String,
        val publicKey: ByteArray,
        val authToken: String
    ) : DIDAuthResult()

    @Serializable data class Failure(
        val error: String,
        val code: Int
    ) : DIDAuthResult()
}