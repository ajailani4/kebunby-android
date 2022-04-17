package com.kebunby.kebunby.util

import com.kebunby.kebunby.domain.use_case.user_credential.GetUserCredentialUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val getUserCredentialUseCase: GetUserCredentialUseCase
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val userCredential = runBlocking {
            getUserCredentialUseCase.invoke().first()
        }

        requestBuilder.addHeader("Authorization", "Bearer ${userCredential.accessToken}")

        return chain.proceed(requestBuilder.build())
    }
}