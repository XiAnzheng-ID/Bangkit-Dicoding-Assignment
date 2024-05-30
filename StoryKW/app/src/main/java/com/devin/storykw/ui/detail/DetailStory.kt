package com.devin.storykw.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.devin.storykw.backend.RetrofitClient
import com.devin.storykw.databinding.ActivityDetailStoryBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailStory : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra("STORY_ID") ?: ""
        loadStoryDetail(storyId)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadStoryDetail(storyId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = RetrofitClient.apiStory.getStoryDetail(storyId)
                if (response.error == false) {
                    val story = response.story
                    if (story != null) {
                        binding.namaDetail.text = story.name
                        binding.descDetail.text = story.description
                        Glide.with(this@DetailStory)
                            .load(story.photoUrl)
                            .into(binding.bannerDetail)
                    } else {
                        Toast.makeText(
                            this@DetailStory,
                            "Story detail is null",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@DetailStory,
                        response.message ?: "Failed to load story detail",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@DetailStory,
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}