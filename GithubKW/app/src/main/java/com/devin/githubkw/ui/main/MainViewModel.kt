package com.devin.githubkw.ui.main

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devin.githubkw.backend.RetrofitClient
import com.devin.githubkw.data.connect.ConnGithubAPI
import com.devin.githubkw.data.connect.Userlist
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    val listUsers = MutableLiveData<ArrayList<Userlist>>()


    fun setSearchUsers(query: String, context: Context){
        RetrofitClient.apiGithub.getSearchUsers(query).enqueue(object: Callback<ConnGithubAPI>{
            override fun onResponse(call: Call<ConnGithubAPI>, response: Response<ConnGithubAPI>) {
                if (response.isSuccessful){
                    listUsers.postValue(response.body()?.items)
                }
            }

            override fun onFailure(call: Call<ConnGithubAPI>, t: Throwable) {
                Toast.makeText(context, "Failure: " + t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getSearchUsers(): LiveData<ArrayList<Userlist>>{
        return listUsers
    }
}