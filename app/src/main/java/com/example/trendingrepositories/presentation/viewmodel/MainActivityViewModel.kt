package com.example.trendingrepositories.presentation.viewmodel

import android.widget.Toast
import androidx.lifecycle.*
import com.example.trendingrepositories.data.interfaces.onCompleteFetching
import com.example.trendingrepositories.presentation.activity.MainActivity
import com.example.trendingrepositories.repository.TrendingRepoListRepository

class MainActivityViewModel(
    val trendingRepoListRepository: TrendingRepoListRepository): ViewModel() {


    val RepoDataFromRoom =trendingRepoListRepository.repoListFromDB

    fun getAllRepository(mainActivity: MainActivity,onCompleteFetching: onCompleteFetching){
        trendingRepoListRepository.getRepositoryDataFromApi(onCompleteFetching,mainActivity)
    }



}



