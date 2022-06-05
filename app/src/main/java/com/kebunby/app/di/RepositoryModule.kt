package com.kebunby.app.di

import com.kebunby.app.data.repository.PlantRepository
import com.kebunby.app.data.repository.UserCredentialRepository
import com.kebunby.app.data.repository.UserRepository
import com.kebunby.app.data.repository.impl.PlantRepositoryImpl
import com.kebunby.app.data.repository.impl.UserCredentialRepositoryImpl
import com.kebunby.app.data.repository.impl.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bindUserCredentialRepository(
        userCredentialRepositoryImpl: UserCredentialRepositoryImpl
    ): UserCredentialRepository

    @Binds
    abstract fun bindPlantRepository(
        plantRepositoryImpl: PlantRepositoryImpl
    ): PlantRepository
}