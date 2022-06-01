package com.example.kiddietrackingadmin.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kiddietrackingadmin.daos.DriverDao
import com.example.kiddietrackingadmin.databinding.FragmentDriverDetailsBinding
import com.example.kiddietrackingadmin.models.Driver

class DriverDetailsFragment : Fragment() {

    private var _binding: FragmentDriverDetailsBinding? = null

    private val args: DriverDetailsFragmentArgs by navArgs()

    private val binding get() = _binding!!

    private val driverDao = DriverDao()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDriverDetailsBinding.inflate(layoutInflater, container, false)

        driverDao.driverCollection.document(args.driverId)
            .get().addOnSuccessListener {
                updateUi((it.toObject(Driver::class.java)))
            }
        binding.saveDriverDetailsButton.setOnClickListener {
            if (binding.driverName.text.toString() != ""
                && binding.driverContentNumber.text.toString() != ""
            ) {
                updateDriverDetails()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please Edit all details before saving",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return binding.root
    }

    private fun updateDriverDetails() {
        val driver =
            Driver(binding.driverName.text.toString(), binding.driverContentNumber.text.toString())
        driverDao.driverCollection.document(args.driverId).set(driver)
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "Driver Details Edited Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigateUp()
            }
    }

    private fun updateUi(driverDetails: Driver?) {
        if (driverDetails != null) {
            binding.driverName.setText(driverDetails.driver_name)
            binding.driverContentNumber.setText(driverDetails.driver_number)
        }
    }
}