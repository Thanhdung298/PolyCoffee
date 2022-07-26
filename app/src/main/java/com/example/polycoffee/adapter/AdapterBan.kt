package com.example.polycoffee.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.polycoffee.OrderActivity
import com.example.polycoffee.databinding.ItemBanBinding
import com.example.polycoffee.fragments.OrderFragment
import com.example.polycoffee.model.Ban

class AdapterBan(val context: Context, val list:ArrayList<Ban>, val fragment:OrderFragment) : RecyclerView.Adapter<AdapterBan.ViewHolder>() {
    class ViewHolder(binding:ItemBanBinding) : RecyclerView.ViewHolder(binding.root) {
        val view = binding.itemBanView
        val menu = binding.itemBanMenu
        val hoaDon = binding.itemBanHoaDon
        val id = binding.itemBanId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBanBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.menu.setOnClickListener {
            context.startActivity(Intent(context,OrderActivity::class.java))
        }

        holder.hoaDon.setOnClickListener {

        }

        holder.id.text = "Bàn ${list[position].maBan.toInt()+1}"
    }

    override fun getItemCount(): Int = list.size
}