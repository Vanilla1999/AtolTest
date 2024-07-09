package com.example.atoltest

import android.content.Context
import android.content.Intent
import android.os.Bundle


class ChainWay(private val mContext: Context, private val callBack: (String) -> Unit) {

    private fun initMain() {
        // 主参数设置 main parameter setting
        // 主参数设置 main parameter setting
        val bMain = Bundle()
        bMain.putString("PROFILE_NAME", "NewProfile") // 配置文件名

        bMain.putString("PROFILE_ENABLED", "true") // 启用该配置文件

        bMain.putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST") // 如果配置文件不存在则创建


// 设置关联应用程序/Set the associated application

// 设置关联应用程序/Set the associated application
        val bundleApp1 = Bundle()
        bundleApp1.putString(
            "PACKAGE_NAME",
            "com.example.atoltest"
        ) // 关联应用程序的包名/associated application package names

        bundleApp1.putStringArray(
            "ACTIVITY_LIST",
            arrayOf("com.example.atoltest.MainActivity")
        ) // 关联的 Activity 列表


        bMain.putParcelableArray(
            "APP_LIST", arrayOf(
                bundleApp1,
            )
        )

        val i = Intent()
        i.setAction("com.symbol.infowedge.api.ACTION")
        i.putExtra("com.symbol.infowedge.api.SET_CONFIG", bMain)
        mContext.sendBroadcast(i)
    }

    fun init() {
        initMain();
        Thread.sleep(1_000)
        println("заиничил, идём дальше");
        val bMain = Bundle()
        bMain.putString("PROFILE_NAME", "NewProfile") // 配置文件名/profile name
        bMain.putString("PROFILE_ENABLED", "true") // 启用该配置文件/enable this profile
        bMain.putString(
            "CONFIG_MODE",
            "UPDATE"
        ) // 将配置合并到已有的配置文件/Merge the configuration into an existing profile



        val bBarcodeConfig = Bundle()
        bBarcodeConfig.putString("PLUGIN_NAME", "BARCODE") // 设置类型：扫码头/type:scanner
        bBarcodeConfig.putString("RESET_CONFIG", "true") // 重置原有扫码头配置/reset to previous settings

        val bBarcodeParams = Bundle()
        bBarcodeParams.putString("barcode_enabled", "true") // 是否启用扫码头/enable or disable scanner
        bBarcodeParams.putString(
            "barcode_trigger_mode",
            "2"
        )
        bBarcodeParams.putString(
            "charset_name",
            "Auto"
        ) // 解码使用的数据集/：Auto，UTF-8，GBK，GB18030，ISO-8859-1，Shift_JIS
        bBarcodeParams.putString("success_audio", "false") // 扫码成功时播放提示音/ success sound
        bBarcodeParams.putString("failure_audio", "false") // 扫码失败时播放提示音 /failure sound
        bBarcodeParams.putString(
            "vibrate",
            "false"
        ) // 扫码成功时是否震动提示/whether to vibrate when scanning successfully
        bBarcodeParams.putString("decoder_code11", "true") // 启用 Code11 条码/eable code 11
        bBarcodeParams.putString("decoder_code128", "true") // 禁用 Code128 条码/eable code 128
        bBarcodeParams.putString("decoder_upca", "true") // 启用 Code11 条码/eable code 11
        bBarcodeParams.putString("decoder_upca", "true")
        bBarcodeParams.putString("decoder_rss", "false") // 禁用 Code128 条码/eable code 128
        bBarcodeParams.putString("decoder_ean13", "true")
        bBarcodeParams.putString("decoder ean13 report check digit","false")
        bBarcodeParams.putString("decoder_ean13_digit5", "true")
        bBarcodeParams.putString("barcode_trigger_mode", "2")
        bBarcodeConfig.putBundle("PARAM_LIST", bBarcodeParams)




        val bIntentConfig = Bundle()
        bIntentConfig.putString("PLUGIN_NAME", "INTENT") // 设置类型：INTENT
        bIntentConfig.putString("RESET_CONFIG", "true") // 重置原有广播输出配置/reset to intent config
        val bIntentParams = Bundle()
        bIntentParams.putString("intent_output_enabled", "true") // 是否启用广播输出/whether enable intent output
        bIntentParams.putString("intent_action", "com.infowedge.data");   // 设置广播输出的 action/ set intent action
        bIntentParams.putString("intent_data", "data_string");   // 设置广播输出的数据名称/set intent output data name
        bIntentConfig.putBundle("PARAM_LIST", bIntentParams)
        val bBDFConfig = Bundle()
        bBDFConfig.putString("PLUGIN_NAME", "BDF")
        bBDFConfig.putString("RESET_CONFIG", "true")
        val bBDFParams = Bundle()
        bBDFParams.putString("bdf_enabled", "true")
        bBDFParams.putString("bdf_send_enter", "true")
        bBDFConfig.putBundle("PARAM_LIST", bBDFParams)
        val bundlePluginConfig: ArrayList<Bundle> = ArrayList<Bundle>()
        bundlePluginConfig.add(bBarcodeConfig )
        bundlePluginConfig.add(bIntentConfig)
        bundlePluginConfig.add(bBDFConfig)
        bMain.putParcelableArrayList("PLUGIN_CONFIG", bundlePluginConfig)


        val i = Intent()
        i.setAction("com.symbol.infowedge.api.ACTION")
        i.putExtra("com.symbol.infowedge.api.SET_CONFIG", bMain)
        mContext.sendBroadcast(i)
    }

    fun prepare() {

    }

    fun pause() {

    }

    fun release() {

    }
}