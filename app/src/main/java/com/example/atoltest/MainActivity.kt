package com.example.atoltest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.superlead.sdk.service.BarcodeService

class MainActivity : AppCompatActivity() {
    lateinit var mertech: Mertech
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService(Intent(this, BarcodeService::class.java))
        val barcodeView = findViewById<TextView>(R.id.barcode)
        mertech = Mertech(this) { barcode ->
            barcodeView.text = barcode
        }
        mertech.init()
    }

    override fun onPause() {
        super.onPause()
        mertech.pause()
    }

    override fun onResume() {
        super.onResume()
        mertech.prepare()
    }

    override fun onDestroy() {
        super.onDestroy()
        mertech.release()
    }
}