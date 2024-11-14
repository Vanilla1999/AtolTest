package com.example.atoltest

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import wt.softdecoding.aidl.ScanerListener
import wt.softdecoding.aidl.SetScanerService

class Wintec(private val mContext: Context, private val callBack: (String) -> Unit) {


    private var wintec: SetScanerService? = null
    private val listener: ScanerListener = object : ScanerListener.Stub() {
        @Throws(RemoteException::class)
        override fun onScanerResult(barcode: String) {
            CoroutineScope(Dispatchers.Main).launch {
                callBack(barcode)
            }
        }
    }
    private val conn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            // 获取远程Service的onBind方法返回的对象的代理
            wintec = SetScanerService.Stub.asInterface(service)
            try {
                wintec?.SetScanrListener(listener)
                wintec?.SetSupplementalLightBrightness(false)
                wintec?.SetSelectSymbologies(20, false)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            wintec = null
        }
    }

    fun init() {
        val intent = Intent()
        intent.setAction("WT.Scaner.Set")
        intent.setPackage("wt.softdecoding.service")
        mContext?.bindService(intent, conn, Service.BIND_AUTO_CREATE)
    }

    fun prepare() {
    }

    fun pause() {
    }

    fun release() {
    }
}