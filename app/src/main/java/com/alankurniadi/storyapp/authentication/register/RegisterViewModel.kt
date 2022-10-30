package com.alankurniadi.storyapp.authentication.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alankurniadi.storyapp.model.ResponseRegister
import com.alankurniadi.storyapp.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _register = MutableLiveData<ResponseRegister>()
    val register: LiveData<ResponseRegister> = _register

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun postRegister(name: String, email: String, password: String) {
        val client = ApiConfig.getApiService().userRegister(name, email, password)
        client.enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(
                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                if (response.isSuccessful) {
                    _register.value = response.body()
                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                Log.e(
                    RegisterViewModel::class.java.simpleName,
                    "onFailure: ${t.message.toString()}",
                )
            }
        })
    }
}
