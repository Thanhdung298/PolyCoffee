package com.example.polycoffee.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.polycoffee.databinding.FragmentDoipassBinding


class DoiPassFragment : Fragment() {
    private var _binding: FragmentDoipassBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDoipassBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val passOld = binding.edPassOld
        val passNew = binding.edPassNew
        val passNew2 = binding.edPassNewAgain
        binding.btnSavePass.setOnClickListener {  }
        binding.btnCancelPass.setOnClickListener {  }
        return root
    }

    private fun valiDateFrom(){}


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}