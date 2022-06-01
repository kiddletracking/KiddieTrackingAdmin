package com.example.kiddietrackingadmin.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.kiddietrackingadmin.R
import com.example.kiddietrackingadmin.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        binding.registerChild.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_registerChildFragment)
        }

        binding.registerBus.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_registerBusFragment)
        }

        binding.editDetails.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_driverListFragment)
        }

        return binding.root
    }
}