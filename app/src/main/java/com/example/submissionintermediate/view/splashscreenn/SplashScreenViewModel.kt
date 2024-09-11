package com.example.submissionintermediate.view.splashscreenn

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.submissionintermediate.database.preference.UserModel
import com.example.submissionintermediate.database.repository.StoryRepo

class SplashScreenViewModel(private val repo: StoryRepo): ViewModel() {
    fun getSession(): LiveData<UserModel>{
        return repo.getSession().asLiveData()
    }
}