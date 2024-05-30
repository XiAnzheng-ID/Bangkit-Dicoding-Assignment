package com.devin.githubkw.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.devin.githubkw.data.connect.Userlist
import com.devin.githubkw.databinding.FragmentFollowBinding
import com.devin.githubkw.ui.main.UserAdapter

class DetailUserFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailUserViewModel
    private lateinit var adapter: UserAdapter
    private var isFollowers: Boolean = true
    companion object {
        private const val ARG_USERNAME = "username"
        private const val ARG_IS_FOLLOWERS = "isFollowers"

        fun newInstance(username: String, isFollowers: Boolean): DetailUserFragment {
            val fragment = DetailUserFragment()
            val args = Bundle()
            args.putString(ARG_USERNAME, username)
            args.putBoolean(ARG_IS_FOLLOWERS, isFollowers)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DetailUserViewModel::class.java]

        arguments?.let { args ->
            val username = args.getString(ARG_USERNAME, "")
            isFollowers = args.getBoolean(ARG_IS_FOLLOWERS, true)

            viewModel.getUserList(username!!, isFollowers).observe(viewLifecycleOwner) { list ->
                if (!list.isNullOrEmpty()) {
                        setupRecyclerView(list)
                        hideLoading()
                }
            }
        }
    }

    private fun setupRecyclerView(userList: List<Userlist>) {
        adapter = UserAdapter(userList)
        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@DetailUserFragment.adapter
        }
    }

    private fun hideLoading() {
        binding.loading.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}