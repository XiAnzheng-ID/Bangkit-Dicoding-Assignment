package com.devin.storykw.ui.drawer.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.devin.storykw.backend.RetrofitClient
import com.devin.storykw.backend.conn.ListStoryItem

class StoryPagingSource : PagingSource<Int, ListStoryItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: 1
            val response = RetrofitClient.apiStory.getStoriesWithLoc(page = position, size = params.loadSize)
            val stories = response.listStory?.filterNotNull() ?: emptyList()

            LoadResult.Page(
                data = stories,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (stories.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}