package com.febiarifin.githubuserapp.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.febiarifin.githubuserapp.api.ApiConfig
import com.febiarifin.githubuserapp.model.DetailUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel: ViewModel() {

    companion object{
        private const val TAG = "DetailUserViewModel"
    }

    val user = MutableLiveData<DetailUser>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> =  _message

    fun setUserDetail(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<DetailUser> {
            override fun onResponse(call: Call<DetailUser>, response: Response<DetailUser>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    user.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<DetailUser>, t: Throwable) {
                Log.d(TAG, "Failure: ${t.message}")
                _isLoading.value = false
                _message.value = t.message.toString()
            }

        })
    }

    fun getUserDetail(): LiveData<DetailUser>{
        return user
    }
}