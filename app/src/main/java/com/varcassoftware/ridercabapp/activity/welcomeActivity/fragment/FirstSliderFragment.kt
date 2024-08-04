package com.varcassoftware.ridercabapp.activity.welcomeActivity.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.databinding.FragmentFristSliderBinding


class FirstSliderFragment : Fragment() {
    private var _binding: FragmentFristSliderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFristSliderBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.next.setOnClickListener {
            viewPager?.currentItem = 1
        }
        return view
    }
}