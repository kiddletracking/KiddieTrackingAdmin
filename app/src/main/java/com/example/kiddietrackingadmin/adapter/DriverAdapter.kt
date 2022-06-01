package com.example.kiddietrackingadmin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.kiddietrackingadmin.databinding.DriverItemViewBinding
import com.example.kiddietrackingadmin.models.Driver
import com.example.kiddietrackingadmin.ui.fragments.DriverListFragmentDirections
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class DriverAdapter(options: FirestoreRecyclerOptions<Driver>, private val view: View) :
    FirestoreRecyclerAdapter<Driver, DriverAdapter.DriverViewHolder>(options) {

    class DriverViewHolder(val binding: DriverItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        return DriverViewHolder(
            DriverItemViewBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int, model: Driver) {
        holder.binding.apply {
            driverName.text = model.driver_name
            driverNumber.text = model.driver_number
            driverCV.setOnClickListener {
                val action =
                    DriverListFragmentDirections.actionDriverListFragmentToDriverDetailsFragment(
                        model.driver_uid
                    )
                view.findNavController().navigate(action)
            }

        }
    }

}