package com.example.polycoffee.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polycoffee.adapter.AdapterBan
import com.example.polycoffee.adapter.AdapterMenu
import com.example.polycoffee.dao.DAO
import com.example.polycoffee.dao.TempFunc
import com.example.polycoffee.databinding.DialogLoaispBinding
import com.example.polycoffee.databinding.FragmentOrderBinding
import com.example.polycoffee.model.Ban
import com.example.polycoffee.model.LoaiSanPham
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.theartofdev.edmodo.cropper.CropImage

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var recyclerView: RecyclerView
    lateinit var adapterBan: AdapterBan
    lateinit var listBan:ArrayList<Ban>
    lateinit var username:String
     var role = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        username = requireActivity().intent.getStringExtra("Username").toString()
        role = requireActivity().intent.getIntExtra("role",0)

        binding.orderFab.setOnClickListener {
            DAO(requireContext()).insert(Ban((listBan.size+1).toString()),"Ban")
        }

        updateRecyclerView()
        getListLSP()

        return binding.root
    }

    fun updateRecyclerView(){
        listBan = ArrayList()
        recyclerView = binding.orderRecyclerView
        adapterBan = AdapterBan(requireContext(),listBan,this,username,role)
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        recyclerView.adapter = adapterBan
    }

    fun getListLSP(){
        val database = FirebaseDatabase.getInstance().getReference("Ban")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listBan.clear()
                for (datasnap in snapshot.children){
                    val ban = datasnap.getValue(Ban::class.java)
                    if (ban != null) {
                        listBan.add(ban)
                    }
                }
                adapterBan.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"Failed", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}