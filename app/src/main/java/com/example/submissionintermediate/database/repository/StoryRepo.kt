package com.example.submissionintermediate.database.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.submissionintermediate.database.api.ApiService
import com.example.submissionintermediate.database.paging.StoryPagingSource
import com.example.submissionintermediate.database.paging.database.StoryDatabase
import com.example.submissionintermediate.database.preference.UserModel
import com.example.submissionintermediate.database.preference.UserPreference
import com.example.submissionintermediate.database.response.AddStoryResponse
import com.example.submissionintermediate.database.response.DetailStoryResponse
import com.example.submissionintermediate.database.response.ListStoryItem
import com.example.submissionintermediate.database.response.ListStoryResponse
import com.example.submissionintermediate.database.response.LoginResponse
import com.example.submissionintermediate.database.response.RegisterResponse
import com.example.submissionintermediate.view.login.LoginViewModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import java.io.File


class StoryRepo private constructor(
    private val apiService: ApiService,
    private val preferences: UserPreference,
    private val storyDatabase: StoryDatabase
){
    fun login(email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(email, password)
            val userModel = UserModel(successResponse.loginResult.name, successResponse.loginResult.token, true)
            saveSession(userModel)
            emit(ResultState.Success(successResponse))
        }catch (e:HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun register(name: String, email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.register(name, email, password)
            emit(ResultState.Success(successResponse))
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun uploadStory(imageFile: File, description: String) = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadStory(multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        }catch (e:HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddStoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun DetailStory(id: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getDetail(id)
            emit(ResultState.Success(successResponse))
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DetailStoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message!!))
        }
    }

    fun listStories(): LiveData<ResultState<PagingData<ListStoryItem>>> = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getAllStories()
            val listory = successResponse.listStory
            val newlist = listory.map { article ->
                ListStoryItem(
                    article.photoUrl,
                    article.createdAt,
                    article.name,
                    article.description,
                    article.lat,
                    article.id,
                    article.lon
                )
            }
            newlist.size
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ListStoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun LocationStories() = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getLocationStories()
                emit(ResultState.Success(successResponse))
        }catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ListStoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }

    }
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5, enablePlaceholders = false
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    suspend fun saveSession(user: UserModel){
        preferences.saveSession(user)
    }
    fun getSession(): Flow<UserModel>{
        return preferences.getSession()
    }
    suspend fun logout(){
        preferences.removeSession()
    }

    companion object {
        private var instance: StoryRepo? = null
//        fun getInstance(apiService: ApiService, preferences: UserPreference): StoryRepo{
//            if (instance == null){
//                instance = StoryRepo(apiService, preferences)
//            }
//            return instance!!
//        }
//    }

        fun getInstance(
            apiService: ApiService,
            preference: UserPreference,
            storyDatabase: StoryDatabase,
            Needed: Boolean
        ): StoryRepo? {
            if (preference == null || Needed) {
                synchronized(this) {
                    instance = StoryRepo(apiService, preference, storyDatabase)
                }
            }
            return instance!!
        }
    }

}