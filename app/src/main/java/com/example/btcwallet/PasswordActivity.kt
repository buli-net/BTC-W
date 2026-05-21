package com.example.btcwallet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        val et = findViewById<EditText>(R.id.etPassword)
        val btn = findViewById<Button>(R.id.btnUnlock)
        val action = intent.getStringExtra("action")
        btn.setOnClickListener {
            val pw = et.text.toString()
            if (WalletManager.verifyPassword(this, pw)) {
                val target = when(action) {
                    "receive" -> ReceiveActivity::class.java
                    "send" -> SendActivity::class.java
                    "manage" -> ManageActivity::class.java
                    else -> WalletActivity::class.java   // <-- FIX Ở ĐÂY
                }
                startActivity(Intent(this, target).putExtra("password", pw))
                finish()
            } else Toast.makeText(this, "Sai MK", Toast.LENGTH_SHORT).show()
        }
    }
}