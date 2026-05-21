package com.example.btcwallet

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

/**
 * Bước 5: Màn Gửi
 * - Nhập địa chỉ nhận + nút Quét QR
 * - Nhập số BTC
 * - Chọn phí: Tiết kiệm/Thường/Nhanh/Tùy chỉnh
 * - Hiện ước tính phí và tổng BTC cần gửi
 */
class SendActivity : AppCompatActivity() {

    private lateinit var etAddr: EditText
    private lateinit var etAmount: EditText
    private lateinit var tvEstimate: TextView
    private var currentSatPerVb = 10 // mặc định Thường

    // Đăng ký quét QR
    private val qrLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            etAddr.setText(result.contents) // điền địa chỉ vừa quét
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Tạo giao diện
        etAddr = EditText(this).apply { hint = "Địa chỉ ví nhận" }
        val btnScan = Button(this).apply { text = "Quét QR địa chỉ" }
        etAmount = EditText(this).apply { 
            hint = "Số BTC muốn gửi"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or 
                       android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
        
        val spinnerFee = Spinner(this)
        val feeOptions = arrayOf("Tiết kiệm (5 sat/vB)", "Thường (10 sat/vB)", 
                                "Nhanh (20 sat/vB)", "Tùy chỉnh")
        spinnerFee.adapter = ArrayAdapter(this, 
            android.R.layout.simple_spinner_dropdown_item, feeOptions)
        
        val etCustomFee = EditText(this).apply {
            hint = "Nhập sat/vB"
            visibility = View.GONE
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }
        
        tvEstimate = TextView(this).apply {
            text = "Ước tính phí: 0 BTC\nTổng cần gửi: 0 BTC"
            textSize = 16f
        }
        
        val btnSend = Button(this).apply { text = "GỬI" }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 60, 40, 0)
            addView(etAddr); addView(btnScan); addView(etAmount)
            addView(TextView(context).apply { text = "Chọn phí gửi:" })
            addView(spinnerFee); addView(etCustomFee)
            addView(tvEstimate); addView(btnSend)
        }
        setContentView(layout)

        // 2. Nút quét QR
        btnScan.setOnClickListener {
            qrLauncher.launch(ScanOptions().apply {
                setPrompt("Quét địa chỉ BTC")
                setBeepEnabled(true)
                setOrientationLocked(false)
            })
        }

        // 3. Khi chọn phí
        spinnerFee.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                when (pos) {
                    0 -> { currentSatPerVb = 5; etCustomFee.visibility = View.GONE }
                    1 -> { currentSatPerVb = 10; etCustomFee.visibility = View.GONE }
                    2 -> { currentSatPerVb = 20; etCustomFee.visibility = View.GONE }
                    3 -> { etCustomFee.visibility = View.VISIBLE }
                }
                updateEstimate()
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }

        // 4. Cập nhật ước tính khi nhập số
        etAmount.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) { updateEstimate() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        
        etCustomFee.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) { 
                currentSatPerVb = s.toString().toIntOrNull() ?: 10
                updateEstimate() 
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 5. Nút gửi
        btnSend.setOnClickListener {
            Toast.makeText(this, "Gửi ${etAmount.text} BTC đến ${etAddr.text}\nPhí: $currentSatPerVb sat/vB", 
                Toast.LENGTH_LONG).show()
            // Code broadcast thật sẽ cần node, để ở phiên bản sau
        }
    }

    // Hàm tính ước phí
    private fun updateEstimate() {
        val amount = etAmount.text.toString().toDoubleOrNull() ?: 0.0
        // Ước tính giao dịch ~250 vbyte (1 input, 2 output)
        val feeBTC = (currentSatPerVb * 250) / 100_000_000.0
        val total = amount + feeBTC
        
        tvEstimate.text = "Ước tính phí: %.8f BTC\nTổng cần gửi: %.8f BTC".format(feeBTC, total)
    }
}