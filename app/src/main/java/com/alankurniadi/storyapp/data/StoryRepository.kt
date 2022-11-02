package com.alankurniadi.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.alankurniadi.storyapp.model.ListStoryItem
import com.alankurniadi.storyapp.network.ApiService
import java.util.StringTokenizer

class StoryRepository(private val apiService: ApiService) {
    fun getStoryList(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }

}
