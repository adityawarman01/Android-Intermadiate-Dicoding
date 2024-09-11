package com.example.submissionintermediate.database.paging.database

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.submissionintermediate.database.api.ApiService
import com.example.submissionintermediate.database.paging.StoryPagingSource
import com.example.submissionintermediate.database.response.ListStoryItem

//class StoryRepository(private val storyDatabase : StoryDatabase, private val apiService: ApiService) {
//    fun getStory(): LiveData<PagingData<ListStoryItem>> {
//        return Pager(
//            config = PagingConfig(
//                    pageSize = 5
//                    ),
//            pagingSourceFactory = {
//                StoryPagingSource(apiService)
//            }
//        ).liveData
//    }}