package com.devin.storykw.testing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.devin.storykw.backend.UserRepository
import com.devin.storykw.backend.conn.ListStoryItem
import com.devin.storykw.ui.drawer.home.StoryPagingAdapter
import com.devin.storykw.ui.drawer.home.StoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    var executorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = CoroutineTest()

    @Mock
    private lateinit var storyRepository: UserRepository

    @Before
    fun setup() {
        // No need to initialize storyRepository here, as it will be injected by Mockito
    }

    @Test
    fun `when get story should not be null and return right data`() = runTest {
        val dummyStoryItem = Data.genDummyListOfStory()
        val dummyData = PagingTest.snapshot(dummyStoryItem)
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = dummyData

        `when`(storyRepository.stories()).thenReturn(expectedStory)

        val storyViewModel = StoryViewModel(storyRepository)
        val actualStory: PagingData<ListStoryItem> = storyViewModel.stories.getOrAwaitValue()

        val storyDiffCallback = StoryPagingAdapter.StoryDiffCallback()

        val differ = AsyncPagingDataDiffer(
            diffCallback = storyDiffCallback,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStoryItem.size, differ.snapshot().size)
        Assert.assertEquals(dummyStoryItem[0], differ.snapshot()[0])
    }

    @Test
    fun `when get empty story should return no data`() = runTest {
        val dummyEmptyData: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = dummyEmptyData

        `when`(storyRepository.stories()).thenReturn(expectedStory)

        val storyViewModel = StoryViewModel(storyRepository)
        val actualStory = storyViewModel.stories.getOrAwaitValue()

        val storyDiffCallback = StoryPagingAdapter.StoryDiffCallback()

        val differ = AsyncPagingDataDiffer(
            diffCallback = storyDiffCallback,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStory)

        Assert.assertEquals(0, differ.snapshot().size)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}