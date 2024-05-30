    package com.devin.githubkw.ui.detail

    import android.os.Bundle
    import android.view.View
    import androidx.appcompat.app.AppCompatActivity
    import androidx.lifecycle.ViewModelProvider
    import com.bumptech.glide.Glide
    import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
    import com.devin.githubkw.R
    import com.devin.githubkw.databinding.ActivityDetailUserBinding
    import com.google.android.material.tabs.TabLayoutMediator
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.withContext

    class DetailUser : AppCompatActivity() {
        private lateinit var binding: ActivityDetailUserBinding
        private lateinit var viewModel: DetailUserViewModel
        companion object{
            const val EXTRA_USERNAME = "extra_username"
            const val EXTRA_ID = "extra_id"
            const val EXTRA_URL = "extra_url"
            private val TAB_TITLES = intArrayOf(R.string.tab1, R.string.tab2)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityDetailUserBinding.inflate(layoutInflater)
            setContentView(binding.root)
            binding.loading.visibility = View.VISIBLE

            val username = intent.getStringExtra(EXTRA_USERNAME)
            val id = intent.getIntExtra(EXTRA_ID, 0)
            val avatarURl = intent.getStringExtra(EXTRA_URL)
            val bundle = Bundle()
            bundle.putString(EXTRA_USERNAME, username)

            viewModel = ViewModelProvider(this)[DetailUserViewModel::class.java]
            viewModel.setUserDetail(username.toString())
            viewModel.getUserDetail().observe(this) {
                if (it != null) {
                    binding.apply {
                        tvName.text = it.name
                        tvUsername.text = it.login
                        tvFollowers.text = "${it.followers} Followers"
                        tvFollowing.text = "${it.following} Following"
                        Glide.with(this@DetailUser)
                            .load(it.avatar_url)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .centerCrop()
                            .into(ivProfile)
                        loading.visibility = View.GONE
                    }
                }
            }

            var _isChecked = false
            CoroutineScope(Dispatchers.IO).launch {
                val count = viewModel.checkUser(id)
                withContext(Dispatchers.Main){
                    if (count != null){
                        if (count > 0){
                            binding.toggleFavourite.isChecked = true
                            _isChecked = true
                        }
                        else{
                            binding.toggleFavourite.isChecked = false
                            _isChecked = false
                        }
                    }
                }
            }

            binding.toggleFavourite.setOnClickListener {
                _isChecked = !_isChecked
                if (username != null) {
                    if (_isChecked) {
                        viewModel.addFavourite(username, id, avatarURl.toString())
                    } else {
                        viewModel.removeFromFavorite(id)
                    }
                }
                binding.toggleFavourite.isChecked = _isChecked
            }

            val followSectionAdapterAdapter = FollowSectionAdapter(this, username ?: "")
            binding.apply {
                listfollow.adapter = followSectionAdapterAdapter
                TabLayoutMediator(tabs, listfollow) { tab, position ->
                    tab.text = resources.getString(TAB_TITLES[position])
                }.attach()
            }
        }
    }