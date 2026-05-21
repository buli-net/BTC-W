package com.example.btcwallet

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

/**
 * Bước 6: Màn Quản lý
 * - Xuất seed (đúng ví đang dùng)
 * - Tạo ví mới, Import seed
 * - Danh sách ví, bấm vào để đổi tên/xóa/xem chi tiết
 * - Đổi mật khẩu
 */
class ManageActivity : AppCompatActivity() {
    
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Tạo các nút
        val btnExport = Button(this).apply { text = "Xuất seed ví đang dùng" }
        val btnNew = Button(this).apply { text = "Tạo ví mới" }
        val btnImport = Button(this).apply { text = "Import seed ví" }
        val btnChangePass = Button(this).apply { text = "Đổi mật khẩu" }
        
        listView = ListView(this)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(30, 30, 30, 30)
            addView(btnExport); addView(btnNew); addView(btnImport)
            addView(btnChangePass)
            addView(TextView(context).apply { text = "\nDanh sách ví:" })
            addView(listView)
        }
        setContentView(layout)

        refreshList()

        // 2. Xuất seed
        btnExport.setOnClickListener {
            val seed = WalletManager.getCurrentSeed(this)
            AlertDialog.Builder(this)
                .setTitle("Seed 12 từ - BẢO MẬT")
                .setMessage(seed ?: "Chưa có ví")
                .setPositiveButton("Đã lưu") { _, _ -> }
                .show()
        }

        // 3. Tạo ví mới
        btnNew.setOnClickListener {
            val name = "Ví ${System.currentTimeMillis() % 1000}"
            // Tạo ví mới sẽ làm ở CreateImport, ở đây tạo nhanh
            WalletManager.saveWallet(this, name, "")
            Toast.makeText(this, "Đã tạo $name", Toast.LENGTH_SHORT).show()
            refreshList()
        }

        // 4. Import seed
        btnImport.setOnClickListener {
            val input = EditText(this)
            AlertDialog.Builder(this)
                .setTitle("Nhập 12 từ seed")
                .setView(input)
                .setPositiveButton("Import") { _, _ ->
                    val seed = input.text.toString().trim()
                    if (seed.split(" ").size == 12) {
                        WalletManager.saveWallet(this, "Ví import", seed)
                        Toast.makeText(this, "Đã import", Toast.LENGTH_SHORT).show()
                        refreshList()
                    } else {
                        Toast.makeText(this, "Seed phải 12 từ", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Hủy", null)
                .show()
        }

        // 5. Đổi mật khẩu
        btnChangePass.setOnClickListener {
            Toast.makeText(this, "Quay lại màn Password để đổi", Toast.LENGTH_SHORT).show()
        }

        // 6. Bấm vào ví trong danh sách
        listView.setOnItemClickListener { _, _, pos, _ ->
            val arr = WalletManager.getAll(this)
            val wallet = arr.getJSONObject(pos)
            
            AlertDialog.Builder(this)
                .setTitle(wallet.getString("name"))
                .setItems(arrayOf("Chọn làm ví đang dùng", "Đổi tên ví", "Xóa ví", "Chi tiết ví")) { _, which ->
                    when (which) {
                        0 -> Toast.makeText(this, "Đã chọn", Toast.LENGTH_SHORT).show()
                        1 -> { // Đổi tên
                            val input = EditText(this).apply { setText(wallet.getString("name")) }
                            AlertDialog.Builder(this).setTitle("Tên mới")
                                .setView(input)
                                .setPositiveButton("OK") { _, _ ->
                                    wallet.put("name", input.text.toString())
                                    refreshList()
                                }.show()
                        }
                        2 -> Toast.makeText(this, "Xóa ví", Toast.LENGTH_SHORT).show()
                        3 -> Toast.makeText(this, "ID: ${wallet.getString("id")}", Toast.LENGTH_LONG).show()
                    }
                }.show()
        }
    }

    private fun refreshList() {
        val arr: JSONArray = WalletManager.getAll(this)
        val names = mutableListOf<String>()
        for (i in 0 until arr.length()) {
            names.add(arr.getJSONObject(i).getString("name"))
        }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, names)
        listView.adapter = adapter
    }
}