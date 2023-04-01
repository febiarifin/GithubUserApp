package com.febiarifin.githubuserapp.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.febiarifin.githubuserapp.api.ApiConfig
import com.febiarifin.githubuserapp.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel: ViewModel() {
    companion object{
        private const val TAG = "FollowingViewModel"
    }

    val listFollowing = MutableLiveData<ArrayList<User>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setListFollowing(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<ArrayList<User>>{
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    listFollowing.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "Failure: ${t.message}")
            }

        })
    }

    fun getListFollowing(): LiveData<ArrayList<User>>{
        return listFollowing
    }
}