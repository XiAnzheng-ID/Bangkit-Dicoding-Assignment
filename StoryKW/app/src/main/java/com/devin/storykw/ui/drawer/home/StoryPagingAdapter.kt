package com.devin.storykw.ui.drawer.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devin.storykw.backend.conn.ListStoryItem
import com.devin.storykw.databinding.ItemStoryBinding
import com.devin.storykw.ui.detail.DetailStory

class StoryPagingAdapter : PagingDataAdapter<ListStoryItem, StoryPagingAdapter.ViewHolder>(StoryDiffCallback()) {

    inner class ViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: ListStoryItem) {
            binding.nama.text = storyItem.name
            binding.description.text = storyItem.description
            Glide.with(binding.root)
                .load(storyItem.photoUrl)
                .into(binding.banner)
        }

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val storyItem = getItem(position)
                    val intent = Intent(it.context, DetailStory::class.java).apply {
                        putExtra("STORY_ID", storyItem?.id)
                    }
                    it.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val storyItem = getItem(position)
        storyItem?.let { holder.bind(it) }
    }

    class StoryDiffCallback : DiffUtil.ItemCallback<ListStoryItem>() {
        override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
            return oldItem == newItem
        }
    }
}