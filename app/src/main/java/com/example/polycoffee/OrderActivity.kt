package com.example.polycoffee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.polycoffee.fragments.MenuFragment
import com.example.polycoffee.model.LoaiSanPham

class OrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_menu)
    }
}