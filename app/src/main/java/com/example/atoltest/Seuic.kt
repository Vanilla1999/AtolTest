package com.example.atoltest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.SoundPool
import android.util.Log
import com.seuic.scankey.IKeyEventCallback
import com.seuic.scankey.ScanKeyService
import com.seuic.scanner.*
import kotlin.properties.Delegates


class Seuic(private val mContext: Context, private val callBack: (String) -> Unit) :
    DecodeInfoCallBack {
    var scanner: Scanner? = null
    private val mScanKeyService = ScanKeyService.getInstance()

    lateinit var sp: SoundPool
    private val MAX_STREAMS = 5
    private var soundId by Delegates.notNull<Int>()
    private val mCallback: IKeyEventCallback = object : IKeyEventCallback.Stub() {
        override fun onKeyDown(keyCode: Int) {
            /* if(keyCode==249){
                 scanner?.startScan()
             }else if(keyCode==248||keyCode==250){
                 scanner!!.startVideo(3000)
             }*/
            scanner?.startScan()
        }

        override fun onKeyUp(keyCode: Int) {
            scanner?.stopScan()
            /*if(keyCode==249){
                scanner?.stopScan()
            }else if(keyCode==248||keyCode==250){
                scanner?.stopVideo()
            }*/
        }
    }

    fun init() {
        mScanKeyService.registerCallback(mCallback, "248,249,250")
        scanner = ScannerFactory.getScanner(mContext)
        scanner?.open()
        setParam()
        scanner?.setDecodeInfoCallBack(this)

        sp = SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0)
        soundId = sp.load(mContext, R.raw.scan, 1)
    }

    private fun setParam() {
        scanner?.setParams(0x105, 1)//enableEAN13
        scanner?.setParams(0x119, 1) // ean13=5
        scanner?.setParams(0x101, 1) // enableUPCA
        scanner?.setParams(0x102, 1)//enableUPCE
        scanner?.setParams(0x103, 1)//enableUPCE1
        scanner?.setParams(0x104, 1)//enableEAN8
        scanner?.setParams(0x106, 1)//enableBookLandEAN
        scanner?.setParams(0x121, 1)//enableCode128
        scanner?.setParams(0x131, 1)//enableCode39
        scanner?.setParams(0x141, 1)//enableCode93
        scanner?.setParams(0x125, 1)//enableGS1128
        scanner?.setParams(0x151, 1)//enableCode11
        scanner?.setParams(0x161, 1)//enableUPCE
        scanner?.setParams(0x191, 1)//enableUPCE
        scanner?.setParams(0x201, 1)//enableUPCE
        scanner?.setParams(0x221, 1)//enableUPCE
        scanner?.setParams(0x231, 3)//enableUPCE
        scanner?.setParams(0x191, 1)//enableUPCE
        scanner?.setParams(0x211, 1)//enableUPCE
        scanner?.setParams(0x212, 1)//enableUPCE
        scanner?.setParams(0x213, 1)//enableUPCE 0x251
        scanner?.setParams(0x251, 1)//enableUPCE
        scanner?.setParams(0x234, 0)//disableDataMatrixInverseOnly
        scanner?.setParams(0x136, 0)//enableCode39MinimumLength
        scanner?.setParams(0x10c, 1) // enableUPCA to Prefix
    }

    fun prepare() {
        if (scanner == null) {
            init()
        }
    }

    fun pause() {
        mScanKeyService.unregisterCallback(mCallback)
        if (scanner != null) {
            scanner!!.close()
            scanner!!.setDecodeInfoCallBack(null)
            scanner = null
        }
    }

    fun release() {
        mScanKeyService.unregisterCallback(mCallback)
        if (scanner != null) {
            scanner!!.close()
            scanner!!.setDecodeInfoCallBack(null)
            scanner = null
        }
    }

    override fun onDecodeComplete(p0: DecodeInfo?) {
        if (p0?.codetype?.contains("EAN-13")!!) {
            Log.i("seuic", "this  is  EAN-13")
        } else {
            Log.i("seuic", "this  is not EAN-13")
        }
        callBack(p0.barcode)
        sp.play(soundId, 1F, 1F, 0, 0, 1F)
    }
}