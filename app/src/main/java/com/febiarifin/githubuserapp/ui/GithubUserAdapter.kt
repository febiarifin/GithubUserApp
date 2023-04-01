package com.febiarifin.githubuserapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.febiarifin.githubuserapp.databinding.ItemUserBinding
import com.febiarifin.githubuserapp.model.User

class GithubUserAdapter: RecyclerView.Adapter<GithubUserAdapter.UserViewHolder>() {

    private val listGithubUser = ArrayList<User>()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback=  onItemClickCallback
    }

    fun setList(users: ArrayList<User>){
        listGithubUser.clear()
        listGithubUser.addAll(users)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(user: User){
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(user)
            }

            binding.apply {
                Glide.with(itemView)
                    .load(user.avatar_url)
                    .into(ivProfile)
                tvUsername.text = user.login
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listGithubUser[position])
    }

    override fun getItemCount(): Int = listGithubUser.size

    interface OnItemClickCallback{
        fun onItemClicked(data: User)
    }
}