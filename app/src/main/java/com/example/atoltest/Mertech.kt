package com.example.atoltest

import android.content.Context
import android.os.Handler
import android.util.Log
import com.superlead.sdk.HexUtils
import com.superlead.sdk.Scanner
import com.superlead.sdk.ScannerHelper
import com.superlead.sdk.listener.BarcodeReceiver
import com.superlead.sdk.usb.UsbConfig
import java.io.IOException
import java.util.Locale

class Mertech(private val mContext: Context, private val callBack: (String) -> Unit) {

    fun init() {

    }

    fun prepare() {
        BarcodeReceiver({ data ->
            Log.d("sdf", "sdf")
        }, mContext)
        UsbConfig.INSTANCE.setVendor(11734)
        val devices: List<Scanner> = ScannerHelper.INSTANCE.listAllDevice(mContext)
        val scanner: Scanner = devices[0]
        try {
            scanner.open()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        scanner.write(getSendBytes("02F003303233373031302E"))
    }

    private fun getSendBytes(message: String): ByteArray? {
        var data: ByteArray? = null
        //根据发送信息格式，把发送消息转换成byte[]
        data = HexUtils.hexStringToByte(message.uppercase(Locale.getDefault()))
        return data
    }

    fun pause() {

    }

    fun release() {

    }
}