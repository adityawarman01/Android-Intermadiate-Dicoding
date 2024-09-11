package com.example.submissionintermediate.view.splashscreenn

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.example.submissionintermediate.MainActivity
import com.example.submissionintermediate.R
import com.example.submissionintermediate.ViewModelFactory
import com.example.submissionintermediate.view.register.RegisterActivity

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private val viewModel by viewModels<SplashScreenViewModel>{
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val timeSplashScreen = 1000L

        Handler(Looper.getMainLooper()).postDelayed({

            var intent = Intent(this, MainActivity::class.java)
            viewModel.getSession().observe(this){
                user ->
                if (!user.isLogin){
                    intent = Intent(this, RegisterActivity::class.java)
                }
            }
            startActivity(intent)
            finish()
        }, timeSplashScreen)
    }
}