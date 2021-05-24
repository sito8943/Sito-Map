package com.inmersoft.trinidadpatrimonial.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.home.R

class RecycleMainAdapter(
    private val test: List<String>
) : RecyclerView.Adapter<RecycleMainAdapter.RecycleMainAdapterViewHolder>() {
    inner class RecycleMainAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val innerRecyclerView: RecyclerView =
            itemView.findViewById<RecyclerView>(R.id.main_inner_recycleview)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecycleMainAdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_places_card_view, parent, false)
        return RecycleMainAdapterViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: RecycleMainAdapterViewHolder, position: Int) {

        val adapter = RecycleMainInnerAdapter(
            listOf(
                "dqwefwef", "wefwef", "wefwef", "wefwef", "wefwef", "wefwef",
                "wefwef"
            )
        )
        holder.innerRecyclerView.adapter = adapter
    }

    override fun getItemCount(): Int {
        return test.size
    }
}