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
    //Getting Data From RoomDatabase
    val repoListFromDB=repositoryDAO.getRepositories()

    //Getting Data From Api
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
                    //Once got Success as Response from Api.Sending Data to Interface
                    onCompleteFetching.onSuccess(it.body() as ArrayList<TrendingRepoListItem>)
                    CoroutineScope(Dispatchers.IO).launch {
                        //Calling function to Store Api Response Data into Room Database
                        inserDataIntoOffline(it.body())
                    }
                }else{
                    //If Api response is not successful
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
                    //Setting Error response to Interface
                   onCompleteFetching.onError(error)
                }
            })
        }catch (exception:Exception){
            onCompleteFetching.onError(exception.toString())
        }
    }

    //Storing Data into Room Database
    private suspend fun inserDataIntoOffline(body: TrendingRepoList?) {
        if (body != null) {
            //Deleting Existing Data in Room Database
            repositoryDAO.deleteAllRepositories()
            //Saving Data into Room Database
            repositoryDAO.saveRepositories(body as ArrayList<TrendingRepoListItem>)
        }
    }
}

