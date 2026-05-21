package com.example.btcwallet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.material.button.Button
import com.google.android.material.textfield.TextInputEditText
import java.security.MessageDigest

/**
 * Bước 1: Màn hình bắt buộc tạo mật khẩu
 * - Dùng Android Keystore (qua EncryptedSharedPreferences) nên an toàn
 * - Tự sáng/tối theo điện thoại
 */
class PasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password) // layout sẽ tạo ở file 2

        // 1. Tạo khóa master nằm trong chip bảo mật của máy
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        // 2. Tạo bộ nhớ mã hóa
        val prefs = EncryptedSharedPreferences.create(
            this, "secure_prefs", masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        // 3. Nếu đã có pass rồi thì bỏ qua (sau này vào Main)
        if (prefs.getBoolean("password_set", false)) {
            // TODO bước 2 sẽ chuyển màn
            return
        }

        // 4. Lấy các ô nhập
        val etPass = findViewById<TextInputEditText>(R.id.etPassword)
        val etConfirm = findViewById<TextInputEditText>(R.id.etConfirm)
        val btnCreate = findViewById<Button>(R.id.btnCreate)

        // 5. Khi bấm Tạo
        btnCreate.setOnClickListener {
            val p1 = etPass.text.toString()
            val p2 = etConfirm.text.toString()
            
            if (p1.length < 6) { toast("Mật khẩu tối thiểu 6 ký tự"); return@setOnClickListener }
            if (p1 != p2) { toast("Không khớp"); return@setOnClickListener }

            // 6. Băm SHA-256, không lưu pass gốc
            val hash = MessageDigest.getInstance("SHA-256")
                .digest(p1.toByteArray()).joinToString("") { "%02x".format(it) }

            // 7. Lưu vào bộ nhớ mã hóa
            prefs.edit().putString("password_hash", hash).putBoolean("password_set", true).apply()
            toast("Đã tạo mật khẩu")
        }
    }
    private fun toast(s:String)= Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
}