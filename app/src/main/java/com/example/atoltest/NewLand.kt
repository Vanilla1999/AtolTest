package com.example.atoltest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log


class NewLand(private val mContext: Context, private val callBack: (String) -> Unit) {
    var mNewLandReceiver: BroadcastReceiver? = null
    private var mNewLandIsScaning = false
    var mNewLandScannderFilter = IntentFilter("nlscan.action.SCANNER_RESULT")

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