package com.example.btcwallet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.btcwallet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nếu chưa có ví → chuyển sang màn tạo/khôi phục
        if (!WalletManager.hasWallet(this)) {
            startActivity(Intent(this, CreateImportActivity::class.java))
            finish()
            return
        }

        // Đã có ví → 3 nút chính
        binding.btnReceive.setOnClickListener {
            val intent = Intent(this, PasswordActivity::class.java)
            intent.putExtra("action", "receive")
            startActivity(intent)
        }

        binding.btnSend.setOnClickListener {
            val intent = Intent(this,