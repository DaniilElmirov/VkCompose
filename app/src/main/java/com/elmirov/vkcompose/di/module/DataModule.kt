package com.elmirov.vkcompose.di.module

import android.content.Context
import com.elmirov.vkcompose.data.network.api.VkApi
import com.elmirov.vkcompose.data.repository.PostRepositoryImpl
import com.elmirov.vkcompose.di.annotation.ApplicationScope
import com.elmirov.vkcompose.domain.repository.PostRepository
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
interface DataModule {

    companion object {
        private const val BASE_URL = "https://api.vk.com/method/"

        @ApplicationScope
        @Provides
        fun provideHttpClient(): OkHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
                .build()

        @ApplicationScope
        @Provides
        fun provideRetrofit(httpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(httpClient)
                .build()

        @ApplicationScope
        @Provides
        fun provideVkApi(retrofit: Retrofit): VkApi =
            retrofit.create()

        @ApplicationScope
        @Provides
        fun provideVkStorage(
            context: Context,
        ): VKPreferencesKeyValueStorage {
            return VKPreferencesKeyValueStorage(context)
        }
    }

    @ApplicationScope
    @Binds
    fun bindPostRepository(impl: PostRepositoryImpl): PostRepository
}