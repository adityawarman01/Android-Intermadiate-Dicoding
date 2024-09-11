package com.example.submissionintermediate.database.preference


data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)
