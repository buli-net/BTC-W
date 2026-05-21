package com.example.btcwallet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.btcwallet.databinding.ActivityCreateImportBinding

class CreateImportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateImportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateImportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnCreate.setOnClickListener {
            val pw = binding.etPassword.text.toString()
            if (pw.length < 6) { Toast.makeText(this,"MK>=6",Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            val words = WalletManager.createWallet(this, pw)
            AlertDialog.Builder(this).setTitle("Lưu 12 từ").setMessage(words.joinToString(" "))
                .setPositiveButton("OK") { _,_ -> startActivity(Intent(this, MainActivity::class.java)); finish() }
                .setCancelable(false).show()
        }
        binding.btnImport.setOnClickListener {
            val ok = WalletManager.importWallet(this, binding.etSeed.text.toString(), binding.etPassword.text.toString())
            if (ok) { startActivity(Intent(this, MainActivity::class.java)); finish() }
            else Toast.makeText(this,"Seed sai",Toast.LENGTH_SHORT).show()
        }
    }
}