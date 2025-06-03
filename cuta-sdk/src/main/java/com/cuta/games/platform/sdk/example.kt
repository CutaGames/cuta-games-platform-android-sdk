package com.cuta.games.platform.sdk

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.crypto.tink.subtle.Base64
import kotlinx.serialization.json.Json

class Test(context: Context) {

    // 初始化
    val keyStorage = DIDKeyStorage(context)
    val didService = DIDService(storage = keyStorage)

    // 生成新DID
  //  val (did, pubKey) = didService.generateNewDID("yourgame.com")

    // 创建登录请求
    @RequiresApi(Build.VERSION_CODES.O)
    val authRequest = didService.createAuthRequest("yourgame.com")

    // 生成签名证明 (客户端)
    private fun createProof(did: String, request: DIDAuthRequest): DIDAuthProof {
        val keys = keyStorage.getKeys(did) ?: error("DID not found")
        val message = Json.encodeToString(request).toByteArray()
        val signature = DIDCrypto.sign(keys.second, message)

        return DIDAuthProof(
            did = did,
            signature = Base64.encodeToString(signature, Base64.NO_WRAP),
            publicKey = Base64.encodeToString(keys.first, Base64.NO_WRAP),
            signedMessage = message.decodeToString()
        )
    }

    // 验证证明 (服务端)
/*    val proof = createProof(did, authRequest)
    when (val result = didService.verifyProof(proof)) {
        is DIDAuthResult.Success -> {
            println("Login success! DID: ${result.did}")
            // 保存authToken
        }
        is DIDAuthResult.Failure -> {
            println("Login failed: ${result.error}")
        }
    }*/

    // DIDNetworkClient.kt
 /*   suspend fun verifyOnChain(did: String, localPublicKey: ByteArray): Boolean {
        val chainPublicKey = queryBlockchain(did) // RPC调用
        return chainPublicKey.contentEquals(localPublicKey)
    }

    private suspend fun queryBlockchain(did: String): ByteArray {
        val response = okHttpClient.get("https://chain-api/resolve?did=$did")
        return response.body?.bytes() ?: throw IOException()
    }*/

    fun testDIDCrypto() {
        // 1. 生成密钥对
        val keyPair = DIDCrypto.generateKeyPair()
        println("Generated KeyPair: ${keyPair.publicKey.size + keyPair.privateKey.size} bytes total")

        // 2. 签名验证
        val message = "Hello, DID World!".toByteArray()
        val signature = DIDCrypto.sign(keyPair.privateKey, message)
        println("Signature length: ${signature.size} bytes")

        // 3. 验证签名
        val isValid = DIDCrypto.verify(keyPair.publicKey, signature, message)
        println("Signature valid: $isValid") // 应输出 true

        // 4. 验证篡改检测
        val tampered = "Hacked message".toByteArray()
        val isTamperedValid = DIDCrypto.verify(keyPair.publicKey, signature, tampered)
        println("Tampered check passed: ${!isTamperedValid}") // 应输出 true
    }
}
