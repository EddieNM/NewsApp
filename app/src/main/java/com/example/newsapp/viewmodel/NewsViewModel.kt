package com.example.newsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.ArticleModel
import com.example.newsapp.model.NewsResponseModel
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponseModel>> = MutableLiveData()
    private var breakingNewsPage = 1

    val searchNews: MutableLiveData<Resource<NewsResponseModel>> = MutableLiveData()
    private var searchNewsPage = 1


    init {
        getBreakingNews("us")
    }

    private fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(keySearch: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchArticle(keySearch, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))

    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponseModel>): Resource<NewsResponseModel> {

        if (response.isSuccessful) {
            response.body()?.let { resultRes ->
                return Resource.Success(resultRes)
            }
        }
        return Resource.Error(response.message())

    }

    private fun handleSearchNewsResponse(response: Response<NewsResponseModel>): Resource<NewsResponseModel> {

        if (response.isSuccessful) {
            response.body()?.let { resultRes ->
                return Resource.Success(resultRes)
            }
        }
        return Resource.Error(response.message())

    }

    fun saveArticle(article: ArticleModel) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: ArticleModel) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
}
