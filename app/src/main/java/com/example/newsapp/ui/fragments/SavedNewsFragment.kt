package com.example.newsapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.Constants
import com.example.newsapp.Constants.Companion.BUNDLE_ARTICLE
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.ui.activity.NewsActivity
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SavedNewsFragment : Fragment() {

    private var binding: FragmentSavedNewsBinding? = null
    private lateinit var viewModel: NewsViewModel
    private var savedNewsAdapter: NewsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRcv()
        initListener()
        initObserver()
    }

    private fun initObserver() {
        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles ->
            savedNewsAdapter?.differ?.submitList(articles)
        })
    }

    private fun initListener() {
        savedNewsAdapter?.setOnclickItemArticle { article ->
            val bundle = Bundle().apply {
                putSerializable(BUNDLE_ARTICLE, article)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_wedViewArticleFragment,
                bundle
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding?.root
    }


    private fun setupRcv() {
        savedNewsAdapter = NewsAdapter()
        setUpItemHelper()
        binding?.rcvSaved?.apply {
            adapter = savedNewsAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }

    private fun setUpItemHelper() {
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            @SuppressLint("ShowToast")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val indexDelete = viewHolder.adapterPosition
                val article = savedNewsAdapter?.differ?.currentList?.get(indexDelete)
                if (article != null) {
                    viewModel.deleteArticle(article)
                    view?.let {
                        Snackbar.make(it, "Successfully deleted article", Snackbar.LENGTH_LONG)
                            .apply {
                                setAction("Undo") {
                                    viewModel.saveArticle(article)
                                    if (indexDelete == 0) {
                                        lifecycleScope.launch {
                                            delay(100)
                                            binding?.rcvSaved?.scrollToPosition(indexDelete)

                                        }
                                    }
                                }
                                show()
                            }
                    }
                }
            }

        }
        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(binding?.rcvSaved)
        }
    }
}