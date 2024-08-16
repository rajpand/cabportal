package com.varcassoftware.ridercabapp.activity.login

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.activity.createpin.CreatePinActivity
import com.varcassoftware.ridercabapp.activity.loginActivity.registration.RegistrationActivity
import com.varcassoftware.ridercabapp.activity.map.MapActivity
import com.varcassoftware.ridercabapp.databinding.ActivityLoginBinding
import com.varcassoftware.ridercabapp.entity.UserLogin
import com.varcassoftware.ridercabapp.localstorage.LocalStorage
import com.varcassoftware.ridercabapp.repository.RepositoryClass
import com.varcassoftware.ridercabapp.utility.SharedPreferencesKeys
import com.varcassoftware.ridercabapp.viewModelFactory.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel
    private var localStorage: LocalStorage? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel = ViewModelProvider(
            this, ViewModelFactory("", RepositoryClass())
        )[LoginViewModel::class.java]
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = this

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        _init()
        onClickListeners()
        setObserve()

    }

    private fun _init() {
        localStorage = LocalStorage(this@LoginActivity)
    }

    private fun onClickListeners() {
        binding.loginButton.setOnClickListener {
            loginViewModel.validateCredentials()
        }


        binding.forgotPassword.setOnClickListener {
            val intent = Intent(
                this, CreatePinActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val options = ActivityOptions.makeCustomAnimation(
                this, R.anim.activity_fade_in, R.anim.activity_fade_out
            )
            startActivity(intent, options.toBundle())
        }

        binding.signUpRedirectText.setOnClickListener {
            val intent = Intent(
                this, RegistrationActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val options = ActivityOptions.makeCustomAnimation(
                this, R.anim.activity_fade_in, R.anim.activity_fade_out
            )
            startActivity(intent, options.toBundle())
        }

    }

    private fun setObserve() {
        loginViewModel.loginResult.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                CoroutineScope(Dispatchers.IO).launch {
                    loginViewModel.saveData(UserLogin(binding.loginEmail.text.toString(),
                        binding.loginEmail.text.toString()))
                }
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        })

        loginViewModel.user.observe(this, Observer {
            localStorage?.saveBoolean(SharedPreferencesKeys.loginScreenStatus, true);
            val intent = Intent(this, MapActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val options = ActivityOptions.makeCustomAnimation(
                this, R.anim.activity_fade_in, R.anim.activity_fade_out
            )
            startActivity(intent, options.toBundle())
            finish()
        })
        loginViewModel.error.observe(this, Observer { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        })
    }
}