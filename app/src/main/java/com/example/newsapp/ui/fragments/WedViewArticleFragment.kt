package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.FragmentWedViewArticleBinding
import com.example.newsapp.ui.activity.NewsActivity
import com.example.newsapp.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class WedViewArticleFragment : Fragment() {

    private var binding: FragmentWedViewArticleBinding? = null
    private var viewModel: NewsViewModel? = null
    private val args: WedViewArticleFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWedViewArticleBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        handleWedView()

    }

    private fun handleWedView() {
        val article = args.article
        binding?.apply {
            wvArticle.apply {
                webViewClient = WebViewClient()
                loadUrl(article.url ?: "")
            }

            flButtonAdd.setOnClickListener {
                viewModel?.saveArticle(article = article)
                Snackbar.make(root, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}