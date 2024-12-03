package com.example.atoltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var m3: M3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        m3 = M3(this) { barcode ->
            findViewById<TextView>(R.id.text).text = barcode
        }
        m3.init()
    }

    override fun onPause() {
        super.onPause()
        m3.pause()
    }

    override fun onResume() {
        super.onResume()
        m3.prepare()
    }

    override fun onDestroy() {
        super.onDestroy()
        m3.release()
    }
}