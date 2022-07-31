package com.example.polycoffee.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.polycoffee.R
import com.example.polycoffee.databinding.FragmentMenuBinding
import com.example.polycoffee.databinding.FragmentThongKeBinding
import com.example.polycoffee.model.HoaDon
import com.example.polycoffee.model.HoaDonTemp
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ThongKeFragment : Fragment() {

    private var _binding: FragmentThongKeBinding? = null
    private val binding get() = _binding!!
    val sdf = SimpleDateFormat("dd-MM-yyyy")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentThongKeBinding.inflate(inflater, container, false)

        val tuNgay = binding.thongkeTuNgay
        val denNgay = binding.thongkeDenNgay
        val calBtn = binding.thongkeResultBtn

        val cal = Calendar.getInstance()
        tuNgay.setText(sdf.format(cal.time))
        denNgay.setText(sdf.format(cal.time))


        calBtn.setOnClickListener {
            val database = FirebaseDatabase.getInstance().getReference("HoaDon")
            database.get().addOnSuccessListener {
                val list = ArrayList<HoaDon>()
                for(snap in it.children){
                    val hoaDon = snap.getValue(HoaDon::class.java)
                    if (hoaDon != null) {
                        Log.d("test",hoaDon.ngay)
                        if (parseDate(hoaDon.ngay)!!.compareTo(parseDate(tuNgay.text.toString()))>=0 && parseDate(hoaDon.ngay)!!.compareTo(parseDate(denNgay.text.toString())) <= 0 ){
                            list.add(hoaDon)
                        }
                    }
                }
                list.sortBy {
                    it.ngay
                }

                var sum = 0
                list.forEach {
                    sum += it.listSP.fold(0) { acc: Int, hoaDonTemp: HoaDonTemp ->
                        acc + hoaDonTemp.donGia * hoaDonTemp.soLuong
                    }
                }
                binding.thongkeTongLoiNhuan.text = "Tổng lợi nhuận: ${sum}"

            }
        }
        calBtn.performClick()


        return binding.root
    }

    fun parseDate(date:String): Date? {
        return sdf.parse(date)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}