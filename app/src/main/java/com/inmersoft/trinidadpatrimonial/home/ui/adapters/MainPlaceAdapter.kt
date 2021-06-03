package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.databinding.ItemMainPlacesSubsectionsBinding

class MainPlaceAdapter :
    RecyclerView.Adapter<MainPlaceViewHolder>() {

    //TODO ( Se puede usar este adapte como base pero es solo para mostrar la lista en pantalla )
    /***
     *   ESTE ADAPTER ES DE PRUEBA
     *   *
     */


    private val subSectionsData = mutableListOf<String>()


    fun setDataList(newDataList: List<String>) {
        subSectionsData.clear()
        subSectionsData.addAll(newDataList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainPlaceViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMainPlacesSubsectionsBinding.inflate(inflater, parent, false)

        return MainPlaceViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: MainPlaceViewHolder, position: Int) {

        holder.bindData(subSectionsData[position])
    }

    override fun getItemCount(): Int {
        return subSectionsData.size
    }
}