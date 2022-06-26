package com.example.trendingrepositories.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trendingrepositories.repository.TrendingRepoListRepository

class MainActivityViewModelFactory(var trendingRepoListRepository: TrendingRepoListRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(trendingRepoListRepository)as T
    }
}