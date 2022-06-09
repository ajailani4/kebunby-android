package com.kebunby.app.data.model.request

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val name: String
)
