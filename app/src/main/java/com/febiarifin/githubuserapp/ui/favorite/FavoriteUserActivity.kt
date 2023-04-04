package com.febiarifin.githubuserapp.ui.favorite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.febiarifin.githubuserapp.database.AppDatabase
import com.febiarifin.githubuserapp.database.FavoriteUser
import com.febiarifin.githubuserapp.databinding.ActivityFavoriteUserBinding
import com.febiarifin.githubuserapp.ui.FavoriteUserAdapter
import com.febiarifin.githubuserapp.ui.detail.DetailUserActivity

class FavoriteUserActivity : AppCompatActivity() {

    private var _activityFavoriteUserBinding: ActivityFavoriteUserBinding? = null
    private val binding get() = _activityFavoriteUserBinding
    private lateinit var adapter: FavoriteUserAdapter
    private lateinit var database: AppDatabase
    private var listFavoriteUser = mutableListOf<FavoriteUser>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityFavoriteUserBinding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        database = AppDatabase.getInstance(applicationContext)
        adapter = FavoriteUserAdapter(listFavoriteUser)

        adapter.setOnItemClickCallback(object : FavoriteUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: FavoriteUser) {
                Intent(this@FavoriteUserActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.username)
                    startActivity(it)
                }
            }
        })

        binding?.apply {
            rvFavoriteUser?.layoutManager = LinearLayoutManager(applicationContext)
            rvFavoriteUser?.setHasFixedSize(true)
            rvFavoriteUser?.adapter = adapter
        }

        val actionBar = supportActionBar
        actionBar!!.title = "Favorite User"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        showLoading(true)
        Handler().postDelayed({
            showLoading(false)
            getData()
        }, 500)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData() {
        listFavoriteUser.clear()
        listFavoriteUser.addAll(database.favoriteUserDao().getAll())
        adapter.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(isLoading: Boolean) = if (isLoading) binding?.progressbar?.visibility = View.VISIBLE else binding?.progressbar?.visibility = View.GONE
}