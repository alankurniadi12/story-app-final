package com.alankurniadi.storyapp.authentication.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alankurniadi.storyapp.model.ResponseLogin
import com.alankurniadi.storyapp.network.ApiConfig
import com.alankurniadi.storyapp.utils.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: SettingPreferences) : ViewModel() {

    private val _login = MutableLiveData<ResponseLogin>()
    val login: LiveData<ResponseLogin> = _login

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun postLogin(email: String, password: String) {
        val client = ApiConfig.getApiService().userLogin(email, password)
        client.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if (response.isSuccessful) {
                    _login.value = response.body()
                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.e(LoginViewModel::class.java.simpleName, "onFailure: ${t.message.toString()}")
            }
        })
    }
}
