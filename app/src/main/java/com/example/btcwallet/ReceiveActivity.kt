package com.example.btcwallet

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.wallet.Wallet

/**
 * Bước 4: Màn Nhận
 * - Hiện địa chỉ ví đầy đủ
 * - Tạo QR code để người khác quét
 * - Nút copy (sẽ thêm ở bước sau)
 */
class ReceiveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tvAddr = TextView(this).apply {
            textSize = 16f
            setPadding(20,20,20,20)
            setTextIsSelectable(true) // cho phép copy
        }
        val imgQR = ImageView(this)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 100, 40, 0)
            addView(tvAddr)
            addView(imgQR)
        }
        setContentView(layout)

        // 1. Lấy seed ví đang dùng
        val seed = WalletManager.getCurrentSeed(this)
        if (seed == null) {
            Toast.makeText(this, "Chưa có ví", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 2. Tạo địa chỉ từ seed
        val wallet = Wallet.fromSeed(
            MainNetParams.get(),
            org.bitcoinj.wallet.DeterministicSeed(seed, null, "", 0)
        )
        val address = wallet.currentReceiveAddress().toString()
        tvAddr.text = address

        // 3. Tạo QR code từ địa chỉ
        try {
            val bitmap: Bitmap = BarcodeEncoder().encodeBitmap(
                address,
                BarcodeFormat.QR_CODE,
                800, 800 // kích thước QR
            )
            imgQR.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi tạo QR", Toast.LENGTH_SHORT).show()
        }
    }
}