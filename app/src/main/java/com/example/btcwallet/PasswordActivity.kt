package com.example.btcwallet
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PasswordActivity : AppCompatActivity() {
    override fun onCreate(s: Bundle?) {
        super.onCreate(s); setContentView(R.layout.activity_password)
        val et = findViewById<EditText>(R.id.etPassword)
        val btn = findViewById<Button>(R.id.btnUnlock)
        val act = intent.getStringExtra("action")
        btn.setOnClickListener {
            val pw = et.text.toString()
            if (WalletManager.verifyPassword(this, pw)) {
                val i = when(act) {
                    "receive" -> Intent(this, ReceiveActivity::class.java)
                    "send" -> Intent(this, SendActivity::class.java)
                    "manage" -> Intent(this, ManageActivity::class.java)
                    else -> Intent(this, MainActivity::class.java)
                }
                i.putExtra("password", pw); startActivity(i); finish()
            } else Toast.makeText(this,"Sai mật khẩu",Toast.LENGTH_SHORT).show()
        }
    }
}