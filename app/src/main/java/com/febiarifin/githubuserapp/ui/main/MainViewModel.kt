package com.febiarifin.githubuserapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.febiarifin.githubuserapp.api.ApiConfig
import com.febiarifin.githubuserapp.model.GithubResponse
import com.febiarifin.githubuserapp.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    companion object{
        private const val TAG = "MainViewModel"
    }

    val listUsers = MutableLiveData<ArrayList<User>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _searchState = MutableLiveData<Boolean>()
    val searchState: LiveData<Boolean> = _searchState

    init {
        _searchState.value = false
    }

    fun setSearchUsers(user: String) {
        _searchState.value = true
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUsers(user)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response != null){
                    listUsers.postValue(response.body()?.items)
                }
                if(response.body()?.totalCount == 0){
                    _message.value = "User Not Found"
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG,"Failure: ${t.message}")
                _message.value = t.message
            }
        })
    }

    fun getSearchUsers(): LiveData<ArrayList<User>>{
        return listUsers
    }
}