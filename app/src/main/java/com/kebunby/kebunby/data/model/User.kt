package com.kebunby.kebunby.data.model

data class User(
    val username: String,
    val email: String,
    val name: String,
    val avatar: String?,
    val planting: Int,
    val planted: Int,
    val uploaded: Int
)
