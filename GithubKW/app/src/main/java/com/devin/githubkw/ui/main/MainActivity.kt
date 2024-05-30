package com.devin.githubkw.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.devin.githubkw.R
import com.devin.githubkw.backend.RetrofitClient
import com.devin.githubkw.data.connect.Userlist
import com.devin.githubkw.data.local.SettingPreferences
import com.devin.githubkw.data.local.dataStore
import com.devin.githubkw.databinding.ActivityMainBinding
import com.devin.githubkw.ui.detail.DetailUser
import com.devin.githubkw.ui.favourite.FavouriteActivity
import com.devin.githubkw.ui.settings.SettingsActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var settingPreferences: SettingPreferences
    private var themeJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        settingPreferences = SettingPreferences.getInstance(applicationContext.dataStore)

        themeJob = lifecycleScope.launch {
            settingPreferences.getThemeSetting().collect { isDarkModeActive ->
                applyTheme(isDarkModeActive)
            }
        }

        adapter = UserAdapter(ArrayList())
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Userlist) {
                Intent(this@MainActivity, DetailUser::class.java).also{
                    it.putExtra(DetailUser.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUser.EXTRA_ID, data.id)
                    it.putExtra(DetailUser.EXTRA_URL, data.avatar_url)
                    startActivity(it)
                }
            }
        })

        getRandomUsers()
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter

            btnSearch.setOnClickListener{
                searchUser()
            }

            search.setOnKeyListener{ _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }

        viewModel.getSearchUsers().observe(this) {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        themeJob?.cancel()
    }

    private fun applyTheme(isDarkModeActive: Boolean) {
        if (isDarkModeActive) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.loading.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun searchUser() {
        binding.apply{
            val query = search.text.toString()
            if (query.isEmpty()) return
            showLoading(true)
            viewModel.setSearchUsers(query, this@MainActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favourite_menu -> {
                Intent(this, FavouriteActivity::class.java).also {
                    startActivity(it)
                }
            }

            R.id.setting -> {
                Intent(this, SettingsActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getRandomUsers() {
        RetrofitClient.apiGithub.getRandomUsers().enqueue(object: Callback<ArrayList<Userlist>> {
            override fun onResponse(call: Call<ArrayList<Userlist>>, response: Response<ArrayList<Userlist>>) {
                if (response.isSuccessful){
                    val randomUsers = response.body() ?: arrayListOf()
                    adapter.setList(randomUsers)
                    showLoading(false)
                }
            }

            override fun onFailure(call: Call<ArrayList<Userlist>>, t: Throwable) {
                val message = if (t.message != null) {
                    "Failure: ${t.message}"
                } else {
                    "Unknown failure"
                }
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}