package com.doozy.doozymovies.movies

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.doozy.doozymovies.R
import com.doozy.doozymovies.movies.persistence.repository.MovieItemRepository

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var img: ImageView
    private lateinit var detailsText: TextView
    private lateinit var movieItemRepository: MovieItemRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        movieItemRepository = MovieItemRepository(this)
        bindViews()
        initViews()
    }

    private fun bindViews() {
        img = findViewById(R.id.img)
        detailsText = findViewById(R.id.detailsMovie)
    }

    private fun initViews() {
        val id = intent.getStringExtra(getString(R.string.extraImageUrl))
        val item = movieItemRepository.getById(id)
        item.observe(this, Observer {
            if (it != null) {
                Glide.with(this)
                    .load(it.Poster)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .crossFade()
                    .into(img)
                detailsText.text = String.format("%s - %s", it.Title, it.Year)
            }
        })

    }
}
