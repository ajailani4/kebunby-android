package com.kebunby.kebunby.util

import com.kebunby.kebunby.data.model.*
import com.kebunby.kebunby.data.model.request.LoginRequest
import com.kebunby.kebunby.data.model.request.PlantActRequest
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
        avatar = "test",
        planting = 10,
        planted = 10,
        uploaded = 10
    )

fun generatePlantItem() =
    PlantItem(
        id = 1,
        name = "Tanaman",
        image = "test",
        category = generatePlantCategory(),
        growthEst = "2-3 Tahun",
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
        name = "Tanaman Hias"
    )

fun generatePlantCategories() =
    listOf(
        generatePlantCategory(),
        generatePlantCategory(),
        generatePlantCategory(),
        generatePlantCategory(),
        generatePlantCategory()
    )

fun generatePlantActRequest() =
    PlantActRequest(
        plantId = 1
    )

fun generatePlant() =
    Plant(
        id = 1,
        name = "Tanaman",
        latinName = "Tanaman latin",
        image = "test",
        category = generatePlantCategory(),
        isPlanting = true,
        isPlanted = false,
        isFavorited = true,
        wateringFreq = "3x Sehari",
        growthEst = "2-3 Tahun",
        desc = "This is a plant",
        tools = listOf(
            "tool 1", "tool 2"
        ),
        materials = listOf(
            "material 1", "material 2"
        ),
        steps = listOf(
            "step 1", "step 2"
        ),
        popularity = 10,
        author = "George",
        publishedOn = "2022-05-12"
    )