package com.example.tawktoassignment.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tawktoassignment.ProfileActivity
import com.example.tawktoassignment.R
import com.example.tawktoassignment.User
import com.example.tawktoassignment.interfaces.UserItemClickListener
import com.squareup.picasso.Picasso

class UserAdapter(val userList: List<User>, val onItemClick : (User)-> Unit) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName : TextView = itemView.findViewById(R.id.userName)
        val userDetails : TextView = itemView.findViewById(R.id.userDetails)
        val userImage : ImageView = itemView.findViewById(R.id.userImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.userName.text = currentUser.login
        holder.userDetails.text = currentUser.id.toString()

        Picasso.get()
            .load(currentUser.avatar_url)
            .into(holder.userImage)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(currentUser)
        }
    }
}