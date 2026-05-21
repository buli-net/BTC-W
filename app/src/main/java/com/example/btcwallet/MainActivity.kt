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
        if (!WalletManager.hasWallet(this)) {
            startActivity(Intent(this, CreateImportActivity::class.java))
            finish()
            return
        }
        binding.btnReceive.setOnClickListener { startActivity(Intent(this, PasswordActivity::class.java).putExtra("action","receive")) }
        binding.btnSend.setOnClickListener { startActivity(Intent(this, PasswordActivity::class.java).putExtra("action","send")) }
        binding.btnManage.setOnClickListener { startActivity(Intent(this, PasswordActivity::class.java).putExtra("action","manage")) }
    }
}