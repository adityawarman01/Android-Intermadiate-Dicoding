package com.example.submissionintermediate.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionintermediate.database.preference.UserModel
import com.example.submissionintermediate.database.repository.StoryRepo
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: StoryRepo) : ViewModel() {

  fun login(email: String, password: String) = repository.login(email, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}