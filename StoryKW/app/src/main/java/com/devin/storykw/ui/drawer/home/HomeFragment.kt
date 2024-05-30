    package com.devin.storykw.ui.drawer.home

    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.viewModels
    import androidx.lifecycle.lifecycleScope
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.devin.storykw.databinding.FragmentHomeBinding
    import com.devin.storykw.ui.ViewModelFactory
    import kotlinx.coroutines.flow.collectLatest
    import kotlinx.coroutines.launch

    class HomeFragment : Fragment() {
        private var _binding: FragmentHomeBinding? = null
        private val binding get() = _binding!!

        private val storyViewModel: StoryViewModel by viewModels<StoryViewModel> {
            ViewModelFactory.getInstance(activity?.applicationContext!!)
        }
        private lateinit var storyAdapter: StoryPagingAdapter

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            _binding = FragmentHomeBinding.inflate(inflater, container, false)
            val root: View = binding.root

            setupRecyclerView()
            loadStories()

            return root
        }

        private fun setupRecyclerView() {
            storyAdapter = StoryPagingAdapter()
            binding.storylist.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = storyAdapter
            }
        }

        private fun loadStories() {
            storyViewModel.stories.observe(viewLifecycleOwner) {
                storyAdapter.submitData(lifecycle, it)
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

        override fun onResume() {
            super.onResume()
            refreshData()
        }

        private fun refreshData() {
            storyViewModel.refresh()
            loadStories()
        }
    }