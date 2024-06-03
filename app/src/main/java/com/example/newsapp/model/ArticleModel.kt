package com.example.newsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "articles")
data class ArticleModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String? = null,
    val content: String? = null,
    val description: String? = null,
    val publishedAt: String? = null,
    @SerializedName("source")
    val sourceModel: SourceModel,
    val title: String? = null,
    val url: String? = null,
    val urlToImage: String? = null
) : Serializable