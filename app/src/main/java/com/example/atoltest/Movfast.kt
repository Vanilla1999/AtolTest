package com.example.atoltest

import android.content.Context
import com.xcheng.scanner.BarcodeType
import com.xcheng.scanner.ScannerResult
import com.xcheng.scanner.XCBarcodeTag
import com.xcheng.scanner.XcBarcodeScanner


class Movfast(private val mContext: Context, private val callBack: (String) -> Unit) :
    ScannerResult {


    fun init() {
        XcBarcodeScanner.init(mContext, this as ScannerResult)
        enableBarcodes()
    }

    fun prepare() {
    }

    fun pause() {
    }

    fun release() {
    }

    private fun enableBarcodes() {
        XcBarcodeScanner.enableBarcodeType(BarcodeType.AZTEC, true)
        XcBarcodeScanner.enableBarcodeType(BarcodeType.CODE11, true)
        XcBarcodeScanner.enableBarcodeType(BarcodeType.CODE39, true)
        XcBarcodeScanner.enableBarcodeType(BarcodeType.CODE93, true)
        XcBarcodeScanner.enableBarcodeType(BarcodeType.CODE128, true)
        XcBarcodeScanner.enableBarcodeType(BarcodeType.CODABAR, true)
        XcBarcodeScanner.enableBarcodeType(BarcodeType.DATAMATRIX, true)
        XcBarcodeScanner.enableBarcodeType(BarcodeType.EAN8, true)
        XcBarcodeScanner.enableBarcodeType(BarcodeType.EAN13, true)
        XcBarcodeScanner.enableBarcodeType(BarcodeType.GS1_DATABAR, true)
        XcBarcodeScanner.enableBarcodeType(BarcodeType.INDUSTRIAL25, true)
        XcBarcodeScanner.enableBarcodeType(BarcodeType.PDF417, true)
        XcBarcodeScanner.enableBarcodeType(BarcodeType.QRCODE, false)
        XcBarcodeScanner.enableBarcodeType(BarcodeType.UPCA, true)
        XcBarcodeScanner.enableBarcodeType(BarcodeType.UPCE, true)
        XcBarcodeScanner.enableBarcodeType(BarcodeType.GS1_128, true)
        XcBarcodeScanner.enableBarcodeType(BarcodeType.GS1_DATABAR_LIMITED, true)
        XcBarcodeScanner.enableBarcodeType(BarcodeType.GS1_DATABAR_EXPANDED, true)
        XcBarcodeScanner.enableBarcodeType(BarcodeType.GS1_128, true)
        XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_EAN13_CHECK_DIGIT_TRANSMIT, 1)
        XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_UPCA_2CHAR_ADDENDA_ENABLED, 1)
        XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_EAN13_2CHAR_ADDENDA_ENABLED, 0)
        XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_EAN13_5CHAR_ADDENDA_ENABLED, 0)
        XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_UPCA_CHECK_DIGIT_TRANSMIT, 1)
        XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_UPCA_5CHAR_ADDENDA_ENABLED, 1)
    }

    override fun onResult(p0: String) {
        callBack(p0)
    }
}