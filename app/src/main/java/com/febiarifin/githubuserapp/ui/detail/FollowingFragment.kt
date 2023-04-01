package com.febiarifin.githubuserapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.febiarifin.githubuserapp.R
import com.febiarifin.githubuserapp.databinding.FragmentFollowBinding
import com.febiarifin.githubuserapp.model.User
import com.febiarifin.githubuserapp.ui.GithubUserAdapter

class FollowingFragment: Fragment(R.layout.fragment_follow) {
    private var _binding : FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowingViewModel
    private lateinit var adapter: GithubUserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(DetailUserActivity.EXTRA_USERNAME).toString()
        _binding = FragmentFollowBinding.bind(view)

        adapter = GithubUserAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvGithubUser.setHasFixedSize(true)
            rvGithubUser.layoutManager = LinearLayoutManager(activity)
            rvGithubUser.adapter = adapter
        }

        adapter.setOnItemClickCallback(object : GithubUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(requireActivity(), DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    startActivity(it)
                }
            }

        })

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)

        viewModel.isLoading.observe(requireActivity(), {
            showLoading(it)
        })

        viewModel.setListFollowing(username)

        viewModel.getListFollowing().observe(viewLifecycleOwner, {
            if (it != null){
                adapter.setList(it)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) = if (isLoading) binding.progressbar.visibility = View.VISIBLE else binding.progressbar.visibility = View.GONE
}