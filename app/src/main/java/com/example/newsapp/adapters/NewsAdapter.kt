package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.LayoutItemArticlePreviewBinding
import com.example.newsapp.model.ArticleModel


class NewsAdapter :
    RecyclerView.Adapter<NewsAdapter.ArticleItemViewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<ArticleModel>() {
        override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem == newItem
        }

    }

    private var onClickItemListener: ((ArticleModel) -> Unit)? = null

    val differ = AsyncListDiffer(this, differCallBack)

    fun setOnclickItemArticle(listener: (ArticleModel) -> Unit) {
        onClickItemListener = listener
    }


    inner class ArticleItemViewHolder(private val binding: LayoutItemArticlePreviewBinding) :
        ViewHolder(binding.root) {
        fun bindData(data: ArticleModel) {
            binding.apply {
                Glide.with(this.root).load(data.urlToImage).into(imgViewArticle)
                tvTitle.text = data.title ?: ""
                tvSource.text = data.sourceModel.name ?: ""
                tvContent.text = data.description ?: ""
                tvDate.text = data.publishedAt ?: ""
                root.setOnClickListener {
                    onClickItemListener?.invoke(data)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleItemViewHolder {
        val binding = LayoutItemArticlePreviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticleItemViewHolder(binding)

    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ArticleItemViewHolder, position: Int) {
        val data = differ.currentList[position]
        holder.bindData(data)
    }
}