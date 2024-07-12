package com.example.atoltest

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.meferi.btscannersdk.BuildConfig
import com.meferi.btscannersdk.MeCommunicateListener
import com.meferi.btscannersdk.MeScannerSDK
import com.meferi.btscannersdk.MeScannerSDK.initialize
import com.meferi.btscannersdk.MeScannerSDK.startMeSppServer
import com.meferi.btscannersdk.communicate.MeTriggerMode
import com.meferi.btscannersdk.connect.ConnectMode
import com.meferi.btscannersdk.connect.MeConnectionListener
import com.meferi.btscannersdk.utils.ByteUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets
import java.util.Timer
import kotlin.concurrent.timer
import kotlin.coroutines.CoroutineContext


class Meferi(private val mContext: Context, private val callBack: (String) -> Unit) :
    CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    var timer: Timer? = null
    var scanReceiver: BroadcastReceiver? = null
    fun init() {
        MeScannerSDK.setConnectionListener(object : MeConnectionListener {

            override fun onConnected(bluetoothDevice: BluetoothDevice, connectMode: ConnectMode) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(mContext, "Вы успешно подключились", Toast.LENGTH_LONG).show()
                }
                timer?.cancel()
                timer = timer(period = 60000L) {
                    launch {
                        MeScannerSDK.getBatteryInfo()
                    }
                }
            }

            override fun onConnectFailure(msg: String) {
            }

            override fun onDisconnected() {

            }

            override fun onConnectionStateChanged(state: Int) {

            }
        })
        MeScannerSDK.setCommunicateListener(object : MeCommunicateListener {

            override fun onDecodingData(data: ByteArray) {
                Log.d("barcode", ByteUtils.bytesToHex(data))
                val barcode = String(data, StandardCharsets.UTF_8)
                barcode.replace("\n", "")
                callBack(barcode)
            }

            override fun onFirmwareVersion(version: String) {

            }

            override fun onBatteryInfo(status: Int, voltage: Int, batteryLevel: Int) {
                Log.d("barcode", "$batteryLevel")
                val intent = Intent("battary")
                intent.putExtra("battary", batteryLevel.toString())
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent)
            }

            override fun onBuzzerStatus(enable: Boolean) {

            }


            override fun onAutoShutdownTime(duration: Int) {

            }

            override fun onTriggerMode(mode: MeTriggerMode) {

            }

            override fun onReconnectEnable(enable: Boolean) {
            }

            override fun onContinuousScanInterval(interval: Float) {

            }

            override fun onScanning() {
            }
        })
        scanReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                var barcodeStr = intent.getStringExtra("barcode_string")
                if (barcodeStr!!.contains("\r")) {
                    barcodeStr = barcodeStr.replace(
                        "\r".toRegex(),
                        ""
                    ) // в конце санирования у сканер кольца. не знаю зачем он, но я его убрал
                }
                callBack(barcodeStr)
            }
        }
        mContext.registerReceiver(
            scanReceiver,
            IntentFilter("android.intent.ACTION_DECODE_DATA")
        )
    }

    fun prepare() {

    }

    fun pause() {

    }

    fun release() {
        timer?.cancel()
        mContext.unregisterReceiver(scanReceiver)
        MeScannerSDK.disconnect()
    }

    companion object {
        fun initMeferi(app: Application) {
            initialize(app, BuildConfig.DEBUG)
            startMeSppServer()
        }
    }
}