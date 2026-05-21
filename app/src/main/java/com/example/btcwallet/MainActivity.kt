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

        // ÉP LUÔN VÀO MÀN TẠO VÍ - bỏ qua ví cũ
        WalletManager.deleteWallet(this) // xóa sạch keystore cũ
        startActivity(Intent(this, CreateImportActivity::class.java))
        finish()
    }
}