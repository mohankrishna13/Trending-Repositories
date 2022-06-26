package com.example.trendingrepositories.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trendingrepositories.data.model.TrendingRepoListItem

@Dao
interface RepositoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRepositories(repositories: kotlin.collections.ArrayList<com.example.trendingrepositories.data.model.TrendingRepoListItem>)

    @Query("DELETE FROM trending_repositories")
    suspend fun deleteAllRepositories()

    @Query("SELECT * FROM trending_repositories")
    fun getRepositories(): LiveData<List<TrendingRepoListItem>>

}