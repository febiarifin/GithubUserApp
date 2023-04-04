package com.febiarifin.githubuserapp.ui.detail

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.febiarifin.githubuserapp.R
import com.febiarifin.githubuserapp.database.AppDatabase
import com.febiarifin.githubuserapp.database.FavoriteUser
import com.febiarifin.githubuserapp.databinding.ActivityDetailUserBinding

class DetailUserActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_USERNAME = "extra_username"
        const val MESSAGE_SUCCES_ADD_TO_FAVORITE = "Ditambahkan ke favorit"
        const val MESSAGE_SUCCES_REMOVE_TO_FAVORITE = "Dihapus dari favorit"
        const val TYPE_ADD_TO_FAVORITE = "type_add_to_favorite"
        const val TYPE_REMOVE_FROM_FAVORITE = "type_remove_from_favorite"
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getInstance(applicationContext)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        var avatar_url: String? = null

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailUserViewModel::class.java)

        viewModel.setUserDetail(username.toString())
        viewModel.getUserDetail().observe(this, {
            if (it != null){
                binding.apply {
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvFollowers.text = "${it.followers} Followers"
                    tvFollowing.text = "${it.following} Following"
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatar_url)
                        .into(ivProfile)
                }
                avatar_url = it.avatar_url
            }
        })

        viewModel.isLoading.observe(this, {
            showLoading(it)
        })

        viewModel.message.observe(this, {
            Toast.makeText(this , it, Toast.LENGTH_SHORT).show()
        })

        val sectionPagerAdapter = SectionPagerAdapter(this,  supportFragmentManager, bundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            tabs.setupWithViewPager(viewPager)
        }

        val actionBar = supportActionBar
        actionBar!!.title = "Detail User"
        actionBar.setDisplayHomeAsUpEnabled(true)

        if(!isUsingNightModeResources()){
            binding.apply {
                tvFollowers.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_people_alt_24_dark, 0, 0, 0)
                tvFollowing.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_people_alt_24_dark, 0, 0, 0)
            }
        }

        val favoriteUser = database.favoriteUserDao().findByUsername(username.toString())
        if (favoriteUser == null) {
            showFavoriteOrClear(TYPE_ADD_TO_FAVORITE)
            binding.tvIsFavorite.visibility = View.GONE
            binding.btnFavorite.setOnClickListener {
                database.favoriteUserDao().insert(
                    FavoriteUser(null, username, avatar_url)
                )
                showMessage(username+ " " + MESSAGE_SUCCES_ADD_TO_FAVORITE)
                reloadActivity()
            }
        }else{
            showFavoriteOrClear(TYPE_REMOVE_FROM_FAVORITE)
            binding.tvIsFavorite.visibility = View.VISIBLE
            binding.btnRemoveFavorite.setOnClickListener {
               database.favoriteUserDao().delete(favoriteUser)
               reloadActivity()
               showMessage(username+ " " + MESSAGE_SUCCES_REMOVE_TO_FAVORITE)
                reloadActivity()
           }
        }
    }

    private fun showFavoriteOrClear(type: String){
        if (type == TYPE_ADD_TO_FAVORITE){
            binding.apply {
                btnFavorite.visibility = View.VISIBLE
                btnRemoveFavorite.visibility = View.GONE
            }
        }else if(type == TYPE_REMOVE_FROM_FAVORITE){
            binding.apply {
                btnRemoveFavorite.visibility = View.VISIBLE
                btnFavorite.visibility = View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun isUsingNightModeResources(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.share -> {
                val username = intent.getStringExtra(EXTRA_USERNAME)
                var text = "https://github.com/" + username
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, text)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) = if (isLoading) binding.progressbar.visibility = View.VISIBLE else binding.progressbar.visibility = View.GONE

    private fun reloadActivity(){
        finish();
        startActivity(getIntent());
    }

    private fun showMessage(message: String){
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show()
    }
}