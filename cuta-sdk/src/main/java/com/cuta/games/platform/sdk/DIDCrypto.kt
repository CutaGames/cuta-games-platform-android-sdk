package com.cuta.games.platform.sdk

// crypto/DIDCrypto.kt
import com.google.crypto.tink.*
import com.google.crypto.tink.signature.SignatureConfig
import com.google.crypto.tink.signature.SignatureKeyTemplates
import com.google.crypto.tink.CleartextKeysetHandle
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.GeneralSecurityException



object DIDCrypto {
    init {
        SignatureConfig.register()
    }

    data class KeyPair(
        val publicKey: ByteArray,  // 公钥keyset二进制
        val privateKey: ByteArray  // 私钥keyset二进制
    )

    fun generateKeyPair(): KeyPair {
        val privateHandle = KeysetHandle.generateNew(SignatureKeyTemplates.ED25519)

        // 私钥序列化
        val privateKeyBytes = ByteArrayOutputStream().use { os ->
            CleartextKeysetHandle.write(privateHandle, BinaryKeysetWriter.withOutputStream(os))
            os.toByteArray()
        }

        // 公钥序列化
        val publicKeyBytes = ByteArrayOutputStream().use { os ->
            CleartextKeysetHandle.write(
                privateHandle.publicKeysetHandle,
                BinaryKeysetWriter.withOutputStream(os)
            )
            os.toByteArray()
        }

        return KeyPair(publicKeyBytes, privateKeyBytes)
    }

    fun sign(privateKey: ByteArray, message: ByteArray): ByteArray {
        val handle = CleartextKeysetHandle.read(
            BinaryKeysetReader.withBytes(privateKey)
        )
        return handle.getPrimitive(PublicKeySign::class.java).sign(message)
    }

    fun verify(publicKey: ByteArray, signature: ByteArray, message: ByteArray): Boolean {
        return try {
            val handle = CleartextKeysetHandle.read(
                BinaryKeysetReader.withBytes(publicKey)
            )
            handle.getPrimitive(PublicKeyVerify::class.java)
                .verify(signature, message)
            true
        } catch (e: GeneralSecurityException) {
            false // 捕获签名不匹配等安全异常
        } catch (e: IOException) {
            false // 捕获数据解析异常
        }
    }
}