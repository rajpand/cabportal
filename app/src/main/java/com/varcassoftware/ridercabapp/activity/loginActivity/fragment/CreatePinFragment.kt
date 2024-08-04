package com.varcassoftware.ridercabapp.activity.loginActivity.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.varcassoftware.ridercabapp.activity.loginActivity.LoginActivity
import com.varcassoftware.ridercabapp.activity.loginActivity.UserAccountViewModel
import com.varcassoftware.ridercabapp.databinding.FragmentCreatePinBinding
import com.varcassoftware.ridercabapp.viewModelFactory.ViewModelFactory


class CreatePinFragment : Fragment() {


    private var _binding: FragmentCreatePinBinding? = null
    private lateinit var viewModel: UserAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(""))[UserAccountViewModel::class.java]
        _binding = FragmentCreatePinBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the ViewModel data
        setObserver()

    }

    private fun setObserver() {
        _binding?.buttonSignUp?.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(intent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}