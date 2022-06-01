package com.example.kiddietrackingadmin.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kiddietrackingadmin.adapter.DriverAdapter
import com.example.kiddietrackingadmin.daos.DriverDao
import com.example.kiddietrackingadmin.databinding.FragmentDriverListBinding
import com.example.kiddietrackingadmin.models.Driver
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class DriverListFragment : Fragment() {

    private var _binding: FragmentDriverListBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: DriverAdapter

    private val driverDao = DriverDao()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDriverListBinding.inflate(layoutInflater, container, false)

        setAdapter()

        return binding.root
    }

    private fun setAdapter() {
        val query = driverDao.driverCollection

        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<Driver>().setQuery(query, Driver::class.java)
                .build()

        adapter = DriverAdapter(recyclerViewOptions, binding.root)

        binding.rvChildren.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}