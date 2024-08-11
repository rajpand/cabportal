package com.varcassoftware.ridercabapp.activity.splashActivity

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.activity.serviceactivity.ServiceActivity
import com.varcassoftware.ridercabapp.activity.welcomeActivity.WelcomeActivity
import com.varcassoftware.ridercabapp.databinding.ActivitySplashBinding
import com.varcassoftware.ridercabapp.localstorage.LocalStorage
import com.varcassoftware.ridercabapp.repository.RepositoryClass
import com.varcassoftware.ridercabapp.utility.SharedPreferencesKeys
import com.varcassoftware.ridercabapp.viewModelFactory.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.imageViewSplash.startAnimation(fadeIn)

        viewModel = ViewModelProvider(this, ViewModelFactory("", RepositoryClass()))[SplashViewModel::class.java]

        _init()
        observeSplashLiveData()
    }

    private fun _init() {
            viewModel.init()
    }

    private fun observeSplashLiveData() {
        viewModel.liveData.observe(this) {
            val localStorage = LocalStorage(this@SplashActivity)
            val intent: Intent
            if (localStorage.getBoolean(SharedPreferencesKeys.welcomScreenStatus, false)) {
                intent = Intent(this, ServiceActivity::class.java)
            } else {
                intent = Intent(this, WelcomeActivity::class.java)
            }
            val options = ActivityOptions.makeCustomAnimation(this,
                R.anim.activity_fade_in, R.anim.activity_fade_out)
            startActivity(intent, options.toBundle())
            finish()

        }
    }


}
