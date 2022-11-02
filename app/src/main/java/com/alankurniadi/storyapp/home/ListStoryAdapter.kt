package com.alankurniadi.storyapp.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alankurniadi.storyapp.R
import com.alankurniadi.storyapp.databinding.StoryItemBinding
import com.alankurniadi.storyapp.model.ListStoryItem
import com.bumptech.glide.Glide

class ListStoryAdapter(private val itemClicked: (itemView: StoryItemBinding, data: ListStoryItem) -> Unit) :
    PagingDataAdapter<ListStoryItem, ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK){

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<ListStoryItem> =
            object : DiffUtil.ItemCallback<ListStoryItem>() {
                override fun areItemsTheSame(
                    oldItem: ListStoryItem,
                    newItem: ListStoryItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: ListStoryItem,
                    newItem: ListStoryItem
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }

    private var listStory = ArrayList<ListStoryItem>()

    @SuppressLint("NotifyDataSetChanged")
//    fun setData(data: List<ListStoryItem>?) {
//        listStory.clear()
//        if (data != null) {
//            listStory.addAll(data)
//        }
//        notifyDataSetChanged()
//    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = StoryItemBinding.bind(itemView)
        fun bind(data: ListStoryItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .into(ivItemPhoto)
                tvItemName.text = data.name

                root.setOnClickListener {
                    itemClicked(binding, data)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.story_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

}
