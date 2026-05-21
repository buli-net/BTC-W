package com.example.btcwallet
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.btcwallet.databinding.ActivityCreateImportBinding

class CreateImportActivity : AppCompatActivity() {
    private lateinit var b: ActivityCreateImportBinding
    override fun onCreate(s: Bundle?) {
        super.onCreate(s); b = ActivityCreateImportBinding.inflate(layoutInflater); setContentView(b.root)
        b.btnCreate.setOnClickListener {
            val pw = b.etPassword.text.toString()
            if (pw.length < 6) { Toast.makeText(this,"MK ≥6 ký tự",Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            val m = WalletManager.createWallet(this, pw)
            AlertDialog.Builder(this).setTitle("LƯU 12 TỪ NÀY").setMessage(m.joinToString(" "))
                .setPositiveButton("Đã lưu"){_,_-> startActivity(Intent(this, MainActivity::class.java)); finish()}.setCancelable(false).show()
        }
        b.btnImport.setOnClickListener {
            val ok = WalletManager.importWallet(this, b.etSeed.text.toString(), b.etPassword.text.toString())
            if(ok){ startActivity(Intent(this, MainActivity::class.java)); finish() }
            else Toast.makeText(this,"Seed sai",Toast.LENGTH_SHORT).show()
        }
    }
}