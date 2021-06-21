package com.kikimore.android_itunes_sample.main.master

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kikimore.android_itunes_sample.R
import com.kikimore.android_itunes_sample.databinding.TrackListItemBinding

/**
 * Created by: ebaylon.
 * Created on: 16/09/2020.
 */

class ListAdapter(private val listItemStrategy: ListStrategy) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return ItemViewHolder.newInstance(parent)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    if (holder is ItemViewHolder)
      holder.onBind(
        listItemStrategy.getImageUrl(position),
        listItemStrategy.getTrackName(position),
        listItemStrategy.getGenre(position),
        listItemStrategy.getPrice(position),
        listItemStrategy.onSelect(position)
      )
  }

  override fun getItemCount(): Int = listItemStrategy.getListCount()

  private class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun onBind(
      imageUrl: String?,
      trackName: String,
      genre: String,
      price: String,
      onSelect: () -> Unit
    ) {
      TrackListItemBinding.bind(itemView).apply {
        // image url
        Glide.with(root)
          .load(imageUrl)
          .centerInside()
          .placeholder(R.drawable.ic_movie_placeholder)
          .into(imageView)
        // track name
        trackNameTextView.text = trackName
        // genre
        genreTextView.text = genre
        // price
        priceTextView.text = price
        // on select
        trackItemContainer.setOnClickListener { onSelect() }
      }
    }

    companion object {
      const val layoutId = R.layout.track_list_item
      fun newInstance(parent: ViewGroup): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ItemViewHolder(view)
      }
    }
  }
}