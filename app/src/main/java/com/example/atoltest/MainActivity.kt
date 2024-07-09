package com.example.atoltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    lateinit var chainWay: Mertech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chainWay = Mertech(this) { barcode ->
            Log.d("barcode", barcode)
        }
        chainWay.init()
    }

    override fun onPause() {
        super.onPause()
        chainWay.pause()
    }

    override fun onResume() {
        super.onResume()
        chainWay.prepare()
    }

    override fun onDestroy() {
        super.onDestroy()
        chainWay.release()
    }
}