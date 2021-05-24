package com.inmersoft.trinidadpatrimonial.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.home.R

class RecycleMainInnerAdapter(
    private val test: List<String>
) : RecyclerView.Adapter<RecycleMainInnerAdapter.SearchListViewHolder>() {
    inner class SearchListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_main_place_recycle_view, parent, false)
        return SearchListViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: SearchListViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return test.size
    }
}