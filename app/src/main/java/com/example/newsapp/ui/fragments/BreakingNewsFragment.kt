package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.Constants.Companion.BUNDLE_ARTICLE
import com.example.newsapp.R
import com.example.newsapp.ui.activity.NewsActivity
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.util.Resource
import com.example.newsapp.viewmodel.NewsViewModel

class BreakingNewsFragment : Fragment() {
    private var binding: FragmentBreakingNewsBinding? = null
    private lateinit var viewModel: NewsViewModel
    private var newsAdapter: NewsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
        initObserver()
        initListener()
    }

    private fun initListener() {
        newsAdapter?.setOnclickItemArticle { article ->
            val bundle = Bundle().apply {
                putSerializable(BUNDLE_ARTICLE, article)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_wedViewArticleFragment,
                bundle
            )
        }
    }

    private fun initView() {
        setupRcv()
    }

    private fun setupRcv() {
        newsAdapter = NewsAdapter()
        binding?.rcvBreakingNew?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun initObserver() {

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data.let { newsResponseModel ->
                        newsAdapter?.differ?.submitList(newsResponseModel?.articleModels)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message.let { mess ->
                        Log.d("ERROR IN BREAKING NEW", mess.toString())
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })

    }

    private fun hideProgressBar() {
        binding?.paginationProgressBar?.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding?.paginationProgressBar?.visibility = View.VISIBLE
    }


    private fun initViewModel() {
        viewModel = (activity as NewsActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding?.root
    }
}