package com.example.polycoffee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.polycoffee.databinding.ItemBanBinding
import com.example.polycoffee.fragments.OrderFragment
import com.example.polycoffee.model.Ban

class AdapterBan(val context: Context, val list:ArrayList<Ban>, val fragment:OrderFragment) : RecyclerView.Adapter<AdapterBan.ViewHolder>() {
    class ViewHolder(binding:ItemBanBinding) : RecyclerView.ViewHolder(binding.root) {
        val view = binding.itemBanView
        val menu = binding.itemBanMenu
        val hoaDon = binding.itemBanHoaDon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBanBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.menu.setOnClickListener {

        }

        holder.hoaDon.setOnClickListener {

        }
    }

    override fun getItemCount(): Int = list.size
}