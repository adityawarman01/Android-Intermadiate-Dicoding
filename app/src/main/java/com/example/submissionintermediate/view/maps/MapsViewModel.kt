package com.example.submissionintermediate.view.maps

import androidx.lifecycle.ViewModel
import com.example.submissionintermediate.database.repository.StoryRepo

class MapsViewModel(private val repository: StoryRepo): ViewModel() {
    fun locationStories() = repository.LocationStories()
}