package com.example.submissionintermediate.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.submissionintermediate.database.preference.UserModel
import com.example.submissionintermediate.database.repository.StoryRepo
import kotlinx.coroutines.launch

class MainViewModel(private val repository: StoryRepo) : ViewModel() {
    fun getSession(): LiveData<UserModel>  {
        return repository.getSession().asLiveData()
    }
    fun logout(){
        viewModelScope.launch {
            repository.logout()
        }
    }
    fun Stories() = repository.listStories()
}