package com.varcassoftware.ridercabapp.activity.loginActivity.registration

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.activity.otp.OtpActivity
import com.varcassoftware.ridercabapp.databinding.ActivityRegistrationBinding
import com.varcassoftware.ridercabapp.repository.RepositoryClass
import com.varcassoftware.ridercabapp.util.CustomProgressDialog
import com.varcassoftware.ridercabapp.utility.GlobalClasses
import com.varcassoftware.ridercabapp.viewModelFactory.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationActivity : AppCompatActivity() {
    private var _binding: ActivityRegistrationBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registrationViewModel = ViewModelProvider(
            this, ViewModelFactory("", RepositoryClass())
        )[RegistrationViewModel::class.java]
        binding.viewModel = registrationViewModel
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        setObserver()
        onClickListeners()
    }

    private fun setObserver() {
        registrationViewModel.isFormValid.observe(this) {
            if (it) {
                showProgressDialog()
                CoroutineScope(Dispatchers.IO).launch {
                    registrationViewModel.createCustomer(GlobalClasses.getDeviceId(this@RegistrationActivity))
                }
            }
        }

        registrationViewModel.customerCreated.observe(this) {
            hideProgressDialog()
            if (it != null) {
                if (it.equals("success", ignoreCase = true)) {
                    registrationViewModel.resetCustomerCreated()
                    gotoNext()
                } else {
                    // Handle the error case if needed
                    registrationViewModel.resetCustomerCreated()
                    gotoNext()
                }
            }
        }

        registrationViewModel.nameError.observe(this) {
            it?.let {
               // binding.name.error = it
                binding.name.requestFocus()
            }
        }

        registrationViewModel.emailError.observe(this) {
            it?.let {

                binding.email.requestFocus()
            }
        }

        registrationViewModel.mobileNumberError.observe(this) {
            it?.let {
                binding.mobilenumber.requestFocus()
            }
        }

        registrationViewModel.aadharNumberError.observe(this) {
            it?.let {
                binding.aadharNumber.requestFocus()
            }
        }

        registrationViewModel.passwordError.observe(this) {
            it?.let {
                binding.loginPassword.requestFocus()
            }
        }
    }

    private fun gotoNext() {
        val intent = Intent(this, OtpActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val options = ActivityOptions.makeCustomAnimation(
            this, R.anim.activity_fade_in, R.anim.activity_fade_out
        )
        startActivity(intent, options.toBundle())
        finish()
    }

    private fun onClickListeners() {
        binding.buttonSignUp.setOnClickListener {
            registrationViewModel.validateForm()
           // gotoNext()
        }
    }

    private fun showProgressBar() {
        runOnUiThread {
            binding.progressBackground.visibility = View.VISIBLE
            binding.progressBar.visibility = View.VISIBLE
            val fadeIn = AlphaAnimation(0f, 1f).apply {
                duration = 500
                fillAfter = true
            }
            binding.progressBackground.startAnimation(fadeIn)
            binding.progressBar.startAnimation(fadeIn)
        }
    }


    private fun hideProgressBar() {
        runOnUiThread {
            val fadeOut = AlphaAnimation(1f, 0f).apply {
                duration = 500
                fillAfter = true
            }
            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    binding.progressBackground.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
            binding.progressBackground.startAnimation(fadeOut)
            binding.progressBar.startAnimation(fadeOut)
        }
    }

    private fun showProgressDialog() {
        progressDialog.show()
    }

    private fun hideProgressDialog() {
        progressDialog.dismiss()
    }
}
