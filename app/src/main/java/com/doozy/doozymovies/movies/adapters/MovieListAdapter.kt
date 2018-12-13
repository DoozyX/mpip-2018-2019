package com.doozy.doozymovies.movies.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.doozy.doozymovies.R
import com.doozy.doozymovies.movies.MovieDetailsActivity
import com.doozy.doozymovies.movies.models.MovieItem

class MovieListAdapter(private var data: List<MovieItem>?) :
    RecyclerView.Adapter<MovieListAdapter.MovieItemViewHolder>() {

    class MovieItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.imageView) as ImageView
        var txtItemInfo: TextView = itemView.findViewById(R.id.txtItemInfo) as TextView
        lateinit var parent: View

        fun bind(entity: MovieItem) {
            Glide.with(itemView.context)
                .load(entity.Poster)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .crossFade()
                .into(imageView)
            txtItemInfo.text = String.format("%s - %s", entity.Title, entity.Year)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MovieItemViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_view_movie_item, viewGroup, false)
        val holder = MovieItemViewHolder(view)
        holder.parent = view
        return holder
    }

    override fun onBindViewHolder(cardItemViewHolder: MovieItemViewHolder, i: Int) {
        val entity = data!![i]
        cardItemViewHolder.bind(entity)
        cardItemViewHolder.parent.setOnClickListener {
            val context = cardItemViewHolder.parent.context
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra(context.getString(R.string.extraImageUrl), entity.imdbID)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        if (data != null)
            return data!!.size
        return 0
    }


    fun updateData(data: List<MovieItem>) {
        this.data = data
        notifyDataSetChanged()
    }
}