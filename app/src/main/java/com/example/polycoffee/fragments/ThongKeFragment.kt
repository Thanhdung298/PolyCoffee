package com.example.polycoffee.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.polycoffee.R
import com.example.polycoffee.databinding.FragmentMenuBinding
import com.example.polycoffee.databinding.FragmentThongKeBinding


class ThongKeFragment : Fragment() {

    private var _binding: FragmentThongKeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentThongKeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}