package com.kebunby.kebunby.di

import com.kebunby.kebunby.BuildConfig
import com.kebunby.kebunby.data.api.PlantCategoryService
import com.kebunby.kebunby.data.api.PlantService
import com.kebunby.kebunby.data.api.UserService
import com.kebunby.kebunby.util.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @Provides
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    fun providePlantService(retrofit: Retrofit): PlantService =
        retrofit.create(PlantService::class.java)

    @Provides
    fun providePlantCategoryService(retrofit: Retrofit): PlantCategoryService =
        retrofit.create(PlantCategoryService::class.java)
}