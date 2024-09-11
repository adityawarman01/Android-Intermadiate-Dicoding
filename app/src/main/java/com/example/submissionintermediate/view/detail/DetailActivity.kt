package com.example.submissionintermediate.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.submissionintermediate.R
import com.example.submissionintermediate.ViewModelFactory
import com.example.submissionintermediate.database.repository.ResultState
import com.example.submissionintermediate.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.detail_story)

        showDetailStory()

    }

    private fun showDetailStory(){
        intent.getStringExtra(STORY_ID)?.let {
            viewModel.goToDetail(it).observe(this) { result ->
                if (result != null){
                    when(result) {
                        is  ResultState.Success ->{
                        showLoading(false)
                            Glide.with(binding.root.context)
                                .load(result.data.story?.photoUrl)
                                .into(binding.storyPic)
                            binding.descriptionStory.text = result.data.story?.description
                            binding.titleStory.text = result.data.story?.name
                        }
                        is ResultState.Error -> {
                            showLoading(false)
                            Toast.makeText(this, "Gagal memuat Detail", Toast.LENGTH_SHORT).show()
                        }
                        is ResultState.Loading -> {
                            showLoading(true)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isBoolean: Boolean){
        binding.progressDetail.visibility = if (isBoolean) View.VISIBLE
        else View.GONE
    }

    companion object{
        var STORY_ID = "ID_STORY"
    }
}