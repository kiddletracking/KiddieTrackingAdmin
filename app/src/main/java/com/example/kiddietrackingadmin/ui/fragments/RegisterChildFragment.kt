package com.example.kiddietrackingadmin.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.kiddietrackingadmin.R
import com.example.kiddietrackingadmin.daos.DriverDao
import com.example.kiddietrackingadmin.daos.UserDao
import com.example.kiddietrackingadmin.databinding.FragmentRegisterChildBinding
import com.example.kiddietrackingadmin.models.Driver
import com.example.kiddietrackingadmin.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterChildFragment : Fragment() {

    private var _binding: FragmentRegisterChildBinding? = null

    private val binding get() = _binding!!

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private var childUri: Uri? = null

    private var storageRef = Firebase.storage.reference

    private val userDao = UserDao()

    private var drivers = mutableListOf<String>()

    private var driverList = mutableListOf<Driver>()

    private var driverName = ""

    private var driverUid = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterChildBinding.inflate(layoutInflater, container, false)

        setChildImage()

        binding.driverSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val name = p0?.getItemAtPosition(p2).toString()
                    driverName = name
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        DriverDao().driverCollection.get().addOnSuccessListener {
            val driver = it.toObjects(Driver::class.java)
            setSpinner(driver)
            driverList = driver
        }

        binding.createChildButton.setOnClickListener {
            uploadChildImage()
        }

        return binding.root
    }

    private fun setSpinner(it: List<Driver>?) {
        it?.forEach {
            it.driver_name?.let { it1 -> drivers.add(it1) }
        }

        val arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
            requireContext(),
            R.layout.spinner_list,
            drivers as List<Any?>
        )

        arrayAdapter.setDropDownViewResource(R.layout.spinner_list)

        binding.driverSpinner.adapter = arrayAdapter
    }

    private fun uploadChildImage() {

        if (childUri != null && binding.parentName.text.toString() != ""
            && binding.email.text.toString() != "" &&
            binding.password.text.toString() != "" &&
            binding.childName.text.toString() != "" &&
            binding.parentsContact.text.toString() != "" &&
            binding.childClass.text.toString() != ""
        ) {
            binding.loadingView.visibility = View.VISIBLE
            binding.createChildButton.visibility = View.GONE
            val imageRef = storageRef
                .child("Child/ + ${childUri!!.lastPathSegment}")
            val uploadTask = imageRef.putFile(childUri!!)
            uploadTask.addOnSuccessListener {
                val url = imageRef.downloadUrl
                url.addOnSuccessListener {
                    createAccount(it)
                }
            }
            uploadTask.addOnFailureListener {
                binding.loadingView.visibility = View.GONE
                binding.createChildButton.visibility = View.VISIBLE
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(
                requireContext(), "Please enter all information properly", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setChildImage() {

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data.let {
                        childUri = it
                        binding.childImage.setImageURI(it)
                    }
                }
            }

        binding.childImage.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                resultLauncher.launch(it)
            }
        }
    }


    private fun createAccount(uri: Uri) {

        driverList.forEach {
            if (driverName == it.driver_name) {
                driverUid = it.driver_uid
            }
        }

        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result!!.user!!
                    CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
                        val newChild = User(
                            uid = firebaseUser.uid,
                            name = binding.childName.text.toString(),
                            image = uri.toString(),
                            classStd = binding.childClass.text.toString(),
                            parentName = binding.parentName.text.toString(),
                            parentContact = binding.parentsContact.text.toString(),
                            driverUid = driverUid
                        )
                        userDao.userCollection.document(firebaseUser.uid).set(newChild)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Child Register successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.loadingView.visibility = View.GONE
                                findNavController().navigateUp()
                            }.addOnFailureListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Registration failed please try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.createChildButton.visibility = View.VISIBLE
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