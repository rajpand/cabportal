package com.varcassoftware.ridercabapp.activity.welcomeActivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.activity.splashActivity.SplashViewModel
import com.varcassoftware.ridercabapp.activity.welcomeActivity.fragment.FirstSliderFragment
import com.varcassoftware.ridercabapp.activity.welcomeActivity.fragment.SecondSliderFragment
import com.varcassoftware.ridercabapp.activity.welcomeActivity.fragment.ThirdSliderFragment
import com.varcassoftware.ridercabapp.activity.welcomeActivity.fragment.ViewPagerAdapter
import com.varcassoftware.ridercabapp.databinding.ActivitySplashBinding
import com.varcassoftware.ridercabapp.databinding.ActivityWelcomeBinding
import com.varcassoftware.ridercabapp.viewModelFactory.ViewModelFactory

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var welcomeViewModel: WelcomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
      //  welcomeViewModel = ViewModelProvider(this, ViewModelFactory(application, ""))[WelcomeViewModel::class.java]

        _init()
    }

    private fun _init() {
        val fragmentList = arrayListOf<Fragment>(
            FirstSliderFragment(),
            SecondSliderFragment(),
            ThirdSliderFragment(),
        )
        val adapter = ViewPagerAdapter(
            fragmentList,
            supportFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = adapter
    }
}