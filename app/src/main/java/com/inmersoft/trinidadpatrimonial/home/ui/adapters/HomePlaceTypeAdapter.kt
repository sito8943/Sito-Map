package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceType
import com.inmersoft.trinidadpatrimonial.databinding.MainPlacesSectionsBinding

class HomePlaceTypeAdapter : RecyclerView.Adapter<HomePlaceTypeViewHolder>() {

    private val mainSectionData = mutableListOf<PlaceType>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomePlaceTypeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainPlacesSectionsBinding.inflate(inflater, parent, false)

        return HomePlaceTypeViewHolder(
            binding
        )
    }

    fun setData(mainDataList: List<PlaceType>) {
        mainSectionData.clear()
        mainSectionData.addAll(mainDataList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: HomePlaceTypeViewHolder, position: Int) {

        val subSectionsAdapter = MainPlaceAdapter()

        //TODO (Este adapter es para mostrarle la app a JOSE)
        subSectionsAdapter.setDataList(
            listOf(
                "dqwefwef", "wefwef", "wefwef", "wefwef", "wefwef", "wefwef",
                "wefwef"
            )
        )

        holder.bindData(mainSectionData[position])

        holder.innerRecyclerView.adapter = subSectionsAdapter
    }

    override fun getItemCount(): Int {
        return mainSectionData.size
    }


}