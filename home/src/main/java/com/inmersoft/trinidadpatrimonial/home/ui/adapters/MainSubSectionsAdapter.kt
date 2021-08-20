package com.inmersoft.trinidadpatrimoniald.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.home.databinding.ItemMainPlacesSubsectionsBinding
import com.inmersoft.trinidadpatrimonial.home.ui.adapters.MainSubSectionsViewHolder

class MainSubSectionsAdapter :
    RecyclerView.Adapter<MainSubSectionsViewHolder>() {

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
    ): MainSubSectionsViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMainPlacesSubsectionsBinding.inflate(inflater, parent, false)

        return MainSubSectionsViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: MainSubSectionsViewHolder, position: Int) {

        holder.bindData(subSectionsData[position])
    }

    override fun getItemCount(): Int {
        return subSectionsData.size
    }
}