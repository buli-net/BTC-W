package com.example.btcwallet

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tv = TextView(this)
        tv.text = "APP MOI - BTCWALLET2"
        tv.textSize = 32f
        tv.setPadding(50,200,50,200)
        setContentView(tv)
        Toast.makeText(this, "DAY LA BAN MOI", Toast.LENGTH_LONG).show()
    }
}