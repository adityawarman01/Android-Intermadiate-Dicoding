package com.example.submissionintermediate.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.submissionintermediate.MainActivity
import com.example.submissionintermediate.R
import com.example.submissionintermediate.ViewModelFactory
import com.example.submissionintermediate.database.repository.ResultState
import com.example.submissionintermediate.databinding.ActivityRegisterBinding
import com.example.submissionintermediate.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.register_page)

    binding.goToLogin.setOnClickListener {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
    }

    binding.submitButtonRegis.setOnClickListener {
        setUpAction()
    }

        playAnimation()
}


    private fun setUpAction(){
            val username = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

        viewModel.register(username, email, password).observe(this) { result ->
            if (result != null){
                when(result){
                    is ResultState.Loading ->{
                        showLoading(true)
                        binding.submitButtonRegis.isEnabled = false
                    }

                    is ResultState.Success -> {
                        Toast.makeText(this, "Selamat anda berhasil membuat akun", Toast.LENGTH_SHORT).show()
                        binding.submitButtonRegis.isEnabled = true
                        showLoading(false)
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    is ResultState.Error -> {
                        showLoading(false)
                        binding.submitButtonRegis.isEnabled = true
                        Toast.makeText(this, "your register is error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        }
    private fun showLoading(isBoolean: Boolean){
        binding.progressRegis.visibility =
            if (isBoolean) View.VISIBLE else View.GONE
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.logoRegis, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.welcomeTextRegis, View.ALPHA, 1f).setDuration(300)
        val desc = ObjectAnimator.ofFloat(binding.descText, View.ALPHA, 1f).setDuration(300)
        val uNText = ObjectAnimator.ofFloat(binding.usernameRegis, View.ALPHA, 1f).setDuration(300)
        val uNInput = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(300)
        val emailText = ObjectAnimator.ofFloat(binding.emailRegis, View.ALPHA, 1f).setDuration(300)
        val emailInput = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(300)
        val passText = ObjectAnimator.ofFloat(binding.passwordUserRegist, View.ALPHA, 1f).setDuration(300)
        val passInput = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(300)
        val btnRegis = ObjectAnimator.ofFloat(binding.submitButtonRegis, View.ALPHA, 1f).setDuration(300)
        val textlogin = ObjectAnimator.ofFloat(binding.alreadyLogin, View.ALPHA, 1f).setDuration(300)
        val moveLogin = ObjectAnimator.ofFloat(binding.goToLogin, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                title,
                desc,
                uNText,
                uNInput,
                emailText,
                emailInput,
                passText,
                passInput,
                btnRegis,
                textlogin,
                moveLogin
            )
            startDelay = 100
        }.start()
    }

    }

