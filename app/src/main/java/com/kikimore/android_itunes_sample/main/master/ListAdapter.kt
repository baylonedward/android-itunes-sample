package com.kikimore.android_itunes_sample.main.master

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kikimore.android_itunes_sample.R
import kotlinx.android.synthetic.main.track_list_item.view.*

/**
 * Created by: ebaylon.
 * Created on: 16/09/2020.
 */

/**
 * Interface of List item functions
 */
interface ListStrategy {
  fun getListCount(): Int
  fun getImageUrl(position: Int): String?
  fun getTrackName(position: Int): String
  fun getGenre(position: Int): String
  fun getPrice(position: Int): String
  fun onSelect(position: Int): () -> Unit
}

class ListAdapter(private val listItemStrategy: ListStrategy) :
  RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater.inflate(R.layout.track_list_item, parent, false)
    return ItemViewHolder(view)
  }

  override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    holder.onBind(
      listItemStrategy.getImageUrl(position),
      listItemStrategy.getTrackName(position),
      listItemStrategy.getGenre(position),
      listItemStrategy.getPrice(position),
      listItemStrategy.onSelect(position)
    )
  }

  override fun getItemCount(): Int = listItemStrategy.getListCount()

  inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun onBind(
      imageUrl: String?,
      trackName: String,
      genre: String,
      price: String,
      onSelect: () -> Unit
    ) {
      // image url
      Glide.with(itemView)
        .load(imageUrl)
        .centerInside()
        .placeholder(R.drawable.ic_movie_placeholder)
        .into(itemView.imageView)
      // track name
      itemView.trackNameTextView.text = trackName
      // genre
      itemView.genreTextView.text = genre
      // price
      itemView.priceTextView.text = price
      // on select
      itemView.trackItemContainer.setOnClickListener { onSelect() }
    }
  }
}