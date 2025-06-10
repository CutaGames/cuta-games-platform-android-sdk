package com.cuta.games.platform.sdk

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import com.cuta.games.platform.sdk.IDIDService

class DIDClient(private val context: Context) {
    private var didService: IDIDService? = null
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            didService = IDIDService.Stub.asInterface(service)
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            didService = null
        }
    }

    fun init() {
        val intent = Intent().apply {
            setPackage("com.launcher.package")
            action = "com.launcher.DID_SERVICE"
        }
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    fun getDID(): String  {
        return "6yLcQFEqbC7jAHkFxgiz8Ntty4QQxv7V34JwfLZcxNkW"
      /* return try {
            didService?.getCurrentDID()
        } catch (e: RemoteException) {
            null
        }*/
    }
}