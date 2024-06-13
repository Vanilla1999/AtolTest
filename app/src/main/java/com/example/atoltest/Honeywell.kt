package com.example.atoltest

import android.content.Context
import android.widget.Toast
import com.honeywell.aidc.AidcManager
import com.honeywell.aidc.BarcodeFailureEvent
import com.honeywell.aidc.BarcodeReadEvent
import com.honeywell.aidc.BarcodeReader
import com.honeywell.aidc.BarcodeReader.BarcodeListener
import com.honeywell.aidc.InvalidScannerNameException
import com.honeywell.aidc.ScannerUnavailableException
import com.honeywell.aidc.UnsupportedPropertyException

class Honeywell(private val mContext: Context, private val callBack: (String) -> Unit) :
    BarcodeListener {
    private var barcodeReader: BarcodeReader? = null
    private var manager: AidcManager? = null
    fun init() {
        AidcManager.create(mContext) { aidcManager ->
            manager = aidcManager
            try {
                barcodeReader = manager?.createBarcodeReader()
                barcodeReader?.addBarcodeListener(this)
            } catch (e: InvalidScannerNameException) {
                e.printStackTrace()
            }
        }
    }

    fun prepare() {
        initHoneywellScaner()
        setHoneyclaimScanner()
    }

    fun pause() {
        barcodeReader?.release()
    }

    fun release() {
        barcodeReader?.removeBarcodeListener(this)
        barcodeReader?.release()
    }

    private fun initHoneywellScaner() {

        if (barcodeReader != null) {

            val map: Map<String, Any> = barcodeReader?.allDefaultProperties!!

            val properties: MutableMap<String, Any?> = HashMap()
            for (key in map.keys) {
                properties[key] = map[key]
            }

            //Коды, которые используются согласно оф. информации от сопровождения
            properties[BarcodeReader.PROPERTY_DATA_PROCESSOR_LAUNCH_BROWSER] = false
            properties[BarcodeReader.PROPERTY_QR_CODE_ENABLED] = true
            properties[BarcodeReader.PROPERTY_CODE_128_ENABLED] = true
            properties[BarcodeReader.PROPERTY_PDF_417_ENABLED] = true
            properties[BarcodeReader.PROPERTY_EAN_13_ENABLED] = true
            properties[BarcodeReader.PROPERTY_EAN_13_CHECK_DIGIT_TRANSMIT_ENABLED] = true
            properties[BarcodeReader.PROPERTY_EAN_13_FIVE_CHAR_ADDENDA_ENABLED] = true
            properties[BarcodeReader.PROPERTY_EAN_13_TWO_CHAR_ADDENDA_ENABLED] = true
            properties[BarcodeReader.PROPERTY_EAN_8_ENABLED] = true
            properties[BarcodeReader.PROPERTY_EAN_8_CHECK_DIGIT_TRANSMIT_ENABLED] = true
            properties[BarcodeReader.PROPERTY_RSS_ENABLED] = true
            properties[BarcodeReader.PROPERTY_CODE_39_ENABLED] = true
            properties[BarcodeReader.PROPERTY_UPC_A_ENABLE] = true
            properties[BarcodeReader.PROPERTY_UPC_A_TWO_CHAR_ADDENDA_ENABLED] = true
            properties[BarcodeReader.PROPERTY_UPC_A_FIVE_CHAR_ADDENDA_ENABLED] = true
            properties[BarcodeReader.PROPERTY_UPC_A_CHECK_DIGIT_TRANSMIT_ENABLED] = true

            properties[BarcodeReader.PROPERTY_UPC_A_TRANSLATE_EAN13] = false
            properties[BarcodeReader.PROPERTY_RSS_EXPANDED_ENABLED] = true

            properties.put(BarcodeReader.PROPERTY_CODE_39_MAXIMUM_LENGTH, 48)
            properties.put(BarcodeReader.PROPERTY_CODE_39_MINIMUM_LENGTH, 0)
            properties.put(
                BarcodeReader.PROPERTY_CODE_39_CHECK_DIGIT_MODE,
                BarcodeReader.CODE_39_CHECK_DIGIT_MODE_NO_CHECK
            )
            properties.put(BarcodeReader.PROPERTY_CODE_39_START_STOP_TRANSMIT_ENABLED, false)
            properties.put(BarcodeReader.PROPERTY_CODE_39_FULL_ASCII_ENABLED, false)
            properties.put(BarcodeReader.PROPERTY_CODE_39_BASE_32_ENABLED, false)


            properties.put(BarcodeReader.PROPERTY_CODE_93_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_RSS_EXPANDED_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_GS1_128_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_UPC_A_ENABLE, true)
            properties.put(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_CODABAR_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_INTERLEAVED_25_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_ISBT_128_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_UPC_E_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_CODE_11_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_AZTEC_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_TLC_39_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_MATRIX_25_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_IATA_25_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_STANDARD_25_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_COMPOSITE_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_MAXICODE_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_KOREAN_POST_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_CODABLOCK_A_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_HAX_XIN_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_CODABLOCK_F_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_MSI_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_CHINA_POST_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_TELEPEN_ENABLED, true)
            properties.put(BarcodeReader.PROPERTY_MICRO_PDF_417_ENABLED, true)

            properties[BarcodeReader.PROPERTY_CENTER_DECODE] = true
            properties[BarcodeReader.PROPERTY_NOTIFICATION_BAD_READ_ENABLED] =
                true
            barcodeReader?.setProperties(properties)
            try {
                barcodeReader?.setProperty(
                    BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                    BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL
                )
            } catch (e: UnsupportedPropertyException) {
                Toast.makeText(mContext, "Failed to apply properties", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setHoneyclaimScanner() {
        if (barcodeReader != null) {
            try {
                barcodeReader!!.claim()
            } catch (e: ScannerUnavailableException) {
                e.printStackTrace()
                Toast.makeText(mContext, "Scanner unavailable", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBarcodeEvent(event: BarcodeReadEvent) {
        callBack("${event.barcodeData}  ${event.charset} ${event.codeId} ${event.aimId}")
    }

    override fun onFailureEvent(p0: BarcodeFailureEvent?) {

    }

}