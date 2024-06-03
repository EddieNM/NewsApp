package com.example.newsapp.repository

import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.model.ArticleModel

class NewsRepository(
    val db: ArticleDatabase
) {

    /* thu  thập dữ liệu từ các nguồn khác nhau có thể là dữ liệu từ local hoặc dữ liệu từ sever
       cung cấp dữ liệu cho viewmodel
        lữu  trữ dữ liệu tạm thời
        quản li trạng thái dữ liệu như loadding, success, error*/

    suspend fun getBreakingNews(country: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(country, pageNumber)

    suspend fun searchArticle(keySearch: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(keySearch, pageNumber)

  suspend  fun upsert(article: ArticleModel) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getALLArticles()

   suspend fun deleteArticle(article: ArticleModel) = db.getArticleDao().deleteArticle(article)

}