package com.devin.githubkw.ui.favourite

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.devin.githubkw.data.connect.Userlist
import com.devin.githubkw.data.local.FavouriteUser
import com.devin.githubkw.databinding.ActivityFavoriteBinding
import com.devin.githubkw.ui.detail.DetailUser
import com.devin.githubkw.ui.main.UserAdapter

class FavouriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: FavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[FavouriteViewModel::class.java]
        adapter = UserAdapter(ArrayList())

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Userlist) {
                Intent(this@FavouriteActivity, DetailUser::class.java).also {
                    it.putExtra(DetailUser.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUser.EXTRA_ID, data.id)
                    it.putExtra(DetailUser.EXTRA_URL, data.avatar_url)
                    startActivity(it)
                }
            }
        })

        binding.apply {
            rvUser.setHasFixedSize(true)
            rvUser.layoutManager = LinearLayoutManager(this@FavouriteActivity)
            rvUser.adapter = adapter
        }

        viewModel.getFavoriteuser()?.observe(this) { users ->
            users?.let {
                val list = mapList(it)
                adapter.setList(list)
            }
        }
    }

    private fun mapList(users: List<FavouriteUser>): ArrayList<Userlist> {
        val listUsers = ArrayList<Userlist>()
        for (user in users) {
            val userMapped = Userlist(
                user.login,
                user.id,
                user.avatar_url
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }
}
