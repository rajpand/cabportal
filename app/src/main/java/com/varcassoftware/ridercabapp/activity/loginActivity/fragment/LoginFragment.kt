package com.varcassoftware.ridercabapp.activity.loginActivity.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.activity.loginActivity.UserAccountViewModel
import com.varcassoftware.ridercabapp.activity.map.MapActivity
import com.varcassoftware.ridercabapp.databinding.FragmentLoginBinding
import com.varcassoftware.ridercabapp.databinding.FragmentRegistrationBinding
import com.varcassoftware.ridercabapp.viewModelFactory.ViewModelFactory

class LoginFragment : Fragment() {


    private var _binding: FragmentLoginBinding? = null
    private lateinit var viewModel: UserAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(""))[UserAccountViewModel::class.java]
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the ViewModel data
        setOnClickListeners()
        setObserver()

    }

    private fun setOnClickListeners() {
        _binding?.loginButton?.setOnClickListener {
            val intent = Intent(activity, MapActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setObserver() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}