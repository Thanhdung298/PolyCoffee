package com.example.polycoffee.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.polycoffee.ChangePasswordActivity
import com.example.polycoffee.LoginActivity
import com.example.polycoffee.R
import com.example.polycoffee.adapter.AdapterUser
import com.example.polycoffee.dao.DAO
import com.example.polycoffee.dao.TempFunc
import com.example.polycoffee.databinding.DialogProfileBinding
import com.example.polycoffee.databinding.DialogUserBinding
import com.example.polycoffee.databinding.FragmentProfileBinding
import com.example.polycoffee.databinding.FragmentThongKeBinding
import com.example.polycoffee.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.theartofdev.edmodo.cropper.CropImage

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: AdapterUser
    lateinit var img: ImageView

    var bitmapImg: Bitmap?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var user = User()
        val username = requireActivity().intent.getStringExtra("Username").toString()
        val database = FirebaseDatabase.getInstance().getReference("User").child(username)
        database.get().addOnSuccessListener {
            user = it.getValue(User::class.java)!!
            binding.tvProfileUsername.text = user.userName
            binding.tvRole.text = if(user.role==0)"Nhân viên" else "Quản lý"
            binding.tvProfileHoten.text = "Họ tên: ${user.hoTen}"
            binding.tvProfileDiachi.text = "Địa chỉ: ${user.diaChi}"
            binding.tvProfileNgaysinh.text = "Ngày sinh: ${user.ngaySinh}"
            binding.tvProfileSdt.text = "Số điện thoại: ${user.sdt}"
            if(user.anhDaiDien!=""){
                binding.imgProfile.setImageBitmap(TempFunc.StringToBitmap(user.anhDaiDien))
            }
        }.addOnFailureListener { Toast.makeText(requireContext(),"failed",Toast.LENGTH_SHORT).show() }



        binding.btnProfileDMK.setOnClickListener {
            startActivity(Intent(requireActivity(),ChangePasswordActivity::class.java))
        }
        binding.btnProfileEdit.setOnClickListener {
            openDialog(user)
        }

        return root
    }
    fun openDialog(user: User){
        val builder = AlertDialog.Builder(requireContext())
        val binding = DialogProfileBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        val alertDialog = builder.create()
        alertDialog.show()

        img = binding.dialogProfileImg
        val username = binding.dialogProfileUsername
        val hoten = binding.dialogProfileHoTen
        val ngaySinh = binding.dialogProfileNgaySinh
        val diaChi = binding.dialogProfileDiaChi
        val sdt = binding.dialogProfileSDT
        val saveBtn = binding.dialogProfileBtnSave
        val cancelBtn = binding.dialogProfileBtnCancel

            username.editText!!.setText(user.userName)
            username.editText!!.isEnabled = false
            hoten.editText!!.setText(user.hoTen)
            ngaySinh.editText!!.setText(user.ngaySinh)
            diaChi.editText!!.setText(user.diaChi)
            sdt.editText!!.setText(user.sdt)
            if(user.anhDaiDien!=""){
                img.setImageBitmap(TempFunc.StringToBitmap(user.anhDaiDien))
                bitmapImg = TempFunc.StringToBitmap(user.anhDaiDien)
            }

        img.setOnClickListener {
            CropImage.activity().setAspectRatio(1,1).start(requireContext(),this)
        }

        saveBtn.setOnClickListener {
            user.hoTen = hoten.editText!!.text.toString()
            user.ngaySinh = ngaySinh.editText!!.text.toString()
            user.diaChi = diaChi.editText!!.text.toString()
            user.sdt = sdt.editText!!.text.toString()
            DAO(requireContext()).insert(user,"User")
            alertDialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }


    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}