package com.example.atoltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var atol: Atol
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        atol = Atol(this) { barcode ->
            findViewById<TextView>(R.id.text).text = barcode
        }
        atol.init()
    }

    override fun onPause() {
        super.onPause()
        atol.pause()
    }

    override fun onResume() {
        super.onResume()
        atol.prepare()
    }

    override fun onDestroy() {
        super.onDestroy()
        atol.release()
    }
}