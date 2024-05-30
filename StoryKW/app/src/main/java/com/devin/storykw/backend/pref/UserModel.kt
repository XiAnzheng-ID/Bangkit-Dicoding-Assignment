package com.devin.storykw.backend.pref

data class UserModel(
    val name: String,
    val token: String,
    val isLogin: Boolean = false
)