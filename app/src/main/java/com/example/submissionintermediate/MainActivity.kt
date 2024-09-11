package com.example.submissionintermediate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionintermediate.adapter.AdapterStory
import com.example.submissionintermediate.adapter.LoadingStateAdapter
import com.example.submissionintermediate.databinding.ActivityMainBinding
import com.example.submissionintermediate.view.add.AddStory
import com.example.submissionintermediate.view.login.LoginActivity
import com.example.submissionintermediate.view.main.MainViewModel
import com.example.submissionintermediate.view.maps.MapsActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvStory.layoutManager = LinearLayoutManager(this)

        binding.addNewStory.setOnClickListener {
           val intent = Intent(this@MainActivity, AddStory::class.java)
            startActivity(intent)
            finish()
        }
        getData()

        val backPress = supportActionBar
        backPress!!.setDisplayHomeAsUpEnabled(true)

        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.story)

        viewModel.getSession().observe(this) { UserSession ->
            if (!UserSession.isLogin) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
            }

    override fun onResume() {
        super.onResume()
        viewModel.Stories()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.map_open -> {
                showLoading(false)
             Intent(this@MainActivity, MapsActivity::class.java).also {
                 startActivity(it)
             }
            }
            R.id.logOut -> {
                showLoading(false)
                viewModel.logout()
                Intent(this@MainActivity, LoginActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading (isBoolean: Boolean){
        binding.progressMain.visibility = if (isBoolean)
            View.VISIBLE else View.GONE
    }

    private fun getData(){
        val adapter = AdapterStory()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )
        viewModel.lisStory.observe(this, {
            adapter.submitData(lifecycle, it )
        })
    }

}