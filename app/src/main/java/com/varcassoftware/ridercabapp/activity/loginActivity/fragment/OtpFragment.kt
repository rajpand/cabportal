package com.varcassoftware.ridercabapp.activity.loginActivity.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.activity.loginActivity.UserAccountActivity
import com.varcassoftware.ridercabapp.activity.loginActivity.UserAccountViewModel
import com.varcassoftware.ridercabapp.databinding.FragmentOtpBinding
import com.varcassoftware.ridercabapp.viewModelFactory.ViewModelFactory

class OtpFragment : Fragment() {


    private lateinit var binding: FragmentOtpBinding
    private lateinit var userAccountViewModel: UserAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtpBinding.inflate(inflater, container, false)
        userAccountViewModel =
            ViewModelProvider(this, ViewModelFactory(""))[UserAccountViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val otpDigits =
            listOf(binding.otpDigit1, binding.otpDigit2, binding.otpDigit3, binding.otpDigit4)

        otpDigits.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && index < otpDigits.size - 1) {
                        otpDigits[index + 1].requestFocus()
                    } else if (s?.length == 0 && index > 0) {
                        otpDigits[index - 1].requestFocus()
                    }
                    val otp = otpDigits.joinToString("") { it.text.toString() }
                    userAccountViewModel.updateOtp(otp)
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }

        binding.verifyOtpButton.setOnClickListener {
            userAccountViewModel.verifyOtp()
        }

        userAccountViewModel.otpVerified.observe(viewLifecycleOwner) { isVerified ->
            if (isVerified) {
                userAccountViewModel.clearData()
                findNavController().navigate(R.id.action_otpFragment_to_createPinFragment)
            }
        }
    }
}