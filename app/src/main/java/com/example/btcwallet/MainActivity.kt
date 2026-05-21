package com.example.btcwallet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (WalletManager.hasWallet(this)) {
            // Đã có ví → vào ví luôn
            startActivity(Intent(this, WalletActivity::class.java))
        } else {
            // Chưa có → tạo mới
            startActivity(Intent(this, CreateImportActivity::class.java))
        }
        finish()
    }
}