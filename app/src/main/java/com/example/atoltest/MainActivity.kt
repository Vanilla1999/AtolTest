package com.example.atoltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    lateinit var movfast: Movfast
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        movfast = Movfast(this) { barcode ->
            Log.d("barcode", barcode)
        }
        movfast.init()
    }

    override fun onPause() {
        super.onPause()
        movfast.pause()
    }

    override fun onResume() {
        super.onResume()
        movfast.prepare()
    }

    override fun onDestroy() {
        super.onDestroy()
        movfast.release()
    }
}