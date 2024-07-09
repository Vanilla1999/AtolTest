package com.example.atoltest

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    lateinit var chainWay: ChainWay
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chainWay = ChainWay(this) { barcode ->
            Log.d("barcode", barcode)
        }
        chainWay.init()
        findViewById<EditText>(R.id.text1).setOnKeyListener { v, keyCode, event -> run {
            Log.d("keys", event.toString());
        }
          true
        }
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