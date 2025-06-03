package com.cuta.games.platform.sdk

// storage/DIDKeyStorage.kt
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.crypto.tink.subtle.Base64

class DIDKeyStorage(context: Context) {
    private val prefs: EncryptedSharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "did_secure_store",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }

    fun saveKeys(did: String, publicKey: ByteArray, privateKey: ByteArray) {
        prefs.edit()
            .putString("${did}_pub", Base64.encodeToString(publicKey, Base64.NO_WRAP))
            .putString("${did}_priv", Base64.encodeToString(privateKey, Base64.NO_WRAP))
            .apply()
    }

    fun getKeys(did: String): Pair<ByteArray, ByteArray>? {
        val pub = prefs.getString("${did}_pub", null) ?: return null
        val priv = prefs.getString("${did}_priv", null) ?: return null
        return Base64.decode(pub, Base64.NO_WRAP) to Base64.decode(priv, Base64.NO_WRAP)
    }
}