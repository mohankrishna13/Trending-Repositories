package com.example.trendingrepositories.presentation.activity

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
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

        //Getting Data From Intent
        val gson = Gson()
        val repoDataFromInent = gson.fromJson<TrendingRepoListItem>(intent.getStringExtra("repoData"), TrendingRepoListItem::class.java)

        setSupportActionBar(viewActivityMainBinding.toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        //OnClick Listener for Back Button
        viewActivityMainBinding.toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })

        viewActivityMainBinding.lifecycleOwner=this

        //Loading Image Using Glide
        Glide.with(this)
            .load(repoDataFromInent.avatar)
            .placeholder(R.drawable.loading)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(viewActivityMainBinding.profileImage)

        //Inserting author name
        viewActivityMainBinding.repoAuthor.text=repoDataFromInent.author

        //Inserting Repository name
        viewActivityMainBinding.repoName.text=repoDataFromInent.name

        //Inserting Repository Current Period Stars
        viewActivityMainBinding.repoCurrentPeriodStars.text=repoDataFromInent.currentPeriodStars.toString()

        //Inserting Repository Description
        viewActivityMainBinding.repoDescription.text=repoDataFromInent.description

        //Inserting Forks
        viewActivityMainBinding.repoForks.text=repoDataFromInent.forks.toString()

        //Inserting Language
        viewActivityMainBinding.repoLanguage.text=repoDataFromInent.language

        //Creating HyperLink for repository url
        viewActivityMainBinding.repoUrl.movementMethod= LinkMovementMethod.getInstance()
        viewActivityMainBinding.repoUrl.text=Html.fromHtml(repoDataFromInent.url)

        //Inserting Name who built
        viewActivityMainBinding.repoBuilt.text=repoDataFromInent.builtBy.get(0).username

        //Creating HyperLink for Builder url
        viewActivityMainBinding.repoLink.movementMethod= LinkMovementMethod.getInstance()
        viewActivityMainBinding.repoLink.text=Html.fromHtml(repoDataFromInent.builtBy.get(0).href)

        //Loading Image for

        Glide.with(this)
            .load(repoDataFromInent.builtBy.get(0).avatar)
            .placeholder(R.drawable.loading)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(viewActivityMainBinding.builtImage)



    }
}