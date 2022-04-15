package com.kebunby.kebunby.util

import com.kebunby.kebunby.data.model.UserCredential
import com.kebunby.kebunby.data.model.request.LoginRequest
import com.kebunby.kebunby.data.model.request.RegisterRequest

fun generateUserCredential() =
    UserCredential(
        username = "george",
        accessToken = "abc"
    )

fun generateLoginRequest() =
    LoginRequest(
        username = "george",
        password = "123"
    )

fun generateRegisterRequest() =
    RegisterRequest(
        username = "george",
        email = "geroge@email.com",
        password = "123",
        name = "George"
    )