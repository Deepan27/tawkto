package com.example.tawktoassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import com.example.tawktoassignment.adapter.UserAdapter
import com.example.tawktoassignment.interfaces.UserDao
import com.example.tawktoassignment.room.RoomDatabaseBuilder
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private lateinit var userDao: UserDao
    private var id: Int? = null
    private lateinit var tvCompany: TextView
    private lateinit var tvBlog: TextView
    private lateinit var tvFollowers: TextView
    private lateinit var tvFollowing: TextView
    private lateinit var tvName: TextView
    private lateinit var ivUserProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        tvCompany = findViewById(R.id.tvCompany)
        tvName = findViewById(R.id.tvName)
        tvBlog = findViewById(R.id.tvBlog)
        tvFollowers = findViewById(R.id.tvFollowers)
        tvFollowing = findViewById(R.id.tvFollowing)
        ivUserProfile = findViewById(R.id.ivUserProfile)

        val roomDatabase = RoomDatabaseBuilder.buildDatabase(applicationContext)
        id = intent.getIntExtra("id",0)
        userDao = roomDatabase.userDao()
        getUsersFromDatabase()
    }

    private fun getUsersFromDatabase() {
        GlobalScope.launch(Dispatchers.IO) {
            val users = userDao.getUsers()
            for (user in users){
                if (id == user.id){
                    tvCompany.text = user.company ?: "none"
                    tvName.text = user.name ?: "none"
                    tvBlog.text = user.blog ?: "none"
                    tvFollowers.text = user.followers ?: "none"
                    tvFollowing.text = user.following ?: "none"

                    runOnUiThread {
                        Picasso.get()
                            .load(user.avatar_url)
                            .into(ivUserProfile)
                    }

                }
            }
        }
    }
}