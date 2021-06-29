package com.kikimore.android_itunes_sample.main.master

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kikimore.android_itunes_sample.R
import com.kikimore.android_itunes_sample.databinding.LastVisitDateTimeBinding
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
    fun lastVisit(): String?
  }

  override fun getItemViewType(position: Int): Int {
    return if (listItemStrategy.lastVisit() != null && position == 0) DateTimeItemViewHolder.layoutId
    else ItemViewHolder.layoutId
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
      DateTimeItemViewHolder.layoutId -> DateTimeItemViewHolder.newInstance(parent)
      else -> ItemViewHolder.newInstance(parent)
    }
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
    if (holder is DateTimeItemViewHolder)
      listItemStrategy.lastVisit()?.also { holder.onBind(it) }
  }

  override fun getItemCount(): Int = listItemStrategy.getListCount()

  private class DateTimeItemViewHolder(private val viewBinding: LastVisitDateTimeBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {
    fun onBind(dateTime: String) {
      viewBinding.root.text = dateTime
    }

    companion object {
      const val layoutId = R.layout.last_visit_date_time
      fun newInstance(parent: ViewGroup): DateTimeItemViewHolder {
        val binding =
          LastVisitDateTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DateTimeItemViewHolder(binding)
      }
    }
  }

  private class ItemViewHolder(private val viewBinding: TrackListItemBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {
    fun onBind(
      imageUrl: String?,
      trackName: String,
      genre: String,
      price: String,
      onSelect: () -> Unit
    ) {
      viewBinding.apply {
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
        val binding =
          TrackListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
      }
    }
  }
}