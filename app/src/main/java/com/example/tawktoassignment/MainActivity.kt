package com.example.tawktoassignment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tawktoassignment.adapter.UserAdapter
import com.example.tawktoassignment.interfaces.UserDao
import com.example.tawktoassignment.room.RoomDatabaseBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class MainActivity : ComponentActivity() {
    private lateinit var searchView : SearchView
    private lateinit var recyclerView : RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var ivConnectionStatus: ImageView
    private lateinit var tvConnectionStatus: TextView
    private lateinit var rlInternetStatus: RelativeLayout
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        searchView = findViewById(R.id.searchView)
        searchView.queryHint = "search"

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        ivConnectionStatus = findViewById(R.id.connectionStatusImageView)
        tvConnectionStatus = findViewById(R.id.connectionStatusTextView)
        rlInternetStatus = findViewById(R.id.rlInternetStatus)

        val roomDatabase = RoomDatabaseBuilder.buildDatabase(applicationContext)
        userDao = roomDatabase.userDao()

        val isConnected = InternetIndicator.isNetworkAvailable(this)

        if (isConnected) {
            showConnectedUI()
        } else {
            showDisconnectedUI()
            getUsersFromDatabase()
        }

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                runOnUiThread {
                    showConnectedUI()
                    fetchUserList()
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                runOnUiThread {
                    showDisconnectedUI()
                    getUsersFromDatabase()
                }
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)


    }


    private fun showConnectedUI() {
        ivConnectionStatus.setImageResource(R.drawable.internet_icon)
        ivConnectionStatus.visibility = View.VISIBLE
        tvConnectionStatus.text = "Internet is available"
        tvConnectionStatus.visibility = View.VISIBLE

        val handler = Handler()
        val delayMillis = 2000L

        handler.postDelayed({
            rlInternetStatus.visibility = View.GONE
        }, delayMillis)
    }

    private fun showDisconnectedUI() {
        rlInternetStatus.visibility = View.VISIBLE
        ivConnectionStatus.setImageResource(R.drawable.no_internet_icon)
        ivConnectionStatus.visibility = View.VISIBLE
        tvConnectionStatus.visibility = View.VISIBLE
    }


    private fun fetchUserList() {
        val call = RetrofitApiClient.userApiService.getUsers()
        Log.d("TAG", "fetchUserList: " + call)

        call.enqueue(object :retrofit2.Callback<List<User>>{
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if(response.isSuccessful){
                    val userList = response.body()
                    userList?.let {
                        userAdapter = UserAdapter(userList){userList->
                            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                            intent.putExtra("id",userList.id)
                            startActivity(intent)
                        }
                        recyclerView.adapter= userAdapter
                        GlobalScope.launch(Dispatchers.IO) {
                            userDao.insertUsers(userList)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
            }

        })
    }

    private fun getUsersFromDatabase() {
        GlobalScope.launch(Dispatchers.IO) {
            val users = userDao.getUsers()
            runOnUiThread {
                userAdapter = UserAdapter(users){}
                recyclerView.adapter = userAdapter
            }
        }
    }

}