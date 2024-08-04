package com.varcassoftware.ridercabapp.activity.welcomeActivity.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.activity.serviceactivity.ServiceActivity
import com.varcassoftware.ridercabapp.databinding.FragmentThridSliderBinding


class ThirdSliderFragment : Fragment() {

    private var _binding: FragmentThridSliderBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThridSliderBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.next3.setOnClickListener {
            val intent: Intent = Intent(activity, ServiceActivity::class.java)
            startActivity(intent)
            activity?.finish();
        }
        return view
    }

}