package com.example.submissionintermediate.database.di

import android.content.Context
import com.example.submissionintermediate.database.api.ApiConfig
import com.example.submissionintermediate.database.paging.database.StoryDatabase
import com.example.submissionintermediate.database.preference.UserPreference
import com.example.submissionintermediate.database.preference.dataStore
import com.example.submissionintermediate.database.repository.StoryRepo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepo{
        val pref = UserPreference.getInstance(context.dataStore)
        val token = runBlocking {
            pref.getSession().first().token
        }
        val apiService = ApiConfig.getApiService(token)
        val storiDatabase = StoryDatabase.getDatabase(context)
        return StoryRepo.getInstance(apiService, pref, storiDatabase, true)!!
    }
}