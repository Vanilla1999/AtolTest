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

    fun init() {
        mNewLandReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                var barcodeStr = intent.getStringExtra("BARCODE1")
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

        val intent = Intent("ACTION_BARCODE_CFG")
        intent.putExtra("CODE_ID", "QR")
        intent.putExtra("PROPERTY", "Enable")
        intent.putExtra("VALUE", "1")
        mContext?.sendBroadcast(intent);
        val intent1 = Intent("ACTION_BARCODE_CFG")
        intent1.putExtra("CODE_ID", "CODE128")
        intent1.putExtra("PROPERTY", "Enable")
        intent1.putExtra("VALUE", "0")
        mContext?.sendBroadcast(intent);
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