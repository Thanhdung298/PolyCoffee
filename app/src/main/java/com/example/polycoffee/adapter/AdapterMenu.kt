package com.example.polycoffee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.polycoffee.dao.TempFunc
import com.example.polycoffee.databinding.ItemMenuBinding
import com.example.polycoffee.fragments.MenuFragment
import com.example.polycoffee.model.LoaiSanPham

class AdapterMenu(val context: Context,val list:ArrayList<LoaiSanPham>,val fragment:MenuFragment) : RecyclerView.Adapter<AdapterMenu.ViewHolder>() {
    class ViewHolder(binding:ItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        val tenLoai = binding.itemLoaiSPTenLoai
        val img = binding.itemLoaiSPImg
        val view = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val loaiSanPham = list[position]
        if(loaiSanPham.img!="") {
            holder.img.setImageBitmap(TempFunc.StringToBitmap(loaiSanPham.img))
        }
        holder.tenLoai.text = loaiSanPham.tenLoai
        holder.view.setOnLongClickListener(object :View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                TempFunc.choosenDialog(context,loaiSanPham,fragment)
                return false
            }

        })

    }

    override fun getItemCount(): Int = list.size
}