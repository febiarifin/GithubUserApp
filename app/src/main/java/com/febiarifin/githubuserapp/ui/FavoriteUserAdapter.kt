package com.febiarifin.githubuserapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.febiarifin.githubuserapp.database.FavoriteUser
import com.febiarifin.githubuserapp.databinding.ItemUserBinding

class FavoriteUserAdapter(private val listFavoriteUser: List<FavoriteUser>): RecyclerView.Adapter<FavoriteUserAdapter.ViewHolder>() {

    private var onItemClickCallback: FavoriteUserAdapter.OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: FavoriteUserAdapter.OnItemClickCallback){
        this.onItemClickCallback=  onItemClickCallback
    }

    inner class ViewHolder(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteUser: FavoriteUser) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(favoriteUser)
            }

            binding.apply {
                Glide.with(itemView)
                    .load(favoriteUser.avatar_url)
                    .into(ivProfile)
                tvUsername.text = favoriteUser.username
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFavoriteUser[position])
    }

    override fun getItemCount(): Int = listFavoriteUser.size

    interface OnItemClickCallback{
        fun onItemClicked(data: FavoriteUser)
    }
}