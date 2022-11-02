package com.alankurniadi.storyapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alankurniadi.storyapp.add.AddStoryViewModel
import com.alankurniadi.storyapp.authentication.login.LoginViewModel
import com.alankurniadi.storyapp.authentication.register.RegisterViewModel
import com.alankurniadi.storyapp.di.Injection
import com.alankurniadi.storyapp.home.ListStoryViewModel

class ViewModelFactory(
    private val context: Context,
    private val pref: SettingPreferences
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListStoryViewModel::class.java)) {
            return ListStoryViewModel(Injection.provideRepository(context), pref) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(pref) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel() as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
