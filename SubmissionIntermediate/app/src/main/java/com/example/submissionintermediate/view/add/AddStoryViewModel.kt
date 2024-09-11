package com.example.submissionintermediate.view.add

import androidx.lifecycle.ViewModel
import com.example.submissionintermediate.database.repository.StoryRepo
import java.io.File

class AddStoryViewModel(private val repository: StoryRepo) : ViewModel() {
    fun uploadStory(file: File, description: String) = repository.uploadStory(file, description)
}