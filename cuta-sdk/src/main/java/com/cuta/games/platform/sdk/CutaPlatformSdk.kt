package com.cuta.games.platform.sdk

import android.content.Context

class CutaPlatformSdk private constructor(private val context: Context) {

    companion object {
        @Volatile private var instance: CutaPlatformSdk? = null

        fun init(context: Context): CutaPlatformSdk {
            return instance ?: synchronized(this) {
                instance ?: CutaPlatformSdk(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    private val didClient = DIDClient(context)

    fun getCurrentDID(): String? {
        return didClient.getDID()
    }

}