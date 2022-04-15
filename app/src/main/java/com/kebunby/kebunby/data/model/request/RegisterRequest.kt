package com.kebunby.kebunby.data.model.request

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val name: String
)
