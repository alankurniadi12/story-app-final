package com.alankurniadi.storyapp.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alankurniadi.storyapp.model.ResponseAllStories
import com.alankurniadi.storyapp.network.ApiConfig
import com.alankurniadi.storyapp.utils.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsStoryViewModel(private val pref: SettingPreferences): ViewModel() {

    private val _dataMaps = MutableLiveData<ResponseAllStories>()
    val dataMaps: LiveData<ResponseAllStories > = _dataMaps

        fun getAllStoryLocation(token: String) {
        val client = ApiConfig.getApiService().getStoriesLocation("Bearer $token")
        client.enqueue(object : Callback<ResponseAllStories> {
            override fun onResponse(
                call: Call<ResponseAllStories>,
                response: Response<ResponseAllStories>
            ) {
                if (response.isSuccessful) {
                    _dataMaps.value = response.body()
                }
            }

            override fun onFailure(call: Call<ResponseAllStories>, t: Throwable) {
                Log.e("MapsStoryViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }

}
