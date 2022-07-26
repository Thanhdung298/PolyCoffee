package com.example.polycoffee.fragments

import android.app.AlertDialog
import android.os.Bundle
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)

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
        adapterBan = AdapterBan(requireContext(),listBan,this)
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

//    fun openDialogLSP(ban: Ban, type:Int){
//        val builder = AlertDialog.Builder(requireContext())
//        val binding = DialogLoaispBinding.inflate(layoutInflater)
//        builder.setView(binding.root)
//        val alertDialog = builder.create()
//        alertDialog.show()
//        val maLoai = binding.dialogLoaiSPMaLoai
//        val tenLoai = binding.dialogLoaiSPTenLoai
//        val saveBtn = binding.dialogLoaiSPSaveBtn
//        val cancelBtn = binding.btnLSPCancel
//        img = binding.dialogLoaiSPImg
//
//        if(type==1){
//            maLoai.editText!!.isEnabled = false
//            maLoai.editText!!.setText(loaiSanPham.maLoai)
//            tenLoai.editText!!.setText(loaiSanPham.tenLoai)
//            img.setImageBitmap(TempFunc.StringToBitmap(loaiSanPham.img))
//            bitmapLoaiSP = TempFunc.StringToBitmap(loaiSanPham.img)
//        }
//
//        img.setOnClickListener {
//            CropImage.activity().setAspectRatio(27,6).start(requireContext(),this)
//        }
//
//        saveBtn.setOnClickListener {
//            val loaiSP = LoaiSanPham(maLoai.editText!!.text.toString(),tenLoai.editText!!.text.toString(),if(bitmapLoaiSP == null)"" else TempFunc.BitMapToString(bitmapLoaiSP!!))
//            DAO(requireContext()).insert(loaiSP,"LoaiSP")
//            alertDialog.dismiss()
//        }
//        cancelBtn.setOnClickListener{
//            alertDialog.dismiss()
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}