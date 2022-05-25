package com.example.kiddietrackingadmin.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.kiddietrackingadmin.daos.DriverDao
import com.example.kiddietrackingadmin.databinding.FragmentRegisterBusBinding
import com.example.kiddietrackingadmin.models.Driver
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterBusFragment : Fragment() {

    private var _binding: FragmentRegisterBusBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBusBinding.inflate(layoutInflater, container, false)

        binding.registerBusButton.setOnClickListener {
            checkValues()
        }

        return binding.root
    }

    private fun checkValues() {
        if (binding.driverName.text.toString() != ""
            && binding.email.text.toString() != "" &&
            binding.password.text.toString() != "" &&
            binding.driverNumber.text.toString() != "" &&
            binding.busNumber.text.toString() != ""
        ) {
            createAccount()
            binding.loadingView.visibility = View.VISIBLE
            binding.registerBusButton.visibility = View.GONE
        }
    }

    private fun createAccount() {

        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result!!.user!!
                    CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
                        val newDriver = Driver(
                            driver_uid = firebaseUser.uid,
                            driver_name = binding.driverName.text.toString(),
                            driver_number = binding.driverNumber.text.toString(),
                            bus_number = binding.busNumber.text.toString()
                        )
                        DriverDao().driverCollection.document(firebaseUser.uid).set(newDriver)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    requireContext(), "Bus Added successful", Toast.LENGTH_SHORT
                                ).show()
                                binding.registerBusButton.visibility = View.VISIBLE
                                binding.loadingView.visibility = View.GONE
                                findNavController().navigateUp()
                            }.addOnFailureListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Registration failed please try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.registerBusButton.visibility = View.VISIBLE
                                binding.loadingView.visibility = View.GONE
                            }
                    }

                } else {
                    Toast.makeText(
                        requireContext(), "Registration failed due to " +
                                task.exception.toString(), Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}