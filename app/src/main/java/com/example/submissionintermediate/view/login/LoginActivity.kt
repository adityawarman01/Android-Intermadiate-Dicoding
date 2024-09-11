package com.example.submissionintermediate.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.submissionintermediate.MainActivity
import com.example.submissionintermediate.R
import com.example.submissionintermediate.ViewModelFactory
import com.example.submissionintermediate.customView.PwCustomView
import com.example.submissionintermediate.database.preference.UserModel
import com.example.submissionintermediate.database.repository.ResultState
import com.example.submissionintermediate.databinding.ActivityLoginBinding
import com.example.submissionintermediate.view.register.RegisterActivity
import kotlin.math.log

class LoginActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var inputEmail : String
    private lateinit var inputPassword : String


    private val viewModel by viewModels<LoginViewModel>{
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


      supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.login_page)

        binding.goToRegis.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding.submitButtonLogin.setOnClickListener(this)

        playAnimation()

    }

    private fun loginUser(){
        inputEmail = binding.edLoginEmail.text.toString()
        inputPassword = binding.edLoginPassword.text.toString()

        viewModel.login(inputEmail, inputPassword).observe(this) { result ->
            if (result != null){
                when(result) {
                    is ResultState.Success -> {
                        binding.submitButtonLogin.isEnabled = true
                        showLoading(false)
                        viewModel.saveSession(
                            UserModel(
                                result.data.loginResult.name,
                                result.data.loginResult.token,
                                )
                        )

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    is ResultState.Error -> {
                        showLoading(false)
                        binding.submitButtonLogin.isEnabled = true
                        Toast.makeText(this, "Login gagal", Toast.LENGTH_SHORT).show()
                    }
                    is ResultState.Loading -> {
                        binding.submitButtonLogin.isEnabled = false
                        showLoading(true)
                    }
                }
            }
        }
    }

private fun showLoading(isLoading: Boolean){
    binding.progressLogin.visibility =
        if (isLoading) View.VISIBLE else View.GONE
}

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.welcomeText, View.ALPHA, 1f).setDuration(300)
        val desc = ObjectAnimator.ofFloat(binding.descText, View.ALPHA, 1f).setDuration(300)
        val emailText = ObjectAnimator.ofFloat(binding.emailUser, View.ALPHA, 1f).setDuration(300)
        val emailEdtText = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(300)
        val passText = ObjectAnimator.ofFloat(binding.passwordUser, View.ALPHA, 1f).setDuration(300)
        val passEdtText = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(300)
        val btnLogin = ObjectAnimator.ofFloat(binding.submitButtonLogin, View.ALPHA, 1f).setDuration(300)
        val textRegis = ObjectAnimator.ofFloat(binding.wantToRegis, View.ALPHA, 1f).setDuration(300)
        val moveRegis = ObjectAnimator.ofFloat(binding.goToRegis, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                title,
                desc,
                emailText,
                emailEdtText,
                passText,
                passEdtText,
                btnLogin,
                textRegis,
                moveRegis
            )
            startDelay = 100
        }.start()
    }

    override fun onClick(v: View?){
        when(v){
            binding.submitButtonLogin -> {
                loginUser()
            }
        }
    }

    }




