package com.varcassoftware.ridercabapp.activity.createpin

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.activity.map.MapActivity
import com.varcassoftware.ridercabapp.activity.otp.OtpViewModel
import com.varcassoftware.ridercabapp.databinding.ActivityCreatePinBinding
import com.varcassoftware.ridercabapp.databinding.ActivityOtpBinding
import com.varcassoftware.ridercabapp.localstorage.LocalStorage
import com.varcassoftware.ridercabapp.repository.RepositoryClass
import com.varcassoftware.ridercabapp.viewModelFactory.ViewModelFactory

class CreatePinActivity : AppCompatActivity() {
    private var _binding: ActivityCreatePinBinding? = null
    private val binding get() = _binding!!

    private lateinit var createPinViewModel: CreatePinViewModel
    private var localStorage: LocalStorage? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityCreatePinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createPinViewModel = ViewModelProvider(
            this, ViewModelFactory("", RepositoryClass())
        )[CreatePinViewModel::class.java]


        _init()
        onClickListeners()
        setObserve()
    }

    private fun setObserve() {

    }

    private fun onClickListeners() {
        binding.buttonSignUp.setOnClickListener {

        }
    }

    private fun _init() {

    }
}