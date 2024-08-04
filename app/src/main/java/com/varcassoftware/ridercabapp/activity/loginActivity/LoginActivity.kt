package com.varcassoftware.ridercabapp.activity.loginActivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.activity.map.MapActivity
import com.varcassoftware.ridercabapp.activity.splashActivity.SplashViewModel
import com.varcassoftware.ridercabapp.activity.welcomeActivity.WelcomeActivity
import com.varcassoftware.ridercabapp.databinding.ActivityLoginBinding
import com.varcassoftware.ridercabapp.databinding.ActivitySplashBinding
import com.varcassoftware.ridercabapp.viewModelFactory.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userAccountViewModel: UserAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userAccountViewModel =
            ViewModelProvider(this, ViewModelFactory(""))[UserAccountViewModel::class.java]

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        onClickListeners()
        setObserve()

    }

    private fun onClickListeners() {
        binding.loginButton.setOnClickListener {
            val intent =
                Intent(this, MapActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }

    private fun setObserve() {
        userAccountViewModel.forLogin.observe(this) {

        }
    }


}