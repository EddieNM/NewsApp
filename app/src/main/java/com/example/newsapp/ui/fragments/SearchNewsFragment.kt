package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.Constants
import com.example.newsapp.Constants.Companion.BUNDLE_ARTICLE
import com.example.newsapp.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.ui.activity.NewsActivity
import com.example.newsapp.databinding.FragmentSearchNewsBinding
import com.example.newsapp.util.Resource
import com.example.newsapp.viewmodel.NewsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment() {

    private var binding: FragmentSearchNewsBinding? = null
    lateinit var viewModel: NewsViewModel
    private var searchAdapter: NewsAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        handleSearchView()
        viewModel = (activity as NewsActivity).viewModel
        initObserver()
        initListener()
    }

    private fun handleSearchView() {
        var job: Job? = null
        binding?.searchView?.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }
    }

    private fun initListener() {
        searchAdapter?.setOnclickItemArticle { article ->
            val bundle = Bundle().apply {
                putSerializable(BUNDLE_ARTICLE, article)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_wedViewArticleFragment,
                bundle
            )
        }
    }

    private fun initObserver() {
        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data.let { newsResponseModel ->
                        searchAdapter?.differ?.submitList(newsResponseModel?.articleModels)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message.let { mess ->
                        Log.d("ERROR IN SEARCHING NEW", mess.toString())
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })


    }

    private fun initView() {
        setUpRCV()
    }

    private fun setUpRCV() {
        searchAdapter = NewsAdapter()
        binding?.rcvSearchView?.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar() {
        binding?.paginationProgressBar?.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding?.paginationProgressBar?.visibility = View.VISIBLE
    }

}