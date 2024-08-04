package com.varcassoftware.ridercabapp.activity.serviceactivity

import android.content.Intent
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
import com.varcassoftware.ridercabapp.activity.loginActivity.UserAccountActivity

import com.varcassoftware.ridercabapp.activity.serviceactivity.adapter.OnServiceItemClickListener
import com.varcassoftware.ridercabapp.activity.serviceactivity.adapter.ServiceAdapter
import com.varcassoftware.ridercabapp.activity.serviceactivity.adapter.ServiceSliderAdapter
import com.varcassoftware.ridercabapp.databinding.ActivityServiceBinding
import com.varcassoftware.ridercabapp.entity.ServiceItem

class ServiceActivity : AppCompatActivity(), OnServiceItemClickListener {
    private lateinit var binding: ActivityServiceBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var sliderAdapter: ServiceSliderAdapter
    private val viewModel: ServiceViewModel by viewModels()
    private lateinit var tabLayout: TabLayout

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
        binding = ActivityServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.sliderForService
        tabLayout = binding.tabLayout

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
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        viewModel.serviceList.observe(this) { serviceList ->
            val adapter = ServiceAdapter(serviceList, this@ServiceActivity)
            binding.recyclerView.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
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
        val intent = Intent(this, UserAccountActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
