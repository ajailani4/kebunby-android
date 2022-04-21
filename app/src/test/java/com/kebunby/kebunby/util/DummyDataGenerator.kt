package com.kebunby.kebunby.util

import com.kebunby.kebunby.data.model.PlantCategory
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.data.model.User
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

fun generateUser() =
    User(
        username = "george",
        email = "george@email.com",
        name = "George",
        avatar = "test"
    )

fun generatePlantItem() =
    PlantItem(
        id = 1,
        name = "Tanaman",
        image = "test",
        category = "Tanaman Hias",
        wateringFreq = "3x Sehari",
        popularity = 10,
        isFavorited = true
    )

fun generatePlants() =
    listOf(
        generatePlantItem(),
        generatePlantItem(),
        generatePlantItem(),
        generatePlantItem(),
        generatePlantItem(),
    )

fun generatePlantCategory() =
    PlantCategory(
        id = 1,
        category = "Tanaman Hias"
    )

fun generatePlantCategories() =
    listOf(
        generatePlantCategory(),
        generatePlantCategory(),
        generatePlantCategory(),
        generatePlantCategory(),
        generatePlantCategory()
    )