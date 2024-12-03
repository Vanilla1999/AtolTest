package com.example.atoltest

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.m3.sdk.scannerlib.BarcodeListener
import com.m3.sdk.scannerlib.BarcodeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.InvocationTargetException


class M3(private val mContext: Context, private val callBack: (String) -> Unit) {
    private var mListener: BarcodeListener? = null
    private var mManager: BarcodeManager? = null
    var needScan = false;

    fun init() {
        mManager = BarcodeManager(mContext)
        mListener = object : BarcodeListener {
            override fun onBarcode(strBarcode: String) {
                CoroutineScope(Dispatchers.Main).launch {
                    if (needScan)
                        callBack(strBarcode)
                }
            }

            override fun onBarcode(s: String, s1: String) {
            }

            override fun onGetSymbology(i: Int, i1: Int) {
            }
        }
        mManager?.addListener(mListener)
        val intent = Intent(SCANNER_ACTION_PARAMETER)
        intent.putExtra("symbology", 293)
        intent.putExtra("value", 0)
        mContext.sendOrderedBroadcast(intent, null)
        set(WAKE_PROP_LSCAN, "false")
    }

    fun prepare() {
        needScan = true
    }

    fun pause() {
        needScan = false
    }

    fun release() {
    }
    @SuppressLint("PrivateApi")
    private fun set(key: String?, value: String?) {
        try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val setMethod = systemPropertiesClass.getDeclaredMethod("set", String::class.java, String::class.java)
            setMethod.isAccessible = true
            setMethod.invoke(null, key, value)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }
}

const val SCANNER_ACTION_SETTING_CHANGE: String = "com.android.server.scannerservice.settingchange"
const val SCANNER_ACTION_PARAMETER: String = "android.intent.action.SCANNER_PARAMETER"
const val SCANNER_ACTION_ENABLE: String = "com.android.server.scannerservice.m3onoff"
const val SCANNER_EXTRA_ENABLE: String = "scanneronoff"
const val WAKE_PROP_LSCAN: String = "persist.sys.scan_left.wakeup"