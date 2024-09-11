package com.example.submissionintermediate.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.submissionintermediate.database.preference.UserModel
import com.example.submissionintermediate.database.repository.StoryRepo
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: StoryRepo) : ViewModel() {

    fun register(name: String, email: String, password: String) = repository.register(name,email, password)

    fun getSession(): LiveData<UserModel>{
        return repository.getSession().asLiveData()
    }
}