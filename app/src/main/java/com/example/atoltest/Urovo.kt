package com.example.atoltest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.device.ScanManager
import android.device.scanner.configuration.PropertyID
import android.device.scanner.configuration.Symbology


class Urovo(private val mContext: Context, private val callBack: (String) -> Unit) {
    val SCAN_DECODING_DATA = "EXTRA_BARCODE_DECODING_DATA"
    var mUrovoScanReceiver: BroadcastReceiver? = null
    var mAtolIntentFilter = IntentFilter(ScanManager.ACTION_DECODE)


    fun init() {
        val scan = ScanManager()
       // scan.resetScannerParameters()
        scan.switchOutputMode(0)
        scan.setParameterInts(intArrayOf(PropertyID.SEND_GOOD_READ_BEEP_ENABLE), intArrayOf(1))
        // scan.enableAllSymbologies(true);
        scan.setPropertyInts(intArrayOf(PropertyID.EAN_EXT_ENABLE_2_5_DIGIT), intArrayOf(1))
        scan.setParameterInts(intArrayOf(PropertyID.SEND_GOOD_READ_BEEP_ENABLE), intArrayOf(1))
        scan.setParameterInts(intArrayOf(PropertyID.UPCA_TO_EAN13), intArrayOf(0))
        scan.setParameterInts(intArrayOf(PropertyID.DATAMATRIX_INVERSE), intArrayOf(2))
       // scan.setParameterInts(intArrayOf(PropertyID.CODE39_LENGTH2), intArrayOf(40))
        scan.setParameterInts(intArrayOf(PropertyID.GS1_EXP_ENABLE), intArrayOf(1))

        scan.enableSymbology(Symbology.CODE93, true)
        scan.enableSymbology(Symbology.GS1_EXP, true)
        scan.enableSymbology(Symbology.GS1_LIMIT, true)
        scan.enableSymbology(Symbology.GS1_14, true)

        /// !!!
        scan.setPropertyInts(
            intArrayOf(PropertyID.CODE39_LENGTH1),
            intArrayOf(1)
        )
        scan.setPropertyInts(
            intArrayOf(PropertyID.CODE39_LENGTH2),
            intArrayOf(48)
        )
        mUrovoScanReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val barcode = intent.getStringExtra(ScanManager.BARCODE_STRING_TAG)
                callBack(barcode!!)
            }
        }
        mContext.registerReceiver(mUrovoScanReceiver, mAtolIntentFilter)
    }

    fun prepare() {
    }

    fun pause() {
    }

    fun release() {
        mContext.unregisterReceiver(mUrovoScanReceiver)
    }
}