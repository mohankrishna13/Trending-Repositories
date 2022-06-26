package com.example.trendingrepositories.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.trendingrepositories.data.model.BuiltByConverters
import com.example.trendingrepositories.data.model.TrendingRepoList
import com.example.trendingrepositories.data.model.TrendingRepoListItem

@Database(entities = [TrendingRepoListItem::class], version = 2, exportSchema = false)
@TypeConverters(BuiltByConverters::class)
abstract class RepositoryDatabase:RoomDatabase() {
    abstract val repositoryDAO:RepositoryDAO

    companion object{
        @Volatile
        private var INSTANCE:RepositoryDatabase?=null
        fun getInstance(context: Context):RepositoryDatabase{
            synchronized(this){
                var instance:RepositoryDatabase?= INSTANCE
                if(instance==null){
                    instance=Room.databaseBuilder(context.applicationContext,
                        RepositoryDatabase::class.java,"GithubRepo").build()
                }
                return instance
            }
        }
    }
}