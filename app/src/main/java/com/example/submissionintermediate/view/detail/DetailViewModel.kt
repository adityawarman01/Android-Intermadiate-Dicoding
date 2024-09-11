package com.example.submissionintermediate.view.detail

import androidx.lifecycle.ViewModel
import com.example.submissionintermediate.database.repository.StoryRepo

class DetailViewModel(private val repository: StoryRepo) : ViewModel() {
    fun goToDetail(id: String) = repository.DetailStory(id)
}