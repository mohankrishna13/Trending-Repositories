package com.example.trendingrepositories.presentation.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingrepositories.data.db.RepositoryDatabase
import com.example.trendingrepositories.data.interfaces.onCompleteFetching
import com.example.trendingrepositories.data.model.TrendingRepoListItem
import com.example.trendingrepositories.presentation.adapter.RepositoryAdapter
import com.example.trendingrepositories.presentation.utils.InternetConnection
import com.example.trendingrepositories.presentation.viewmodel.MainActivityViewModel
import com.example.trendingrepositories.repository.TrendingRepoListRepository
import com.example.trendingrepositories.R
import com.example.trendingrepositories.databinding.ActivityMainBinding
import com.example.trendingrepositories.presentation.viewmodel.MainActivityViewModelFactory
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
    private lateinit var RepoViewModel: MainActivityViewModel
    private lateinit var adapter: RepositoryAdapter

    private var repoCacheData=ArrayList<TrendingRepoListItem>()
    private var filterOn:Boolean = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding=DataBindingUtil.setContentView(this, R.layout.activity_main)

        //Setting Toolbar
        setSupportActionBar(mainBinding.toolbar)

        //Initializing DAO of RoomDatabase
        var dao=RepositoryDatabase.getInstance(application).repositoryDAO

        //Initializing Repository , passing DAO as a parameter
        val repo=TrendingRepoListRepository(dao)

        //Initializing ModelViewFactory ,passing repo as parameter
        val factory= MainActivityViewModelFactory(repo)

        //Initializing Viewmodel
        RepoViewModel= ViewModelProvider(this,factory).get(MainActivityViewModel::class.java)
        mainBinding.lifecycleOwner=this

        //Onclick Listener for Swipe(Updating data when refresh)
        mainBinding.refreshLayout.setOnRefreshListener {
           if(!filterOn){
               CoroutineScope(Dispatchers.Main).launch {
                   if(InternetConnection().isOnline(this@MainActivity)){
                       getDataFromOnline()
                   }else{
                       clearRecyclerView()
                   }
               }
           }
            mainBinding.refreshLayout.isRefreshing = false
        }

        //Initializing Recyclerview to show trending Repositories
        initRecyclerView()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            val inflater = menuInflater
            inflater.inflate(R.menu.search_menu, menu)
            val searchViewItem: MenuItem = menu!!.findItem(R.id.app_bar_search)
            val searchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
            searchView.isIconified=true

        //OnClick Listener for Search View in Toolbar as Option Menu
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    searchView.clearFocus()
                    return false
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    if(newText.isBlank() || newText.isEmpty()){
                        filterOn=false
                        adapter.setReposList(repoCacheData,{ selectedRepo: TrendingRepoListItem ->
                            onSelectedRepo(
                                selectedRepo
                            )
                        })
                        adapter.notifyDataSetChanged()
                    }else{
                        //Passing User Entered Data for filtering
                        filterOn=true
                        adapter.filter.filter(newText)
                    }
                    return false
                }
            })
        return super.onCreateOptionsMenu(menu)
    }
    private fun clearRecyclerView() {
        val responseData=ArrayList<TrendingRepoListItem>()
        adapter.setReposList(responseData) { selectedRepo: TrendingRepoListItem ->
            onSelectedRepo(
                selectedRepo
            )
        }
        adapter.notifyDataSetChanged()
        mainBinding.mainScreen.visibility=View.GONE
        mainBinding.internetOffScreen.visibility=View.VISIBLE
    }
    fun initRecyclerView(){
        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RepositoryAdapter()
        mainBinding.recyclerView.adapter = adapter
        checkConnectivity()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkConnectivity() {
        //Checking Internet Connection
       if(InternetConnection().isOnline(this)){
           CoroutineScope(Dispatchers.Main).launch {
               getDataFromOnline()
           }
       }else{
           //Checking Data in room database
           checkDataInLocal()
       }
    }
    private fun checkDataInLocal() {
        RepoViewModel.RepoDataFromRoom.observe(this, Observer {
            if(it.size==0){
                clearRecyclerView()
            }else{
                mainBinding.toolbar.menu.findItem(R.id.app_bar_search).isVisible=true
                repoCacheData.clear()
                repoCacheData.addAll(it)
                adapter.setReposList(it as ArrayList<TrendingRepoListItem>) { selectedRepo: TrendingRepoListItem ->
                    onSelectedRepo(
                        selectedRepo
                    )
                }
                adapter.notifyDataSetChanged()
            }

        })
    }
    private fun getDataFromOnline() {
        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.setMessage("Please wait..")
        mProgressDialog.show()

        val responseData=ArrayList<TrendingRepoListItem>()
        adapter.setReposList(responseData,{ selectedRepo: TrendingRepoListItem ->
            onSelectedRepo(
                selectedRepo
            )
        })
        adapter.notifyDataSetChanged()
        val instance=object : onCompleteFetching {
            override fun onError(error: String) {
                Log.e("Error",error)
                mainBinding.errorTextView.text=error
                clearRecyclerView()
                mProgressDialog.dismiss()
            }
            override fun onSuccess(data: ArrayList<TrendingRepoListItem>) {
                if (data != null) {
                    if(data.size==0){
                        showToastMessage();
                    }
                    else if (data.size >= 1) {
                        //Showing search bar if  data is present
                        mainBinding.toolbar.menu.findItem(R.id.app_bar_search).isVisible = true
                    }
                    repoCacheData.clear()
                    repoCacheData.addAll(data)
                    adapter.setReposList(data, { selectedRepo: TrendingRepoListItem ->
                        onSelectedRepo(
                            selectedRepo
                        )
                    })
                    adapter.notifyDataSetChanged()
                    mProgressDialog.dismiss()
                }else{
                    mProgressDialog.dismiss()
                }
            }

        }
        //Fetching Data from ViewModel
        RepoViewModel.getAllRepository(mainBinding.lifecycleOwner as MainActivity,instance)
    }

    private fun showToastMessage() {
        Toast.makeText(this, "No Data is Present", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun reloadData(view: View) {
        if(InternetConnection().isOnline(this)){
            mainBinding.mainScreen.visibility=View.VISIBLE
            mainBinding.internetOffScreen.visibility=View.GONE
            checkConnectivity()

        }
    }
    //Getting Selected Data from RecyclerView
    fun onSelectedRepo(trendingRepoListItem: TrendingRepoListItem){
        val gson = Gson()
        val myJson = gson.toJson(trendingRepoListItem)
        intent=Intent(this,ViewDetailsActivity::class.java)
        intent.putExtra("repoData",myJson)
        startActivity(intent)
    }
}



