package com.example.atoltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var seuic: Seuic
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seuic = Seuic(this) { barcode ->
            findViewById<TextView>(R.id.text).text = barcode
        }
        seuic.init()
    }

    override fun onPause() {
        super.onPause()
        seuic.pause()
    }

    override fun onResume() {
        super.onResume()
        seuic.prepare()
    }

    override fun onDestroy() {
        super.onDestroy()
        seuic.release()
    }
}