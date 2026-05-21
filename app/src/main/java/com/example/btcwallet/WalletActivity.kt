package com.example.btcwallet

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WalletActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tv = TextView(this)
        tv.text = "Ví đã mở thành công!\n(Đây là màn hình ví tạm)"
        tv.textSize = 20f
        tv.setPadding(50, 200, 50, 50)
        setContentView(tv)
    }
}