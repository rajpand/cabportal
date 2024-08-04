package com.varcassoftware.ridercabapp.activity.splashActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.activity.welcomeActivity.WelcomeActivity
import com.varcassoftware.ridercabapp.databinding.ActivitySplashBinding
import com.varcassoftware.ridercabapp.viewModelFactory.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel =
            ViewModelProvider(this, ViewModelFactory(""))[SplashViewModel::class.java]

        _init()
        observeSplashLiveData()
    }

    private fun _init() {
        viewModel.init();
    }

    private fun observeSplashLiveData() {
        viewModel.liveData.observe(this) {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}
