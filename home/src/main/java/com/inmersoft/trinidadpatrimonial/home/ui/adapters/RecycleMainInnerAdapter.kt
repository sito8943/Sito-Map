package com.inmersoft.trinidadpatrimoniald.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.home.R

class RecycleMainInnerAdapter :
    RecyclerView.Adapter<RecycleMainInnerAdapter.SearchListViewHolder>() {

    //TODO ( Se puede usar este adapte como base pero es solo para mostrar la lista en pantalla )
    /***
     *   ESTE ADAPTER ES DE PRUEBA     *
     */

    inner class SearchListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    private val dataInnerList = mutableListOf<String>()


    fun setDataList(newDataList: List<String>) {
        dataInnerList.clear()
        dataInnerList.addAll(newDataList)
        notifyDataSetChanged()
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
        return dataInnerList.size
    }
}