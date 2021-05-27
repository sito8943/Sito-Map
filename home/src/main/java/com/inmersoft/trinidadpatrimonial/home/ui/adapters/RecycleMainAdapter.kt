package com.inmersoft.trinidadpatrimoniald.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.home.R

class RecycleMainAdapter : RecyclerView.Adapter<RecycleMainAdapter.RecycleMainAdapterViewHolder>() {

    private val dataList = mutableListOf<String>()

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

    fun setData(mainDataList: List<String>) {
        dataList.clear()
        dataList.addAll(mainDataList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecycleMainAdapterViewHolder, position: Int) {

        val adapter = RecycleMainInnerAdapter()

        //TODO (Este adapter es para mostrarle la app a JOSE)
        adapter.setDataList(
            listOf(
                "dqwefwef", "wefwef", "wefwef", "wefwef", "wefwef", "wefwef",
                "wefwef"
            )
        )

        holder.innerRecyclerView.adapter = adapter
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}