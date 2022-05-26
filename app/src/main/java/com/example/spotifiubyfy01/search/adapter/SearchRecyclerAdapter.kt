package com.example.spotifiubyfy01.search.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.search.SearchItem


class SearchRecyclerAdapter(
    private var searchItemList: List<SearchItem>,
    private val onClickListener:(SearchItem) -> Unit
): RecyclerView.Adapter<SearchViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return searchItemList[position].getSearchItemType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return createRespectiveHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = searchItemList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return searchItemList.size
    }

    fun updateList(searchItemList: List<SearchItem>) {
        this.searchItemList = searchItemList
        notifyDataSetChanged()
    }
}
