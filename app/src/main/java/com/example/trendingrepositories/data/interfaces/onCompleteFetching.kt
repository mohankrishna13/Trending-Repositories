package com.example.trendingrepositories.data.interfaces

import com.example.trendingrepositories.data.model.TrendingRepoList
import com.example.trendingrepositories.data.model.TrendingRepoListItem
import retrofit2.Response

interface onCompleteFetching {
     fun onError(error: String)
     fun onSuccess(data: ArrayList<TrendingRepoListItem>)
}