package com.example.atoltest

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    lateinit var meferi: Meferi
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        meferi = Meferi(this) { barcode ->
            Log.d("barcode", barcode)
        }
        meferi.init()
        findViewById<EditText>(R.id.text1).setOnKeyListener { v, keyCode, event -> run {
            Log.d("keys", event.toString());
        }
          true
        }
    }

    override fun onPause() {
        super.onPause()
        meferi.pause()
    }

    override fun onResume() {
        super.onResume()
        meferi.prepare()
    }

    override fun onDestroy() {
        super.onDestroy()
        meferi.release()
    }
}