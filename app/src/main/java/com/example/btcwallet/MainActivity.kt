package com.example.btcwallet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // FIX: Kiểm tra ví, không xóa nữa
        if (WalletManager.hasWallet(this)) {
            // Đã có ví (12 từ bạn tạo lúc nãy) → yêu cầu mật khẩu
            startActivity(Intent(this, PasswordActivity::class.java))
        } else {
            // Chưa có ví → tạo/khôi phục
            startActivity(Intent(this, CreateImportActivity::class.java))
        }
        finish()
    }
}