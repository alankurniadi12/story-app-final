package com.alankurniadi.storyapp.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alankurniadi.storyapp.model.ResponseAddNewStory
import com.alankurniadi.storyapp.network.ApiConfig
import com.alankurniadi.storyapp.utils.SettingPreferences
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(private val pref: SettingPreferences) : ViewModel() {

    private val _addStory = MutableLiveData<ResponseAddNewStory>()
    val addStory: LiveData<ResponseAddNewStory> = _addStory

    fun postNewStory(
        token: String,
        storyText: RequestBody,
        imageMultipart: MultipartBody.Part
    ) {
        val client =
            ApiConfig.getApiService().postNewStory("Bearer $token", storyText, imageMultipart)
        client.enqueue(object : Callback<ResponseAddNewStory> {
            override fun onResponse(
                call: Call<ResponseAddNewStory>,
                response: Response<ResponseAddNewStory>
            ) {
                if (response.isSuccessful) {
                    _addStory.value = response.body()
                } else {
                    Log.e(
                        AddStoryViewModel::class.java.simpleName,
                        "onResponse:${response.body().toString()}",
                    )
                }
            }

            override fun onFailure(call: Call<ResponseAddNewStory>, t: Throwable) {
                Log.e(
                    AddStoryViewModel::class.java.simpleName,
                    "onFailure:${t.message.toString()}",
                )
            }
        })
    }

    fun getToken(): LiveData<String> {
        return pref.getTokenKey().asLiveData()
    }
}
