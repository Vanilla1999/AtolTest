package com.example.atoltest

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.nlscan.nlsbackuprecovery.IBackupRecovery


class NewLand(private val mContext: Context, private val callBack: (String) -> Unit) {
    var mNewLandReceiver: BroadcastReceiver? = null
    private var mNewLandIsScaning = false
    var mNewLandScannderFilter = IntentFilter("nlscan.action.SCANNER_RESULT")
    var iBackupRecovery: IBackupRecovery? = null
    var connectionToBackupConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(arg0: ComponentName) {
            iBackupRecovery = null
        }

        override fun onServiceConnected(arg0: ComponentName, arg1: IBinder) {
            iBackupRecovery = IBackupRecovery.Stub.asInterface(arg1)
            val settings = """{
    "scan_setting": [
        {
            "common": [
                {
                    "BROADCAST_OUTPUT_SETTINGS.ACTION": "nlscan.action.SCANNER_RESULT",
                    "BROADCAST_OUTPUT_SETTINGS.RESULT1": "BARCODE1"
                }
            ],
            "softwareEngine": [
                {
                    "AZTEC.Enable": "1",
                    "AZTEC.Minlen": "1",
                    "AZTEC.Maxlen": "3832",
                    "AZTEC.CodeNum": "2",
                    "AZTEC.NumFixed": "0",
                    "AZTEC.VideoMode": "2"
                }
            ]
        }
    ]
}"""

            val result = iBackupRecovery!!.setNLSRecoverData(settings)
            print("")
        }
    }

    fun init() {
        mNewLandReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                var barcodeStr = intent.getStringExtra("SCAN_BARCODE1")
                if (barcodeStr!!.contains("\n")) {
                    barcodeStr = barcodeStr.replace(
                        "\n".toRegex(),
                        ""
                    )
                }
                val scanStatus = intent.getStringExtra("SCAN_STATE")
                Log.d("Newland", scanStatus.toString())
                Log.d("Newland", barcodeStr)
                if ("ok" == scanStatus)
                    if (mNewLandIsScaning)
                        callBack(barcodeStr)
            }
        }
        mContext?.registerReceiver(mNewLandReceiver, mNewLandScannderFilter)

        val intent1 = Intent("nlscan.action.SCANNER_TRIG")
        mContext.sendBroadcast(intent1)
        val intent = Intent()
        intent.setComponent(
            ComponentName(
                "com.nlscan.nlsbackuprecovery",
                "com.nlscan.nlsbackuprecovery.service.BackupRecoveryService"
            )
        )
        mContext.bindService(intent, connectionToBackupConnection, Context.BIND_AUTO_CREATE)
    }


    fun prepare() {
        mNewLandIsScaning = true
    }

    fun pause() {
        mNewLandIsScaning = false
    }

    fun release() {
        mContext.unregisterReceiver(mNewLandReceiver)
    }
}