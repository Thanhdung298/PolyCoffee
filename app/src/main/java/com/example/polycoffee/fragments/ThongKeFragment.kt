package com.example.polycoffee.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.polycoffee.R
import com.example.polycoffee.adapter.AdapterThongKe
import com.example.polycoffee.databinding.FragmentMenuBinding
import com.example.polycoffee.databinding.FragmentThongKeBinding
import com.example.polycoffee.model.HoaDon
import com.example.polycoffee.model.HoaDonTemp
import com.google.android.material.textfield.TextInputLayout
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
        tuNgay.editText!!.setText(sdf.format(cal.time))
        denNgay.editText!!.setText(sdf.format(cal.time))

        chooseDate(tuNgay)
        chooseDate(denNgay)



        calBtn.setOnClickListener {
            val database = FirebaseDatabase.getInstance().getReference("HoaDon")
            database.get().addOnSuccessListener {
                val list = ArrayList<HoaDon>()
                for(snap in it.children){
                    val hoaDon = snap.getValue(HoaDon::class.java)
                    if (hoaDon != null) {
                        Log.d("test",hoaDon.ngay)
                        if (parseDate(hoaDon.ngay)!!.compareTo(parseDate(tuNgay.editText!!.text.toString()))>=0 && parseDate(hoaDon.ngay)!!.compareTo(parseDate(denNgay.editText!!.text.toString())) <= 0 ){
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
                val adapterThongKe = AdapterThongKe(requireContext(),list)
                binding.thongkeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.thongkeRecyclerView.adapter = adapterThongKe
            }
        }
        calBtn.performClick()


        return binding.root
    }

    fun chooseDate(textInputLayout: TextInputLayout){
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->
            cal.set(Calendar.YEAR,i)
            cal.set(Calendar.MONTH,i2)
            cal.set(Calendar.DAY_OF_MONTH,i3)
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            textInputLayout.editText!!.setText(sdf.format(cal.time))
        }

        textInputLayout.editText!!.setOnFocusChangeListener{ _, b ->
            if(b){
                DatePickerDialog(requireContext(),dateSetListener,cal.get(Calendar.YEAR),cal.get(
                    Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
                textInputLayout.editText!!.setOnClickListener{
                    DatePickerDialog(requireContext(),dateSetListener,cal.get(Calendar.YEAR),cal.get(
                        Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
                }
            }
        }
    }

    fun parseDate(date:String): Date? {
        return sdf.parse(date)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}