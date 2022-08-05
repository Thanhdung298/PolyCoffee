package com.example.polycoffee

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polycoffee.adapter.AdapterMenu
import com.example.polycoffee.databinding.FragmentMenuBinding
import com.example.polycoffee.fragments.MenuFragment
import com.example.polycoffee.model.LoaiSanPham
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
        supportActionBar?.hide()
        binding.rootFragMenu.setBackgroundResource(R.drawable.background10)
        binding.rootFragMenu.background.alpha = 130
        binding.menuFab.isVisible = false
        binding.orderSuccessBtn.isVisible = true

        binding.orderSuccessBtn.setOnClickListener {
            onBackPressed()
        }

        maBan = intent.getStringExtra("maBan").toString()

        updateRecyclerView()
        getListLSP()

    }

    fun getListLSP(){
        listLoaiSP.clear()
        val database = FirebaseDatabase.getInstance().getReference("LoaiSP")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listLoaiSP.clear()
                for (datasnap in snapshot.children){
                    val loaiSanPham = datasnap.getValue(LoaiSanPham::class.java)
                    if (loaiSanPham != null) {
                        listLoaiSP.add(loaiSanPham)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@OrderActivity,"Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateRecyclerView(){
        listLoaiSP = ArrayList<LoaiSanPham>()
        recyclerView = binding.menuRecyclerView
        adapter = AdapterMenu(this,listLoaiSP,MenuFragment(),1,maBan)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}