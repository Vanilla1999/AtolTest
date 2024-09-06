package com.example.atoltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    lateinit var newLand: NewLand
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        newLand = NewLand(this) { barcode ->
            Log.d("barcode", barcode)
        }
        newLand.init()
    }

    override fun onPause() {
        super.onPause()
        newLand.pause()
    }

    override fun onResume() {
        super.onResume()
        newLand.prepare()
    }

    override fun onDestroy() {
        super.onDestroy()
        newLand.release()
    }
}