package com.devin.githubkw.ui.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FollowSectionAdapter(fragmentActivity: FragmentActivity, private val username: String) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DetailUserFragment.newInstance(username, true)
            1 -> DetailUserFragment.newInstance(username, false)
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }
}