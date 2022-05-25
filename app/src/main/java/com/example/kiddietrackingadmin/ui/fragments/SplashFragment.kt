package com.example.kiddietrackingadmin.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.kiddietrackingadmin.R
import com.example.kiddietrackingadmin.databinding.FragmentSplashBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null

    private val binding get() = _binding!!

    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(layoutInflater, container, false)

        CoroutineScope(Dispatchers.IO).launch {
            timer()
        }

        return binding.root
    }

    private suspend fun timer() {
        withContext(Dispatchers.Main) {
            binding.logo.animate().alpha(1f).duration = 700
        }
        delay(1000)
        withContext(Dispatchers.Main) {
            binding.logo.animate().alpha(0f).duration = 700
        }
        delay(1000)
        withContext(Dispatchers.Main) {
            if (auth.currentUser == null) {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            }
        }
    }

}