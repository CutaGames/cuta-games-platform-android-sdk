package com.cuta.games.platform.sdk

import android.content.Context
import android.os.RemoteException
import android.util.Log

object CutaPlatformSdk {
    private lateinit var appContext: Context
    private val didClient by lazy { DIDClient(appContext) }

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun getCurrentDID(): String? {
        return "did:cuta:6yLcQFEqbC7jAHkFxgiz8Ntty4QQxv7V34JwfLZcxNkW"
       /* return try {
            didClient.getDID().also { did ->
                if (did.isNullOrEmpty()) {
                    Log.w("CutaDID", "Got empty DID from launcher")
                }
            }
        } catch (e: RemoteException) {
            Log.e("CutaDID", "AIDL communication failed", e)
            null
        }*/
    }

}