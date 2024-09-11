package com.example.submissionintermediate.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.submissionintermediate.adapter.AdapterStory
import com.example.submissionintermediate.database.repository.StoryRepo
import com.example.submissionintermediate.database.response.ListStoryItem
import com.example.submissionintermediate.utils.DataDummy
import com.example.submissionintermediate.view.login.LoginViewModel
import com.example.submissionintermediate.view.main.MainViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.submissionintermediate.database.repository.ResultState
import com.example.submissionintermediate.database.response.ListStoryResponse
import com.example.submissionintermediate.utils.MainDispatcherRule
import com.example.submissionintermediate.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.internal.junit.NoOpTestListener

@ExperimentalStdlibApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepo: StoryRepo
    private lateinit var mainViewModel: MainViewModel

//    @Before
//    fun setUp() {
//        mainViewModel = MainViewModel(storyRepo)
//    }

    @Test
    fun `when Get Data Should Not Null and Return Succsess`() = runBlocking {
        val dummyMain = DataDummy.generateDummyMVModel()
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapShot(dummyMain)
        val expectedNews = MutableLiveData<PagingData<ListStoryItem>>()
        expectedNews.value = data

        `when`(storyRepo.getStory()).thenReturn(expectedNews)

        val mainViewModels = MainViewModel(storyRepo)
        val actualNews : PagingData<ListStoryItem> = mainViewModels.lisStory.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = AdapterStory.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualNews)
        Mockito.verify(storyRepo).getStory()

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyMain.size, differ.snapshot().size)
        Assert.assertEquals(dummyMain[0], differ.snapshot()[0])

    }


    @Test
    fun `when Network Error Should Return Error`() = runTest {
        val dataData: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val headlineNews = MutableLiveData<PagingData<ListStoryItem>>()
        headlineNews.value = dataData

        `when`(storyRepo.getStory()).thenReturn(headlineNews)

        val mainViewModels = MainViewModel(storyRepo)
        val actualNews : PagingData<ListStoryItem> = mainViewModels.lisStory.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = AdapterStory.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualNews)
        Mockito.verify(storyRepo).getStory()
        Assert.assertEquals(0, differ.snapshot().size)

    }

}
class StoryPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {

    companion object {
        fun snapShot(Items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(Items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}
    val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {
            TODO("Not yet implemented")
        }

        override fun onInserted(position: Int, count: Int) {

        }

        override fun onRemoved(position: Int, count: Int) {
            TODO("Not yet implemented")
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            TODO("Not yet implemented")
        }
    }

