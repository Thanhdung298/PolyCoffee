package com.example.polycoffee.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.polycoffee.R
import com.example.polycoffee.databinding.FragmentMenuBinding
import com.example.polycoffee.databinding.FragmentThongKeBinding
import com.example.polycoffee.model.HoaDon
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat


class ThongKeFragment : Fragment() {

    private var _binding: FragmentThongKeBinding? = null
    private val binding get() = _binding!!
    val sdf = SimpleDateFormat("dd-MM-yyyy")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentThongKeBinding.inflate(inflater, container, false)

        val tuNgay = binding.thongkeTuNgay
        val denNgay = binding.thongkeDenNgay
        val calBtn = binding.thongkeResultBtn

        calBtn.setOnClickListener {
            val database = FirebaseDatabase.getInstance().getReference("HoaDon")
            database.get().addOnSuccessListener {
                val list = ArrayList<HoaDon>()
                for(snap in it.children){
                    val hoaDon = snap.getValue(HoaDon::class.java)
                    if (hoaDon != null) {
                        list.add(hoaDon)
                    }
                }
                list.sortBy {
                    it.ngay
                }

            }

        }


        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}