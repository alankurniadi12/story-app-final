package com.alankurniadi.storyapp.di

import android.content.Context
import com.alankurniadi.storyapp.data.StoryRepository
import com.alankurniadi.storyapp.network.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService)
    }
}
