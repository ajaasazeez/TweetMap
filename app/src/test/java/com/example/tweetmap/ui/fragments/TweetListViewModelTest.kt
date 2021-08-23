package com.example.tweetmap.ui.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tweetmap.MainCoroutineRule
import com.example.tweetmap.getOrAwaitValueTest
import com.example.tweetmap.repository.FakeDataRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TweetListViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: TweetListViewModel
    private val fakeDataRepository = FakeDataRepository()

    @Before
    fun setup() {
        viewModel = TweetListViewModel(fakeDataRepository)
    }

    @Test
    fun `post rule returns success`() {
        fakeDataRepository.setShouldReturnNetworkError(false)
        viewModel.postRules("hi")
        val value = viewModel.ruleResponseLiveData.getOrAwaitValueTest()
        assertThat(value.data).isNotNull()
    }

    @Test
    fun `post rule returns error`() {
        fakeDataRepository.setShouldReturnNetworkError(true)
        viewModel.postRules("hey")
        val value = viewModel.ruleResponseLiveData.getOrAwaitValueTest()
        assertThat(value.errorCode).isNotNull()
    }
}