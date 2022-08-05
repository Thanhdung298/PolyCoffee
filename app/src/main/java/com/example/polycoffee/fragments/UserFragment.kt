package com.example.polycoffee.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polycoffee.adapter.AdapterUser
import com.example.polycoffee.dao.DAO
import com.example.polycoffee.dao.TempFunc
import com.example.polycoffee.databinding.DialogUserBinding
import com.example.polycoffee.databinding.FragmentUserBinding
import com.example.polycoffee.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var adapter:AdapterUser
    lateinit var recyclerView: RecyclerView
    lateinit var listUser:ArrayList<User>
    var bitmapImg:Bitmap?=null
    lateinit var img:ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fabUser.setOnClickListener {
            openDialog(User(),0)
        }
        updateRecyclerView()
        getListLSP()
        return root
    }

    fun updateRecyclerView(){
        listUser = ArrayList()
        recyclerView = binding.reyclerViewUser
        adapter = AdapterUser(requireContext(),listUser,this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
    fun getListLSP(){
        val database = FirebaseDatabase.getInstance().getReference("User")
        database.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listUser.clear()
                for (datasnap in snapshot.children){
                    val user = datasnap.getValue(User::class.java)
                    if (user != null) {
                        listUser.add(user)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"Failed",Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun openDialog(user: User,type:Int){
        val builder = AlertDialog.Builder(requireContext())
        val binding = DialogUserBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        val alertDialog = builder.create()
        alertDialog.show()
        bitmapImg = null
        binding.root.background.alpha = 100

        img = binding.dialogUserImg
        val username = binding.dialogUserUsername
        val password = binding.dialogUserPassword
        val hoten = binding.dialogUserHoTen
        val ngaySinh = binding.dialogUserNgaySinh
        val diaChi = binding.dialogUserDiaChi
        val sdt = binding.dialogUserSDT
        val saveBtn = binding.dialogUserBtnSave
        val cancelBtn = binding.dialogUserBtnCancel
        val rdo1 = binding.dialogUserRdoAdmin
        val rdo0 = binding.dialogUserRdoNhanVien

        if(type==1){
            username.editText!!.setText(user.userName)
            username.editText!!.isEnabled = false
            password.editText!!.setText(user.passWord)
            hoten.editText!!.setText(user.hoTen)
            ngaySinh.editText!!.setText(user.ngaySinh)
            diaChi.editText!!.setText(user.diaChi)
            sdt.editText!!.setText(user.sdt)
            if(user.anhDaiDien!=""){
                img.setImageBitmap(TempFunc.StringToBitmap(user.anhDaiDien))
                bitmapImg = TempFunc.StringToBitmap(user.anhDaiDien)
            }
            if(user.role==0){
                rdo0.isChecked = true
            } else rdo1.isChecked = true
        }

        img.setOnClickListener {
            CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).setAspectRatio(1,1).start(requireContext(),this)
        }
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->
            cal.set(Calendar.YEAR,i)
            cal.set(Calendar.MONTH,i2)
            cal.set(Calendar.DAY_OF_MONTH,i3)
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            ngaySinh.editText!!.setText(sdf.format(cal.time))
        }

        ngaySinh.editText!!.setOnFocusChangeListener{ _, b ->
            if(b){
                DatePickerDialog(requireContext(),dateSetListener,cal.get(Calendar.YEAR),cal.get(
                    Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
                ngaySinh.editText!!.setOnClickListener{
                    DatePickerDialog(requireContext(),dateSetListener,cal.get(Calendar.YEAR),cal.get(
                        Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
                }
            }
        }

        saveBtn.setOnClickListener {
            TempFunc.checkField(username,password,hoten,ngaySinh,diaChi,sdt)
//                if(username.editText!!.text.toString().length<6){
//                    username.error = "Tên đăng nhập phải ít nhất 6 ký tự"
//                } else username.error = null
            val regexUsername = "^[[A-Z]|[a-z]][[A-Z]|[a-z]|\\d|[_]]{6,25}$".toRegex()
                if(!regexUsername.matches(username.editText!!.text.toString())){
                    username.error = "Tên đăng nhập không hợp lệ"
                    Toast.makeText(requireContext(),"Tên đăng nhập phải từ 6 đến 25 ký tự và không chứa kí tự đặc biệt",Toast.LENGTH_LONG).show()
                } else username.error = null
                if(password.editText!!.text.toString().length<6 || password.editText!!.text.toString().length>30){
                    password.error = "Mật khẩu phải từ 6 đến 30 ký tự"
                } else password.error = null

            val regexPhone = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}\$".toRegex()
                if(!regexPhone.matches(sdt.editText!!.text.toString())){
                    sdt.error = "Sai định dạng số điện thoại"
                } else{
                    sdt.error = null
                }


                if(rdo0.isChecked || rdo1.isChecked){
                    if(TempFunc.noError(username,password,hoten,ngaySinh,diaChi,sdt)){
                        val userAdd = User(username.editText!!.text.toString(),password.editText!!.text.toString(),hoten.editText!!.text.toString(),ngaySinh.editText!!.text.toString(),diaChi.editText!!.text.toString(),sdt.editText!!.text.toString(),if(bitmapImg == null)"" else TempFunc.BitMapToString(bitmapImg!!),if(rdo0.isChecked)0 else 1)
                        DAO(requireContext()).insert(userAdd,"User")
                        alertDialog.dismiss()
                    }
                } else{
                    Toast.makeText(requireContext(),"Bạn chưa chọn chức vụ",Toast.LENGTH_SHORT).show()
                }


        }
        cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val result = CropImage.getActivityResult(data)
            if(resultCode == Activity.RESULT_OK){
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver,result.uri)
                bitmapImg = bitmap
                img.setImageBitmap(bitmapImg)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                val error = result.error
                Log.d("error",error.toString())
            }
        }
    }
}