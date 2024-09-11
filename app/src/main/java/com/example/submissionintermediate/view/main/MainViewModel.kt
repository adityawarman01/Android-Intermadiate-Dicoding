package com.example.submissionintermediate.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.submissionintermediate.database.preference.UserModel
import com.example.submissionintermediate.database.repository.StoryRepo
import com.example.submissionintermediate.database.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository : StoryRepo) : ViewModel() {

      val lisStory: LiveData<PagingData<ListStoryItem>> =
        repository.getStory().cachedIn(viewModelScope)


    fun Stories() = repository.getStory()

    fun getSession(): LiveData<UserModel>  {
        return repository.getSession().asLiveData()
    }
    fun logout(){
        viewModelScope.launch {
            repository.logout()
        }
    }

}