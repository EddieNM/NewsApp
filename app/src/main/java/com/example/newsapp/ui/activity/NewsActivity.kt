package com.example.newsapp.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityNewsBinding
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelProviderFactory

class NewsActivity : AppCompatActivity() {
    private var binding: ActivityNewsBinding? = null
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DEV", " run onCreate")
        enableEdgeToEdge()
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]
    }

    private fun initView() {
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        binding?.apply {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.flContainerView) as NavHostFragment
            val navController = navHostFragment.navController
            bottomNavigationView.setupWithNavController(navController)
            bottomNavigationView.setOnApplyWindowInsetsListener(null)
        }
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("DEV", " run onReStart")
    }

    override fun onStart() {
        super.onStart()
        Log.d("DEV", " run onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("DEV", " run onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("DEV", " run onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("DEV", " run onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("DEV", " run onDestroy")
    }
}