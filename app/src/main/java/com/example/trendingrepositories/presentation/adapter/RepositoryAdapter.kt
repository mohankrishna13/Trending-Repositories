package com.example.trendingrepositories.presentation.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.trendingrepositories.R
import com.example.trendingrepositories.data.model.BuiltByConverters
import com.example.trendingrepositories.data.model.TrendingRepoListItem
import com.example.trendingrepositories.databinding.ListItemBinding
import java.util.*
import kotlin.collections.ArrayList


class RepositoryAdapter(): RecyclerView.Adapter<MyViewHolder>(),Filterable {
    private var reposList = ArrayList<TrendingRepoListItem>()
    var copyRepoList=ArrayList<TrendingRepoListItem>()
    private lateinit var listener : (trendingRepo:TrendingRepoListItem) -> Unit
    fun setReposList(
        repos: ArrayList<TrendingRepoListItem>,
        function: (TrendingRepoListItem) -> Unit
    ){
        reposList.clear()
        reposList.addAll(repos)
        listener=function
        copyRepoList=reposList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : ListItemBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.list_item,
            parent,
            false
        )
        return MyViewHolder(binding,listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(reposList[position])

    }

    override fun getItemCount(): Int {
        return reposList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
               val charSearch = constraint.toString()
                if (!charSearch.isEmpty()) {
                    val resultList = ArrayList<TrendingRepoListItem>()
                    for (row in copyRepoList) {
                        if (row.name.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    reposList = resultList
                }else{
                    reposList=reposList
                }
                val filterResults = FilterResults()
                filterResults.values = reposList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                reposList = results?.values as ArrayList<TrendingRepoListItem>
                notifyDataSetChanged()
            }

        }
    }
}


class MyViewHolder(
    val binding: ListItemBinding,
    val listener: (trendingRepo: TrendingRepoListItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(reposData:TrendingRepoListItem){
        binding.descriptionTextView.text=reposData.description
        binding.titleTextView.text=reposData.name
        binding.languageTextView.text=reposData.language
        binding.starsTextView.text= reposData.stars.toString()
        val gradientDrawable = (binding.langColor.getBackground() as GradientDrawable).mutate()
        (gradientDrawable as GradientDrawable).setColor(Color.parseColor(reposData.languageColor))

        binding.cardView.setOnClickListener(View.OnClickListener {
            listener(reposData)
        })
    }


}