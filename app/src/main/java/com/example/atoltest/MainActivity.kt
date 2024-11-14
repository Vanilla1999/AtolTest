package com.example.atoltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var wintec: Wintec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        wintec = Wintec(this) { barcode ->
            findViewById<TextView>(R.id.text).text = barcode
        }
        wintec.init()
    }

    override fun onPause() {
        super.onPause()
        wintec.pause()
    }

    override fun onResume() {
        super.onResume()
        wintec.prepare()
    }

    override fun onDestroy() {
        super.onDestroy()
        wintec.release()
    }
}