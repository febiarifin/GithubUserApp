package com.febiarifin.githubuserapp.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.febiarifin.githubuserapp.ui.detail.DetailUserActivity
import com.febiarifin.githubuserapp.R
import com.febiarifin.githubuserapp.databinding.ActivityMainBinding
import com.febiarifin.githubuserapp.model.User
import com.febiarifin.githubuserapp.ui.GithubUserAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: GithubUserAdapter
    private lateinit var viewModel: MainViewModel

    companion object{
        private const val DEFAULT_USER = "febi"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = GithubUserAdapter()
        adapter.notifyDataSetChanged()
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        adapter.setOnItemClickCallback(object : GithubUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    startActivity(it)
                }
            }

        })

        binding.apply {
            rvGithubUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvGithubUser.setHasFixedSize(true)
            rvGithubUser.adapter = adapter
        }

        viewModel.searchState.observe(this, {
            if (it != true){
                viewModel.setSearchUsers(DEFAULT_USER)
            }
        })

        viewModel.getSearchUsers().observe(this, {
            if(it != null){
                adapter.setList(it)
            }
        })

        viewModel.isLoading.observe(this,{
            showLoading(it)
        })

        viewModel.message.observe(this,{
            Toast.makeText(this , it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun showLoading(isLoading: Boolean) = if (isLoading) binding.progressbar.visibility = View.VISIBLE else binding.progressbar.visibility = View.GONE

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.setSearchUsers(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }
}