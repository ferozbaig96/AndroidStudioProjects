package com.app.fbulou.myrecyclerviewshimmer.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.app.fbulou.myrecyclerviewshimmer.R
import com.app.fbulou.myrecyclerviewshimmer.viewholders.ContentViewHolder

class ContentAdapter : RecyclerView.Adapter<ContentViewHolder>() {
    var dataList: List<String>? = null
    fun populate(dataList: List<String>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ContentViewHolder?, position: Int) {
        holder?.populate(dataList?.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_layout_content, parent, false)
        return ContentViewHolder(view)
    }
}