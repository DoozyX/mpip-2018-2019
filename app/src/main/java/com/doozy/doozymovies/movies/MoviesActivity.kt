package com.doozy.doozymovies.movies

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.doozy.doozymovies.R
import com.doozy.doozymovies.movies.adapters.MovieListAdapter
import com.doozy.doozymovies.movies.clients.OMDbApiClient
import com.doozy.doozymovies.movies.models.MovieItem
import com.doozy.doozymovies.movies.models.OMDbResponse
import com.doozy.doozymovies.movies.persistence.repository.MovieItemRepository
import com.doozy.doozymovies.movies.repository.OMDbApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesActivity : AppCompatActivity() {
    val API_KEY: String = "be7fcbb8"
    private var recyclerView: RecyclerView? = null
    private var data: List<MovieItem>? = null
    private var cardViewAdapter: MovieListAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var mHandler: Handler? = null
    private var progressBar: ProgressBar? = null
    private var flickItemRepository: MovieItemRepository? = null
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        mHandler = Handler()
        flickItemRepository = MovieItemRepository(this)
        initViews()
        initList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                syncData(s)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onStart() {
        super.onStart()
        startProgress()
    }

    private fun initViews() {
        val myToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        setSupportActionBar(myToolbar)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun initList() {
        recyclerView = findViewById(R.id.recyclerView)
        cardViewAdapter = MovieListAdapter(this.data)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = cardViewAdapter
        val ldItems = flickItemRepository!!.listAll()
        ldItems.observe(this,
            Observer<List<MovieItem>> { items ->
                if (items == null || items.isEmpty()) {
                    syncData("FINKI")
                } else {
                    cardViewAdapter!!.updateData(items)
                }
            })
    }

    private fun syncData(searchTag: String) {
        val service = OMDbApiClient.getRetrofit().create(OMDbApiInterface::class.java)

        service.listMovies(API_KEY, searchTag).enqueue(object : Callback<OMDbResponse> {
            override fun onResponse(call: Call<OMDbResponse>, response: Response<OMDbResponse>) {
                if (response.isSuccessful) {
                    flickItemRepository!!.deleteAll()
                    Log.d("RESPONSE::", response.toString())

                    if (response.body()!!.Search != null) {
                        for (item in response.body()!!.Search!!) {
                            flickItemRepository!!.insertItem(item)
                        }
                        cardViewAdapter!!.updateData(response.body()!!.Search!!)
                    } else {
                        Toast.makeText(this@MoviesActivity, "FAILED SEARCH", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<OMDbResponse>, t: Throwable) {

            }
        })
    }

    private fun startProgress() {
        Thread(Runnable {
            for (i in 0..29) {
                try {
                    Thread.sleep(500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                mHandler!!.post { progressBar!!.progress = i }

            }
        }).start()
    }
}
