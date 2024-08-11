package com.varcassoftware.ridercabapp.activity.serviceactivity

import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.activity.login.LoginActivity
import com.varcassoftware.ridercabapp.activity.map.MapActivity

import com.varcassoftware.ridercabapp.activity.serviceactivity.adapter.OnServiceItemClickListener
import com.varcassoftware.ridercabapp.activity.serviceactivity.adapter.ServiceAdapter
import com.varcassoftware.ridercabapp.activity.serviceactivity.adapter.ServiceSliderAdapter
import com.varcassoftware.ridercabapp.activity.welcomeActivity.WelcomeActivity
import com.varcassoftware.ridercabapp.databinding.ActivityServiceBinding
import com.varcassoftware.ridercabapp.entity.ServiceItem
import com.varcassoftware.ridercabapp.localstorage.LocalStorage
import com.varcassoftware.ridercabapp.utility.SharedPreferencesKeys

class ServiceActivity : AppCompatActivity(), OnServiceItemClickListener {
    private  var _binding: ActivityServiceBinding? =null
    private  val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var sliderAdapter: ServiceSliderAdapter
    private val viewModel: ServiceViewModel by viewModels()
    private lateinit var tabLayout: TabLayout
    private var localStorage: LocalStorage? = null

    private val slideDuration: Long = 2000 // 2 seconds
    private val handler = Handler(Looper.getMainLooper())
    private val updateSlide = object : Runnable {
        override fun run() {
            val nextItem = (viewPager.currentItem + 1) % sliderAdapter.itemCount
            viewPager.setCurrentItem(nextItem, true)
            handler.postDelayed(this, slideDuration)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        _binding = ActivityServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadSlider()
        setClickLisner()
        setObserver()
    }

    private fun setClickLisner() {

    }

    private fun loadSlider() {
        viewPager = binding.sliderForService
        tabLayout = binding.tabLayout
        _init()
        viewModel.imageList.observe(this, Observer { imageList ->
            sliderAdapter = ServiceSliderAdapter(imageList)
            viewPager.adapter = sliderAdapter
            setupTabLayout()
            handler.post(updateSlide)
        })

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateTabIndicators(position)
            }
        })
    }

    private fun setObserver() {
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        viewModel.serviceList.observe(this) { serviceList ->
            val adapter = ServiceAdapter(serviceList, this@ServiceActivity)
            binding.recyclerView.adapter = adapter
        }
    }

    private fun _init() {
        localStorage = LocalStorage(this@ServiceActivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        handler.removeCallbacks(updateSlide)
    }

    private fun setupTabLayout() {
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null) {
                tab.setCustomView(R.layout.custom_tab)
                val tabView = tab.customView
                if (tabView != null) {
                    val imageView = tabView.findViewById<ImageView>(R.id.tab_indicator)
                    imageView.setImageResource(
                        if (i == 0) R.drawable.tab_indicator_selected else R.drawable.tab_indicator_unselected
                    )
                }
            }
        }
    }

    private fun updateTabIndicators(position: Int) {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null) {
                val tabView = tab.customView
                if (tabView != null) {
                    val imageView = tabView.findViewById<ImageView>(R.id.tab_indicator)
                    imageView.setImageResource(
                        if (i == position) R.drawable.tab_indicator_selected else R.drawable.tab_indicator_unselected
                    )
                }
            }
        }
    }

    override fun onServiceItemClick(serviceItem: ServiceItem) {
        val intent: Intent
        if (localStorage?.getBoolean(SharedPreferencesKeys.loginScreenStatus, false) == false) {
            intent = Intent(this, LoginActivity::class.java)
        } else {
            intent = Intent(this, MapActivity::class.java)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val options = ActivityOptions.makeCustomAnimation(
            this,
            R.anim.activity_fade_in, R.anim.activity_fade_out
        )
        startActivity(intent, options.toBundle())
        startActivity(intent)

    }
}
