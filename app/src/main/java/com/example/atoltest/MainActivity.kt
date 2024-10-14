package com.example.atoltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var urovo: Urovo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        urovo = Urovo(this) { barcode ->
            findViewById<TextView>(R.id.text).text = barcode
        }
        urovo.init()
    }

    override fun onPause() {
        super.onPause()
        urovo.pause()
    }

    override fun onResume() {
        super.onResume()
        urovo.prepare()
    }

    override fun onDestroy() {
        super.onDestroy()
        urovo.release()
    }
}