package com.example.kiddietrackingadmin.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.kiddietrackingadmin.R
import com.example.kiddietrackingadmin.databinding.FragmentLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        binding.btnLogin.setOnClickListener {
            signInWithEmail()
        }

        return binding.root
    }

    private fun signInWithEmail() {
        if (binding.email.text.toString() != "" && binding.password.text.toString() != "") {
            binding.loadingView.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.GONE
            auth.signInWithEmailAndPassword(
                binding.email.text.toString(),
                binding.password.text.toString()
            ).addOnCompleteListener(requireActivity()){task ->
                if (task.isSuccessful) {
                    binding.loadingView.visibility = View.GONE
                    binding.btnLogin.visibility = View.VISIBLE
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    binding.loadingView.visibility = View.GONE
                    binding.btnLogin.visibility = View.VISIBLE
                    Toast.makeText(
                        requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }else{
            Toast.makeText(
                requireContext(), "Please enter the details before LOGIN",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}