package com.example.newsapp.model

import com.google.gson.annotations.SerializedName

data class NewsResponseModel(
    @SerializedName("articles")
    val articleModels: List<ArticleModel>,
    val status: String,
    val totalResults: Int
)