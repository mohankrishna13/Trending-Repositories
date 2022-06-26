package com.example.trendingrepositories.repository

import android.util.Log
import android.widget.Switch
import androidx.lifecycle.*
import com.example.trendingrepositories.data.api.ApiServices
import com.example.trendingrepositories.data.api.RetrofitInstance
import com.example.trendingrepositories.data.db.RepositoryDAO
import com.example.trendingrepositories.data.interfaces.onCompleteFetching
import com.example.trendingrepositories.data.model.TrendingRepoList
import com.example.trendingrepositories.data.model.TrendingRepoListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response


class TrendingRepoListRepository(private val repositoryDAO: RepositoryDAO) {
    val repoListFromDB=repositoryDAO.getRepositories()

    fun getRepositoryDataFromApi(onCompleteFetching: onCompleteFetching, mainActivity: LifecycleOwner){
        val apiServices: ApiServices = RetrofitInstance.getRetrofitInstance()
            .create(ApiServices::class.java)

        try{
            val responseLiveData:LiveData<Response<TrendingRepoList>> = liveData {
                val response=apiServices.getRepositories()
                emit(response)
            }
            responseLiveData.observe(mainActivity, Observer {
                if(it.isSuccessful){
                    onCompleteFetching.onSuccess(it.body() as ArrayList<TrendingRepoListItem>)
                    CoroutineScope(Dispatchers.IO).launch {
                        inserDataIntoOffline(it.body())
                    }
                }else{
                    var error:String
                    when(it.code()){
                        404->error="404  Not found web api"
                        403 -> error="403 Forbidden"
                        500 -> error="500 InternalServerError"
                        502 -> error="502 BadGateWay"
                        301 -> error="301 ResourceRemoved"
                        302 ->error="302 RemovedResourceFound"
                        else -> error=it.code().toString()+it.errorBody()
                    }
                   onCompleteFetching.onError(error)
                }
            })
        }catch (exception:Exception){
            onCompleteFetching.onError(exception.toString())
        }
    }

    private suspend fun inserDataIntoOffline(body: TrendingRepoList?) {
        if (body != null) {
            repositoryDAO.deleteAllRepositories()
            repositoryDAO.saveRepositories(body as ArrayList<TrendingRepoListItem>)
        }
    }
}

