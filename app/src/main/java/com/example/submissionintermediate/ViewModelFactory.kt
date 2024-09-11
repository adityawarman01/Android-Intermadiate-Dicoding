package com.example.submissionintermediate

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissionintermediate.database.di.Injection
import com.example.submissionintermediate.database.repository.StoryRepo
import com.example.submissionintermediate.view.add.AddStoryViewModel
import com.example.submissionintermediate.view.detail.DetailViewModel
import com.example.submissionintermediate.view.login.LoginViewModel
import com.example.submissionintermediate.view.main.MainViewModel
import com.example.submissionintermediate.view.maps.MapsActivity
import com.example.submissionintermediate.view.maps.MapsViewModel
import com.example.submissionintermediate.view.register.RegisterViewModel
import com.example.submissionintermediate.view.splashscreenn.SplashScreenViewModel

class ViewModelFactory ( private val repository: StoryRepo):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SplashScreenViewModel::class.java) ->{
                SplashScreenViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }
           modelClass.isAssignableFrom(MainViewModel::class.java)-> {
               MainViewModel(repository) as T
           }
            else -> throw IllegalArgumentException("Unknown ViewModel Class: "+ modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE : ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context, isNeeded: Boolean): ViewModelFactory {
            synchronized(this){
                if(INSTANCE == null || isNeeded){
                    INSTANCE = Injection.provideRepository(context)?.let{ ViewModelFactory(it)}
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}