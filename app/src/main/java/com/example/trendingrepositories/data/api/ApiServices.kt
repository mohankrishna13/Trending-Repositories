package com.example.trendingrepositories.data.api

import com.example.trendingrepositories.data.model.TrendingRepoList
import retrofit2.Response
import retrofit2.http.GET

interface ApiServices {
    @GET("/repositories")
    suspend fun getRepositories():Response<TrendingRepoList>
}