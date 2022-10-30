package com.alankurniadi.storyapp.home

import android.util.Log
import androidx.lifecycle.*
import com.alankurniadi.storyapp.model.ResponseAllStories
import com.alankurniadi.storyapp.network.ApiConfig
import com.alankurniadi.storyapp.utils.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListStoryViewModel(private val pref: SettingPreferences) : ViewModel() {

    companion object {
        const val TAG = "ListStoryViewModel"
    }
    private val _listStory = MutableLiveData<ResponseAllStories>()
    val listStory: LiveData<ResponseAllStories> = _listStory

    fun getAllStory(token: String) {
        val client = ApiConfig.getApiService().getAllStories(
            "Bearer $token",
            1,
            10
        )
        client.enqueue(object : Callback<ResponseAllStories> {
            override fun onResponse(
                call: Call<ResponseAllStories>,
                response: Response<ResponseAllStories>
            ) {
                if (response.isSuccessful) {
                    _listStory.value = response.body()
                }
            }

            override fun onFailure(call: Call<ResponseAllStories>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveTokenKey(token)
        }
    }

    fun getToken(): LiveData<String> {
        return pref.getTokenKey().asLiveData()
    }
}
