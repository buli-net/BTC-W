package com.example.btcwallet
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class ReceiveActivity : AppCompatActivity() {
    override fun onCreate(s: Bundle?) {
        super.onCreate(s); setContentView(R.layout.activity_receive)
        val pw = intent.getStringExtra("password") ?: return
        val addr = WalletManager.getAddress(this, pw) ?: "Lỗi"
        findViewById<TextView>(R.id.tvAddress).text = addr
        val bmp = BarcodeEncoder().encodeBitmap(addr, BarcodeFormat.QR_CODE, 600, 600)
        findViewById<ImageView>(R.id.ivQr).setImageBitmap(bmp)
    }
}