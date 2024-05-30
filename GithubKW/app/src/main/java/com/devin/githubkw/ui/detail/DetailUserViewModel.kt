package com.devin.githubkw.ui.detail

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devin.githubkw.backend.RetrofitClient
import com.devin.githubkw.data.connect.DetailUser
import com.devin.githubkw.data.connect.Userlist
import com.devin.githubkw.data.local.FavouriteUser
import com.devin.githubkw.data.local.FavouriteUserDao
import com.devin.githubkw.data.local.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {
    val user = MutableLiveData<DetailUser>()
    private var userDao: FavouriteUserDao?
    private var userDb: UserDatabase?
    private val appContext: Application = application
    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun setUserDetail(username: String) {
        RetrofitClient.apiGithub.getUserDetail(username)
            .enqueue(object : Callback<DetailUser> {
                override fun onResponse(call: Call<DetailUser>, response: Response<DetailUser>) {
                    if (response.isSuccessful) {
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailUser>, t: Throwable) {
                    showToast("Failed: ${t.message}")
                }

            })

    }

    fun getUserDetail(): LiveData<DetailUser> {
        return user
    }

    fun addFavourite(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavouriteUser(
                username,
                id,
                avatarUrl
            )

            userDao?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)
    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(id)
        }
    }

    fun getUserList(username: String, isFollowers: Boolean): LiveData<ArrayList<Userlist>> {
        val listData = MutableLiveData<ArrayList<Userlist>>()
        val call = if (isFollowers) RetrofitClient.apiGithub.getFollowers(username)
        else RetrofitClient.apiGithub.getFollowing(username)

        call.enqueue(object : Callback<ArrayList<Userlist>> {
            override fun onResponse(
                call: Call<ArrayList<Userlist>>,
                response: Response<ArrayList<Userlist>>
            ) {
                if (response.isSuccessful) {
                    listData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ArrayList<Userlist>>, t: Throwable) {
                showToast("Failed: ${t.message}")
            }
        })

        return listData
    }

    private fun showToast(message: String) {
        Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()
    }
}