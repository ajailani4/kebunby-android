package com.kebunby.kebunby.util

import com.kebunby.kebunby.data.model.UserCredential
import com.kebunby.kebunby.data.model.request.LoginRequest

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