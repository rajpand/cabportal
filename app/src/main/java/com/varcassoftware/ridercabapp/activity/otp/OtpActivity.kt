package com.varcassoftware.ridercabapp.activity.otp

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.activity.login.LoginViewModel
import com.varcassoftware.ridercabapp.activity.map.MapActivity
import com.varcassoftware.ridercabapp.databinding.ActivityLoginBinding
import com.varcassoftware.ridercabapp.databinding.ActivityOtpBinding
import com.varcassoftware.ridercabapp.localstorage.LocalStorage
import com.varcassoftware.ridercabapp.repository.RepositoryClass
import com.varcassoftware.ridercabapp.viewModelFactory.ViewModelFactory

class OtpActivity : AppCompatActivity() {
    private var _binding: ActivityOtpBinding? = null
    private val binding get() = _binding!!

    private lateinit var otpViewModel: OtpViewModel
    private var localStorage: LocalStorage? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        otpViewModel = ViewModelProvider(
            this, ViewModelFactory("", RepositoryClass())
        )[OtpViewModel::class.java]
        binding.lifecycleOwner = this

        _init()
        onClickListeners()
        setObserve()
    }

    private fun setObserve() {
        otpViewModel.otpVerified.observe(this) { isVerified ->
            if (isVerified) {
                otpViewModel.clearData()
                val intent: Intent = Intent(this, MapActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                val options = ActivityOptions.makeCustomAnimation(
                    this, R.anim.activity_fade_in, R.anim.activity_fade_out)
                startActivity(intent, options.toBundle())
                startActivity(intent)
                finish()
            }
        }
    }

    private fun onClickListeners() {
        binding.verifyOtpButton.setOnClickListener {
            otpViewModel.verifyOtp()
        }
    }

    private fun _init() {
        val otpDigits =
            listOf(binding.otpDigit1, binding.otpDigit2, binding.otpDigit3, binding.otpDigit4)

        otpDigits.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && index < otpDigits.size - 1) {
                        otpDigits[index + 1].requestFocus()
                    } else if (s?.length == 0 && index > 0) {
                        otpDigits[index - 1].requestFocus()
                    }
                    val otp = otpDigits.joinToString("") { it.text.toString() }
                    otpViewModel.updateOtp(otp)
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }
}
