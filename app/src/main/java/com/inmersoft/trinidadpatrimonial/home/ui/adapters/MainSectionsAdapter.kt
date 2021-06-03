package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.databinding.MainPlacesSectionsBinding

class MainSectionsAdapter : RecyclerView.Adapter<MainSectionsViewHolder>() {

    private val mainSectionData = mutableListOf<String>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainSectionsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainPlacesSectionsBinding.inflate(inflater, parent, false)

        return MainSectionsViewHolder(
            binding
        )
    }

    fun setData(mainDataList: List<String>) {
        mainSectionData.clear()
        mainSectionData.addAll(mainDataList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MainSectionsViewHolder, position: Int) {

        val subSectionsAdapter = MainSubSectionsAdapter()

        //TODO (Este adapter es para mostrarle la app a JOSE)
        subSectionsAdapter.setDataList(
            listOf(
                "dqwefwef", "wefwef", "wefwef", "wefwef", "wefwef", "wefwef",
                "wefwef"
            )
        )

        holder.innerRecyclerView.adapter = subSectionsAdapter
    }

    override fun getItemCount(): Int {
        return mainSectionData.size
    }


}