package com.varcassoftware.ridercabapp.activity.loginActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.databinding.ActivityUserAccountBinding
import com.varcassoftware.ridercabapp.repository.RepositoryClass
import com.varcassoftware.ridercabapp.viewModelFactory.ViewModelFactory

class UserAccountActivity  : AppCompatActivity() {

    private lateinit var binding: ActivityUserAccountBinding
    private lateinit var userAccountViewModel: UserAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userAccountViewModel  = ViewModelProvider(this,ViewModelFactory("",RepositoryClass()))[UserAccountViewModel::class.java]

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}