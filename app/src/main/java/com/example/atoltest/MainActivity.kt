package com.example.atoltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    lateinit var honeywell: Honeywell
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        honeywell = Honeywell(this) { barcode ->
            Log.d("barcode", barcode)
        }
        honeywell.init()
    }

    override fun onPause() {
        super.onPause()
        honeywell.pause()
    }

    override fun onResume() {
        super.onResume()
        honeywell.prepare()
    }

    override fun onDestroy() {
        super.onDestroy()
        honeywell.release()
    }
}