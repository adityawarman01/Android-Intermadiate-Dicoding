package com.example.submissionintermediate.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionintermediate.database.response.ListStoryItem
import com.example.submissionintermediate.databinding.ItemRowStoryBinding
import com.example.submissionintermediate.view.detail.DetailActivity

class AdapterStory(private val listStory: List<ListStoryItem>):
RecyclerView.Adapter<AdapterStory.ListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
       val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listStories = listStory[position]
        holder.bind(listStories)
    }

    override fun getItemCount(): Int = listStory.size

    class ListViewHolder(private val binding: ItemRowStoryBinding):
            RecyclerView.ViewHolder(binding.root){
                fun bind(story: ListStoryItem){
                    Glide.with(binding.root.context)
                        .load(story.photoUrl)
                        .into(binding.storyPic)
                    binding.titleStory.text = story.name
                    binding.descriptionStory.text = story.description

                    itemView.setOnClickListener {
                        val idStory = story.id
                        val goToDetail = Intent(binding.root.context, DetailActivity::class.java)
                        goToDetail.putExtra(DetailActivity.STORY_ID, idStory)
                        binding.root.context.startActivity(goToDetail)
                    itemView.context.startActivity(goToDetail, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
                    }
                }

    }

}