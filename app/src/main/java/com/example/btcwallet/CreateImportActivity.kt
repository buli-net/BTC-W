package com.example.btcwallet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.wallet.Wallet

/**
 * Bước 2: Sau khi có mật khẩu, cho phép tạo ví mới hoặc import
 * - Chỉ dùng Bitcoin MAINNET (không testnet)
 * - Ví tạo ra là loại SegWit (bc1q...)
 */
class CreateImportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Tạo giao diện đơn giản bằng code (đỡ phải tạo xml)
        val btnNew = Button(this).apply { text = "Tạo ví mới" }
        val btnImport = Button(this).apply { text = "Import seed 12 từ" }
        
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 300, 40, 0)
            addView(btnNew)
            addView(btnImport)
        }
        setContentView(layout)

        // Nút TẠO VÍ MỚI
        btnNew.setOnClickListener {
            // 1. Lấy tham số mạng Bitcoin chính
            val params = MainNetParams.get()
            
            // 2. Tạo ví deterministic mới (tự sinh 12 từ)
            val wallet = Wallet.createDeterministic(params, org.bitcoinj.script.Script.ScriptType.P2WPKH)
            
            // 3. Lấy seed 12 từ
            val seed = wallet.keyChainSeed.mnemonicCode!!.joinToString(" ")
            
            // 4. Lưu vào bộ nhớ mã hóa (WalletManager ở file sau)
            WalletManager.saveWallet(this, "Ví 1", seed)
            
            Toast.makeText(this, "Đã tạo ví!\nSeed: $seed", Toast.LENGTH_LONG).show()
            
            // 5. Vào màn hình chính
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Nút IMPORT (tạm thời, code chi tiết ở bước 5)
        btnImport.setOnClickListener {
            Toast.makeText(this, "Sẽ làm ở màn Quản lý", Toast.LENGTH_SHORT).show()
        }
    }
}