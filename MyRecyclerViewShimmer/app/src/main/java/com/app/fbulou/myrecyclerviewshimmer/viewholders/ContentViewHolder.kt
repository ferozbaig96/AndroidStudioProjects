package com.app.fbulou.myrecyclerviewshimmer.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.app.fbulou.myrecyclerviewshimmer.R

class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvContent: TextView = itemView.findViewById(R.id.tvContent)

    fun populate(textValue: String?) {
        tvContent.text = textValue
    }
}