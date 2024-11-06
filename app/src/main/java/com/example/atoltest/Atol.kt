package com.example.atoltest

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContentProviderCompat.requireContext
import ru.atol.barcodeservice.api.ScannerSettings
import ru.atol.os.settings.api.manager.SetupManager

class Atol(private val mContext: Context, private val callBack: (String) -> Unit) {
    val SCAN_DECODING_BROADCAST = "com.xcheng.scanner.action.BARCODE_DECODING_BROADCAST"
    val SCAN_DECODING_DATA = "EXTRA_BARCODE_DECODING_DATA"
    var mAtolScanReceiver: BroadcastReceiver? = null
    var mAtolIntentFilter = IntentFilter(SCAN_DECODING_BROADCAST)

    private val settings = object : ScannerSettings() {
        override fun onServiceConnected() {// Читать настройки
            readSettings()
        }

        override fun onServiceDisconnected() {
            TODO("Not yet implemented")
        }
    }


    private fun readSettings() {
        val setupManager = SetupManager(mContext)
        settings.codes.ean13.enable.value = true
        settings.codes.ean13.enable2charAddenda.value = true
        settings.codes.ean13.enable5charAddenda.value = true
        settings.codes.ean13.separatorAddenda.value = false
        settings.codes.code39.enable.value = true
        settings.codes.gs1databar.enable
        settings.codes.datamatrix.gs1datamatrixSendFncPrefix.value = false
        settings.codes.datamatrix.enable.value = true
//        setupManager.setupRaw("{\"ver\": 1, \"kv\": [{\"k\": \"bs\", \"v\": [{\"3107\": 1}]}]}", false) // UPCA to EAN13
//        setupManager.setupRaw("{\"ver\": 1, \"kv\": [{\"k\": \"bs\", \"v\": [{\"1801\": 1}]}]}", false) //datamatrix
        setParameterValueIntent(1801,"1")
        setupManager.setupRaw("{\"ver\":1,\"kv\":[{\"k\":\"board\",\"v\":[{\"k\":2,\"v\":1}]}]}", false)
    }
    private fun setParameterValueIntent(num: Int, value: String) {
        val intent = Intent("ru.atol.barcodeservice.action.SET_PROPERTY")
        intent.putExtra ("PROPERTY_NUMBER",num)
        intent.putExtra("PROPERTY_VALUE", value)
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
        intent.component = ComponentName("ru.atol.barcodeservice", "ru.atol.barcodeservice.app.service.ScanReceiver")
        mContext.sendBroadcast(intent)
    }


    fun init() {
        mAtolScanReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                if (action == SCAN_DECODING_BROADCAST) {
                    val barcode = intent.getStringExtra(SCAN_DECODING_DATA)
                    callBack(barcode!!)
                }
            }
        }
        mContext.registerReceiver(mAtolScanReceiver, mAtolIntentFilter)
    }

    fun prepare() {
        settings.bindService(mContext)
    }

    fun pause() {
        settings.unbindService(mContext)
    }

    fun release() {
        mContext.unregisterReceiver(mAtolScanReceiver)
    }
}