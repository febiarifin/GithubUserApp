package com.febiarifin.githubuserapp.api

import com.febiarifin.githubuserapp.model.DetailUser
import com.febiarifin.githubuserapp.model.GithubResponse
import com.febiarifin.githubuserapp.model.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun searchUsers(
        @Query("q") username: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUser>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ):Call<ArrayList<User>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ):Call<ArrayList<User>>
}