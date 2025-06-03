package com.cuta.games.platform.sdk

// service/DIDService.kt
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.signature.SignatureKeyTemplates
import com.google.crypto.tink.subtle.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

class DIDService(
    private val crypto: DIDCrypto = DIDCrypto,
    private val storage: DIDKeyStorage
) {
    suspend fun generateNewDID(domain: String): Pair<String, ByteArray> {
        return withContext(Dispatchers.IO) {
            val keyPair = crypto.generateKeyPair()
            val did = "did:mobile:${Base64.encodeToString(keyPair.publicKey, Base64.URL_SAFE)}"
            storage.saveKeys(did, keyPair.publicKey, keyPair.privateKey)
            did to keyPair.publicKey
        }
    }

    // DIDService.kt
    @RequiresApi(Build.VERSION_CODES.O)
    fun createAuthRequest(did: String): DIDAuthRequest {
        return DIDAuthRequest(
            domain = "yourgame.com",
            nonce = UUID.randomUUID().toString(), // 防重放
            statement = "Sign in to Epic Game",
            expiresAt = Instant.now().plus(5, ChronoUnit.MINUTES).toString()
        )
    }

    // DIDService.kt
    suspend fun generateProof(did: String, challenge: DIDAuthRequest): DIDAuthProof {
        // 从安全存储获取私钥
        val (_, privateKey) = storage.getKeys(did) ?: throw IllegalStateException()

        // 签名（内部调用Tink）
        val message = Json.encodeToString(challenge).toByteArray()
        val signature = DIDCrypto.sign(privateKey, message) // 使用PrivateKeySign

        return DIDAuthProof(
            did = did,
            signature = Base64.encodeToString(signature, Base64.NO_WRAP),
            publicKey = Base64.encodeToString(getPublicKey(did), Base64.NO_WRAP),
            signedMessage = message.decodeToString()
        )
    }

    private fun getPublicKey(did: String): ByteArray? {
        return storage.getKeys(did)?.first
    }

    suspend fun verifyProof(proof: DIDAuthProof): DIDAuthResult {
        return try {
            val valid = crypto.verify(
                publicKey = Base64.decode(proof.publicKey),
                signature = Base64.decode(proof.signature),
                message = proof.signedMessage.toByteArray()
            )

            if (!valid) return DIDAuthResult.Failure("Invalid signature", 401)

            DIDAuthResult.Success(
                did = proof.did,
                publicKey = Base64.decode(proof.publicKey),
                authToken = generateJWT(proof.did)
            )
        } catch (e: Exception) {
            DIDAuthResult.Failure("Verification failed: ${e.message}", 500)
        }
    }

    private fun generateJWT(did: String): String {
        // 简化的JWT生成，实际应使用安全库如java-jwt
        val header = Base64.encodeToString("""{"alg":"EdDSA","typ":"JWT"}""".toByteArray(), Base64.URL_SAFE)
        val payload = Base64.encodeToString("""{"sub":"$did","exp":${System.currentTimeMillis() + 3600000}}""".toByteArray(), Base64.URL_SAFE)
        return "$header.$payload."
    }
}