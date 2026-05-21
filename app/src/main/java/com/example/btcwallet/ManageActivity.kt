package com.example.btcwallet
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.btcwallet.databinding.ActivityManageBinding

class ManageActivity : AppCompatActivity() {
    private lateinit var b: ActivityManageBinding
    override fun onCreate(s: Bundle?) {
        super.onCreate(s); b = ActivityManageBinding.inflate(layoutInflater); setContentView(b.root)
        val pw = intent.getStringExtra("password") ?: return
        b.btnShowSeed.setOnClickListener {
            val m = WalletManager.getMnemonic(this, pw)
            AlertDialog.Builder(this).setTitle("12 từ khóa").setMessage(m?.joinToString(" ") ?: "Lỗi").setPositiveButton("OK",null).show()
        }
        b.btnDelete.setOnClickListener {
            AlertDialog.Builder(this).setTitle("Xóa ví").setMessage("Xóa vĩnh viễn?")
                .setPositiveButton("Xóa"){_,_-> WalletManager.deleteWallet(this); Toast.makeText(this,"Đã xóa",Toast.LENGTH_SHORT).show(); finishAffinity()}
                .setNegativeButton("Hủy",null).show()
        }
    }
}