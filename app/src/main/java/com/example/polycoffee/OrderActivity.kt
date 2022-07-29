package com.example.polycoffee

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polycoffee.adapter.AdapterMenu
import com.example.polycoffee.databinding.FragmentMenuBinding
import com.example.polycoffee.fragments.MenuFragment
import com.example.polycoffee.model.LoaiSanPham

class OrderActivity : AppCompatActivity() {
    lateinit var binding:FragmentMenuBinding
    lateinit var adapter: AdapterMenu
    lateinit var recyclerView: RecyclerView
    var listLoaiSP=ArrayList<LoaiSanPham>()
    var maBan = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.menuFab.isVisible = false
        binding.orderSuccessBtn.isVisible = true

        binding.orderSuccessBtn.setOnClickListener {
            onBackPressed()
        }

        maBan = intent.getStringExtra("maBan").toString()

        updateRecyclerView()
        MenuFragment().getListLSP(listLoaiSP,adapter)

    }

    fun updateRecyclerView(){
        listLoaiSP = ArrayList<LoaiSanPham>()
        recyclerView = binding.menuRecyclerView
        adapter = AdapterMenu(this,listLoaiSP,MenuFragment(),1,maBan)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}