package com.example.btcwallet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ÉP XÓA VÍ CŨ - bỏ qua mật khẩu
        WalletManager.deleteWallet(this)

        // Vào thẳng màn tạo/khôi phục ví
        startActivity(Intent(this, CreateImportActivity::class.java))
        finish()
    }
}