package com.varcassoftware.ridercabapp.activity.loginActivity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.activity.loginActivity.UserAccountViewModel
import com.varcassoftware.ridercabapp.databinding.FragmentRegistrationBinding
import com.varcassoftware.ridercabapp.viewModelFactory.ViewModelFactory


class RegistrationFragment : Fragment() {


    private var _binding: FragmentRegistrationBinding? = null
    private lateinit var viewModel: UserAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(""))[UserAccountViewModel::class.java]
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Observe the ViewModel data
        setOnClickListeners()
        setObserver()

    }
    private fun setOnClickListeners() {
        _binding?.buttonSignUp?.setOnClickListener {
            viewModel.clearData()
            findNavController().navigate(R.id.action_registrationFragment_to_otpFragment)
        }
    }

    private fun setObserver() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}