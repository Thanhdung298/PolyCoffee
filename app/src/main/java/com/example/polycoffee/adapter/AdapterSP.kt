package com.example.polycoffee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.polycoffee.dao.TempFunc
import com.example.polycoffee.databinding.ItemSpBinding
import com.example.polycoffee.model.SanPham

class AdapterSP(val context: Context, val list:ArrayList<SanPham>) : RecyclerView.Adapter<AdapterSP.ViewHolder>() {
    class ViewHolder(binding:ItemSpBinding) : RecyclerView.ViewHolder(binding.root) {
        val view = binding.itemSpView
        val tenSP = binding.itemSpTenSP
        val gia = binding.itemSpGia
        val img = binding.itemSpImg
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSpBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sanPham = list[position]
        holder.tenSP.text = sanPham.tenSP
        holder.gia.text = "${sanPham.giaSP} VND"
        if(sanPham.img!=""){
            holder.img.setImageBitmap(TempFunc.StringToBitmap(sanPham.img))
        }
    }

    override fun getItemCount(): Int = list.size
}