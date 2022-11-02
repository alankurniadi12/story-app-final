package com.alankurniadi.storyapp.home

import androidx.lifecycle.*
import androidx.paging.PagingData
import com.alankurniadi.storyapp.data.StoryRepository
import com.alankurniadi.storyapp.model.ListStoryItem
import com.alankurniadi.storyapp.model.ResponseAllStories
import com.alankurniadi.storyapp.utils.SettingPreferences
import kotlinx.coroutines.launch

class ListStoryViewModel(private val storyRepository: StoryRepository, private val pref: SettingPreferences) :
    ViewModel() {

    private val _listStory = MutableLiveData<ResponseAllStories>()
    val listStory: LiveData<ResponseAllStories> = _listStory

    fun getAllStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return storyRepository.getStoryList(token)
    }
//    fun getAllStory(token: String) {
//        val client = ApiConfig.getApiService().getAllStories(
//            "Bearer $token",
//            1,
//            10
//        )
//        client.enqueue(object : Callback<ResponseAllStories> {
//            override fun onResponse(
//                call: Call<ResponseAllStories>,
//                response: Response<ResponseAllStories>
//            ) {
//                if (response.isSuccessful) {
//                    _listStory.value = response.body()
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseAllStories>, t: Throwable) {
//                Log.e(TAG, "onFailure: ${t.message.toString()}")
//            }
//        })
//    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveTokenKey(token)
        }
    }

    fun getToken(): LiveData<String> {
        return pref.getTokenKey().asLiveData()
    }
}
