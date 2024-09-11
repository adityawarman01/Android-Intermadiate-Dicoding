package com.example.submissionintermediate

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submissionintermediate.adapter.AdapterStory
import com.example.submissionintermediate.database.repository.ResultState
import com.example.submissionintermediate.databinding.ActivityMainBinding
import com.example.submissionintermediate.view.add.AddStory
import com.example.submissionintermediate.view.login.LoginActivity
import com.example.submissionintermediate.view.main.MainViewModel
import com.example.submissionintermediate.view.register.RegisterActivity
import com.google.android.material.snackbar.Snackbar
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvStory.layoutManager = LinearLayoutManager(this)

        binding.addNewStory.setOnClickListener {
           val intent = Intent(this@MainActivity, AddStory::class.java)
            startActivity(intent)
//            finish()
        }

        viewModel.getSession().observe(this) {
            if (!it.isLogin){
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            }
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.story)

        listOfStories()
            }

    override fun onResume() {
        super.onResume()
        viewModel.Stories()
    }
    private fun listOfStories(){
//        viewModel.getSession().observe(this) { UserSession ->
//            if (!UserSession.isLogin) {
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//                finish()
//            } else {
                viewModel.Stories().observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is ResultState.Success -> {
                                showLoading(false)
                                val list = AdapterStory(result.data.listStory)
                                binding.rvStory.adapter = list
                            }

                            is ResultState.Error -> {
//                                viewModel.getSession().observe(this){ user ->
//                                    showSnackBar(user.token)
//                                }
                                showLoading(false)
                                Toast.makeText(this, "Gagal menampilkan cerita", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            is ResultState.Loading -> {
                                showLoading(true)
                            }
                        }
                    }
                }
            }
       // }
    //}

    private fun showLoading (isBoolean: Boolean){
        binding.progressMain.visibility = if (isBoolean)
            View.VISIBLE else View.GONE
    }

    private fun showSnackBar(messsage: String){
        Snackbar.make(findViewById(android.R.id.content), messsage, Snackbar.LENGTH_LONG)
            .show()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.add_new_story -> {
//                showLoading(false)
//                Intent(this@MainActivity, AddStory::class.java).also {
//                    startActivity(it)
//                }
//            }
            R.id.logOut -> {
                showLoading(false)
                viewModel.logout()
                Intent(this@MainActivity, RegisterActivity::class.java).also {
                   startActivity(it)
               }
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }




}