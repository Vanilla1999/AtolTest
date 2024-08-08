package com.example.atoltest

import android.content.Context
import android.content.Intent
import android.util.Log
import com.superlead.sdk.HexUtils
import com.superlead.sdk.Scanner
import com.superlead.sdk.ScannerHelper
import com.superlead.sdk.listener.BarcodeReceiver
import com.superlead.sdk.service.BarcodeService
import com.superlead.sdk.usb.UsbConfig
import java.io.IOException
import java.nio.charset.Charset
import java.util.Locale

class Mertech(private val mContext: Context, private val callBack: (String) -> Unit) {
    private var barcodeReceiver: BarcodeReceiver? = null
    var scanner: Scanner? = null
    var isNeedScan = false
    fun init() {
        barcodeReceiver = BarcodeReceiver({ data ->
            if (isNeedScan) {
                var barcode = String(data, Charset.forName("ISO-8859-1"))
                barcode = barcode.replace("\r", "")
                callBack(barcode)
            }
        }, mContext)
        try {
            UsbConfig.INSTANCE.setVendor(11734)
            val devices: List<Scanner> = ScannerHelper.INSTANCE.listAllDevice(mContext)
            scanner = devices[0]
            scanner?.open()
            setScannerSettings()
        } catch (e: Exception) {
            Log.d("sdf", "sdf")
        }
    }

    fun prepare() {
        isNeedScan = true
    }

    private fun getSendBytes(message: String): ByteArray? {
        var data: ByteArray? = null
        //根据发送信息格式，把发送消息转换成byte[]
        data = HexUtils.hexStringToByte(message.uppercase(Locale.getDefault()))
        return data
    }

    fun pause() {
        isNeedScan = false
    }

    fun release() {
        try {
            scanner!!.close()
            mContext.unregisterReceiver(barcodeReceiver)
        } catch (e: java.lang.Exception) {
        }
    }

    private fun setScannerSettings() {
        try {
            scanner!!.write(getSendBytes("02F003303233373031312E")) // QR
            scanner!!.write(getSendBytes("02F003303231343031312E")) // EAN8
            scanner!!.write(getSendBytes("02F003303231333031312E")) // EAN13
            scanner!!.write(getSendBytes("02F003303231343032312E")) // EAN8 check digit
            scanner!!.write(getSendBytes("02F003303230413031312E")) // Code128
            scanner!!.write(getSendBytes("02F003303231343034312E")) // EAN8+5
            scanner!!.write(getSendBytes("02F003303231333034312E")) // EAN13+5
            scanner!!.write(getSendBytes("02F003303231333032312E")) // EAN13 check digit
            scanner!!.write(getSendBytes("02F003303231323031312E")) // UPC-e
            scanner!!.write(getSendBytes("02F003303231313031312E")) // UPC-a
            scanner!!.write(getSendBytes("02F003303230333031312E")) // CODE39
            //scanner.write(getSendBytes("")); // CODE39 min width // не может
            scanner!!.write(getSendBytes("02F003303230443031312E")) // CODE93
            scanner!!.write(getSendBytes("02F003303230423030312E")) // GS1 128
            scanner!!.write(getSendBytes("02F003303231393031312E")) // GS1 databar
            scanner!!.write(getSendBytes("02F003303231413031312E")) // GS1 limited
            scanner!!.write(getSendBytes("02F003303231423031312E")) // GS1 expended
            scanner!!.write(getSendBytes("02F003303230393031312E")) // CODE11
            scanner!!.write(getSendBytes("02F003303231463031312E")) // PDF 417
            scanner!!.write(getSendBytes("02F003303233333031312E")) // Aztec
            scanner!!.write(getSendBytes("02F003303233363031312E")) // DataMatrix
            scanner!!.write(getSendBytes("02F003303230343031312E")) // I25
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}