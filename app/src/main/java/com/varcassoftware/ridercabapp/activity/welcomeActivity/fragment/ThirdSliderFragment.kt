package com.varcassoftware.ridercabapp.activity.welcomeActivity.fragment

import android.app.ActivityOptions
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
import com.varcassoftware.ridercabapp.localstorage.LocalStorage
import com.varcassoftware.ridercabapp.utility.SharedPreferencesKeys


class ThirdSliderFragment : Fragment() {

    private var _binding: FragmentThridSliderBinding? = null
    private val binding get() = _binding!!
    private  var localStorage:LocalStorage?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThridSliderBinding.inflate(inflater, container, false)
        val view = binding.root
        localStorage = LocalStorage(requireContext())
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.next3.setOnClickListener {
            localStorage?.saveBoolean(SharedPreferencesKeys.welcomScreenStatus,true)
            val intent: Intent = Intent(activity, ServiceActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(activity,
                R.anim.activity_fade_in, R.anim.activity_fade_out)
            startActivity(intent,options.toBundle())
            activity?.finish();
        }
        return view
    }

}