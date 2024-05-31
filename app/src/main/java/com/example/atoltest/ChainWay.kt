package com.example.atoltest

import android.content.Context
import android.content.Intent
import android.os.Bundle


class ChainWay(private val mContext: Context, private val callBack: (String) -> Unit) {



    fun init() {
        val bMain = Bundle()
        bMain.putString("PROFILE_NAME", "NewProfile") // 配置文件名/profile name
        bMain.putString("PROFILE_ENABLED", "true") // 启用该配置文件/enable this profile
        bMain.putString(
            "CONFIG_MODE",
            "UPDATE"
        ) // 将配置合并到已有的配置文件/Merge the configuration into an existing profile

        val bConfig = Bundle()
        bConfig.putString("PLUGIN_NAME", "BARCODE") // 设置类型：扫码头/type:scanner
        bConfig.putString("RESET_CONFIG", "true") // 重置原有扫码头配置/reset to previous settings

        val bParams = Bundle()
        bParams.putString("barcode_enabled", "true") // 是否启用扫码头/enable or disable scanner
        bParams.putString(
            "barcode_trigger_mode",
            "0"
        )
        bParams.putString(
            "charset_name",
            "Auto"
        ) // 解码使用的数据集/：Auto，UTF-8，GBK，GB18030，ISO-8859-1，Shift_JIS
        bParams.putString("success_audio", "true") // 扫码成功时播放提示音/ success sound
        bParams.putString("failure_audio", "false") // 扫码失败时播放提示音 /failure sound
        bParams.putString(
            "vibrate",
            "false"
        ) // 扫码成功时是否震动提示/whether to vibrate when scanning successfully
        bParams.putString("decoder_code11", "true") // 启用 Code11 条码/eable code 11
        bParams.putString("decoder_code128", "true") // 禁用 Code128 条码/eable code 128

        bConfig.putBundle("PARAM_LIST", bParams)
        bMain.putBundle("PLUGIN_CONFIG", bConfig)

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