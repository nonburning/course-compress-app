package com.example.encryptionkurs.di

import android.content.Context
import android.content.SharedPreferences
import com.example.encryptionkurs.data.EncryptionRepositoryImpl
import com.example.encryptionkurs.data.api.ApiService
import com.example.encryptionkurs.domain.EncryptionRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {


    private val contentType = "application/json".toMediaType()

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, sharedPreferences: SharedPreferences) = Retrofit.Builder()
        .baseUrl(sharedPreferences.getString("api_url", "https://7c6a-77-121-152-86.ngrok-free.app/")
            ?: "https://7c6a-77-121-152-86.ngrok-free.app/")
        .addConverterFactory(json.asConverterFactory(contentType))
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideEncryptionRepository(
        apiService: ApiService,
        sharedPreferences: SharedPreferences,
    ): EncryptionRepository =
        EncryptionRepositoryImpl(
            apiService, sharedPreferences
        )

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences =
        appContext.getSharedPreferences(
            "shared_prefs", Context.MODE_PRIVATE
        )

}