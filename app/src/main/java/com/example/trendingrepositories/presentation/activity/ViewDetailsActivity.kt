package com.example.trendingrepositories.presentation.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.trendingrepositories.data.model.TrendingRepoListItem
import com.example.trendingrepositories.R
import com.example.trendingrepositories.databinding.ActivityViewDetailsBinding
import com.google.gson.Gson

class ViewDetailsActivity : AppCompatActivity() {
    private lateinit var viewActivityMainBinding: ActivityViewDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewActivityMainBinding=DataBindingUtil.setContentView(this, R.layout.activity_view_details)
        val gson = Gson()
        val repoDataFromInent = gson.fromJson<TrendingRepoListItem>(intent.getStringExtra("repoData"), TrendingRepoListItem::class.java)

        setSupportActionBar(viewActivityMainBinding.toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        viewActivityMainBinding.toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })

        viewActivityMainBinding.lifecycleOwner=this

        Glide.with(this)
            .load(repoDataFromInent.avatar)
            .placeholder(R.drawable.loading)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(viewActivityMainBinding.profileImage)


        viewActivityMainBinding.repoAuthor.text=repoDataFromInent.author
        viewActivityMainBinding.repoName.text=repoDataFromInent.name
        viewActivityMainBinding.repoCurrentPeriodStars.text=repoDataFromInent.currentPeriodStars.toString()
        viewActivityMainBinding.repoDescription.text=repoDataFromInent.description
        viewActivityMainBinding.repoForks.text=repoDataFromInent.forks.toString()
        viewActivityMainBinding.repoLanguage.text=repoDataFromInent.language

        viewActivityMainBinding.repoUrl.movementMethod= LinkMovementMethod.getInstance()
        viewActivityMainBinding.repoUrl.text=Html.fromHtml(repoDataFromInent.url)

        viewActivityMainBinding.repoBuilt.text=repoDataFromInent.builtBy.get(0).username
        viewActivityMainBinding.repoLink.movementMethod= LinkMovementMethod.getInstance()
        viewActivityMainBinding.repoLink.text=Html.fromHtml(repoDataFromInent.builtBy.get(0).href)

        Glide.with(this)
            .load(repoDataFromInent.builtBy.get(0).avatar)
            .placeholder(R.drawable.loading)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(viewActivityMainBinding.builtImage)



    }
}